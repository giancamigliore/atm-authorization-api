package com.itti.digital.atm.atm_authorization_api.utils;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.nio.ByteBuffer;
import java.security.*;

public class CryptoUtils {
    private static final CipherMode CIPHER_MODE = CipherMode.GCM;
    public static final int TAG_LENGTH_BYTE = 16;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int ENCRYPTION_KEY_LENGTH_BYTE = 256;
    private static final String HASH_ALGORITHM = "SHA3-256";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String PUBLIC_PRIVATE_KEY_ALGORITHM = "RSA/None/OAEPWITHSHA-256ANDMGF1PADDING";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private SecureRandom secureRandom;
    private Provider provider;

    public CryptoUtils() {
        this.provider = new BouncyCastleProvider();
        this.secureRandom = new SecureRandom();
    }

    public byte[] sign(PrivateKey privateKey, byte[] privateText) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(privateText);
        return signature.sign();
    }

    public boolean verifySignature(PublicKey publicClientKey, byte[] data, byte[] signature) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        Signature signatureServer = Signature.getInstance(SIGNATURE_ALGORITHM);
        signatureServer.initVerify(publicClientKey);
        signatureServer.update(data);
        return signatureServer.verify(signature);
    }

    public byte[] keyWrap(PublicKey publicKey, SecretKey data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(PUBLIC_PRIVATE_KEY_ALGORITHM);
        cipher.init(3, publicKey);
        return cipher.wrap(data);
    }

    public Key keyUnwrap(PrivateKey privateKey, byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(PUBLIC_PRIVATE_KEY_ALGORITHM);
        cipher.init(4, privateKey);
        return cipher.unwrap(encrypted, ENCRYPTION_ALGORITHM, 3);
    }

    public byte[] encryptData(SecretKey encryptionKey, byte[] rawData, byte[] associatedData) throws IllegalStateException, InvalidCipherTextException {
        if (encryptionKey.getEncoded().length < 32) {
            throw new IllegalArgumentException("largo de key debe ser mayor a 32 bytes");
        } else {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            this.secureRandom.nextBytes(iv);
            GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            AEADParameters keyParameters = new AEADParameters(new KeyParameter(encryptionKey.getEncoded()), TAG_LENGTH_BYTE*8, iv);
            cipher.init(true, keyParameters);
            if (associatedData != null) {
                cipher.processAADBytes(associatedData, 0, associatedData.length);
            }

            var bcCiphertext = new byte[rawData.length + TAG_LENGTH_BYTE];
            var offset = cipher.processBytes(rawData, 0, rawData.length, bcCiphertext, 0);
            cipher.doFinal(bcCiphertext, offset);

            return packCipherDataBytes(bcCiphertext, iv);
        }
    }

    private byte[] packCipherDataBytes(byte[] encryptedBytes, byte[] iv) {
        var dataSize = encryptedBytes.length + iv.length + 1;
        if (CIPHER_MODE == CipherMode.GCM)
            dataSize += 1;

        ByteBuffer packedData = ByteBuffer.allocate(dataSize);
        packedData.put((byte)iv.length);
        packedData.put((byte)TAG_LENGTH_BYTE);
        packedData.put(iv);
        packedData.put(encryptedBytes);

        return packedData.array();
    }

    public byte[] decryptData(Key key, byte[] cipherData, byte[] associatedData) throws IllegalStateException, InvalidCipherTextException {
        int index = 0;
        byte ivSize = cipherData[index];
        index += 1;

        byte tagSize = cipherData[index];
        index += 1;

        if(tagSize != TAG_LENGTH_BYTE)
            throw new InvalidCipherTextException("Tag size incorrecto");

        byte[] iv = new byte[ivSize];
        System.arraycopy(cipherData, index, iv, 0, ivSize);
        index += ivSize;

        byte[] encryptedBytes = new byte[cipherData.length - index];
        System.arraycopy(cipherData, index, encryptedBytes, 0, encryptedBytes.length);

        GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
        AEADParameters keyParameters = new AEADParameters(new KeyParameter(key.getEncoded()), TAG_LENGTH_BYTE*8, iv);
        cipher.init(false, keyParameters);
        if (associatedData != null) {
            cipher.processAADBytes(associatedData, 0, associatedData.length);
        }

        var bcCiphertext = encryptedBytes;
        var plaintextBytes = new byte[encryptedBytes.length];

        var offset = cipher.processBytes(bcCiphertext, 0, bcCiphertext.length, plaintextBytes, 0);
        cipher.doFinal(plaintextBytes, offset);

        var res = new byte[plaintextBytes.length - TAG_LENGTH_BYTE];
        System.arraycopy(plaintextBytes,0,res,0, plaintextBytes.length - TAG_LENGTH_BYTE);

        return res;
    }

    public byte[] signAndEncryptSign(byte[] data, byte[] associatedData, PrivateKey privateKey, PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchProviderException, IllegalStateException, InvalidCipherTextException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM, this.provider.getName());
        digest.update(data);
        byte[] hash = digest.digest();
        byte[] signature = this.sign(privateKey, hash);
        KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, this.provider.getName());
        keygen.init(ENCRYPTION_KEY_LENGTH_BYTE);
        SecretKey key = keygen.generateKey();
        byte[] encKey = this.keyWrap(publicKey, key);
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + signature.length);
        byteBuffer.put(signature);
        byteBuffer.put(data);
        byte[] completeData = byteBuffer.array();
        byte[] encData = this.encryptData(key, completeData, associatedData);
        byteBuffer = ByteBuffer.allocate(encKey.length + encData.length);
        byteBuffer.put(encKey);
        byteBuffer.put(encData);
        byte[] message = byteBuffer.array();
        digest.update(message);
        byte[] signature2 = this.sign(privateKey, digest.digest());
        byteBuffer = ByteBuffer.allocate(message.length + signature2.length);
        byteBuffer.put(signature2);
        byteBuffer.put(message);
        return byteBuffer.array();
    }

    public byte[] verifyDecryptAndVerify(byte[] data, byte[] associatedData, PrivateKey privateKey, PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchPaddingException, NoSuchProviderException, IllegalStateException, InvalidCipherTextException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byte[] encSignature = new byte[ENCRYPTION_KEY_LENGTH_BYTE];
        byteBuffer.get(encSignature);
        byte[] data1 = new byte[byteBuffer.remaining()];
        byteBuffer.get(data1);
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM, this.provider.getName());
        digest.update(data1);
        if (this.verifySignature(publicKey, digest.digest(), encSignature)) {
            byteBuffer = ByteBuffer.wrap(data1);
            byte[] encKey = new byte[ENCRYPTION_KEY_LENGTH_BYTE];
            byteBuffer.get(encKey);
            byte[] encData = new byte[byteBuffer.remaining()];
            byteBuffer.get(encData);
            Key key = this.keyUnwrap(privateKey, encKey);
            byte[] decDataSignature = this.decryptData(key, encData, associatedData);
            byteBuffer = ByteBuffer.wrap(decDataSignature);
            byte[] dataSignature = new byte[256];
            byteBuffer.get(dataSignature);
            byte[] decData = new byte[byteBuffer.remaining()];
            byteBuffer.get(decData);
            digest.update(decData);
            if (this.verifySignature(publicKey, digest.digest(), dataSignature)) {
                return decData;
            } else {
                throw new SignatureException();
            }
        } else {
            throw new SignatureException();
        }
    }

    public enum CipherMode
    {
        CBC,
        GCM
    }

}

