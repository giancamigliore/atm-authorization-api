package com.itti.digital.atm.atm_authorization_api.configs.springsecurity;

public class JwtConfig {
    public static final String SECRET ="LaBurgerDeLaVTiene1Huevo";

    public static final String RSA_PRIVATE="-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAidf8lqZI1wdMGJyZPDY0OlcvibBhaCfSTvjglxHmmmhcS2oS\n" +
            "loLpy+7Ff5T5k6vhisaVcCf8KZNL/VNz2g1L9wHoO/RS1ReWO8MMExT6YV3LBu3j\n" +
            "gv7K0Ti2oCdnJPW25Faxbppxog3mBFcs1Ji6gUXqigKR6TVu1zzKP53bUg0fKRVa\n" +
            "i/Zi2veOInQJ4siRNyyBAINB/3qdU7ofSUBsdQYPXIA+L/oX9a8SlLp739Pt78lr\n" +
            "dWuheqNqkWjD4/8MneOewaewH57/UfGyPcaR4yD34vRWEbl/eQbdpaUoXLOZaUQ0\n" +
            "yXBk2zZOTCn/S2HBnOwSKQDMU+kZnGmbgdZ+NQIDAQABAoIBAHM8yyES0RBbPGXS\n" +
            "/VRWKGGho89jUnul1u9Ip1IEmBn8JzSw7+s+dMCV1Cyt2dx3tG21SRdjhi8nwj59\n" +
            "dvwTY6+Tu5fr0RfuyR0bGcwWHKArrmETncFUyzAQTrUjW9qNRSIty9YgbeFB50s3\n" +
            "qfXt3PXYApTwQxWJIynz201F5iX4Jdr3ORxFavpWDJ8BFRKWWQ1ifEWvseZSvzIp\n" +
            "pdjzG1qQACDSIfro8mYiqONvL4/PniuEv5lFrnsOyz9I1BSs6w4R58Wy8Onm+YyQ\n" +
            "x0VN9w31FPFbigPWgQ8bZQgtiQZwO7fdoI57JhBqNLaOVWqCrxiRlGCTViRCHUsS\n" +
            "ivRo7GECgYEA+IlhYt8Qw8vvC0wFf2O6egXkq8RSLr2poAxqXAl27Mp+qUZ64a7r\n" +
            "5SxnXonr//ydAd8Es2G/HmoDDcKExU+FYWtoczb5UWpXhI+jQvslC9X/glYRadbr\n" +
            "xKYw8eAd0RNgTyn4ltOfJr/Q+hSN6N4Cn7ZQQJZysyu4JLhX+QYl9q0CgYEAjfuo\n" +
            "J7XzIgittg6Xc+px9P3Dm7m9u4gH/vYI4GhCoFxnThWZZT8BeUsfg+fuUBVJ367N\n" +
            "Fj7oLQguliEyIWbMFUH2EiFH54v9NpI02VMxmi4/oAfHjLj+y76RqjWgwOrG1bci\n" +
            "H57rjP1dVXJ7jaaV5nmjia5d8MBZW1yqhvQD/qkCgYBUIt3PBMi+H8mav2+VAoPc\n" +
            "TyZsZ5eSry4/OjS/V4zk+GAMmgl7YSYGfRO5U2OLU08rlP4YY9y5RRYnkFQ2H4d9\n" +
            "xUikyFV6lE9wVtfkEvUlnppS2yQl+lXySniDSWWOo8qUwhafqKBWE9WA83he/tlQ\n" +
            "i/16//NwaDp8Y9OUP9qWOQKBgGpP9SlG5DanqOqZIm55Lbr0CHOiMJ8qzFHsh9kA\n" +
            "OGM6zV96bNGSy9e2mHJ8R8VPK3GBwa7Wi6KsbbWcRZ0dC9MfANGyedS2zXL/JvNT\n" +
            "h+CaoKmDKbgFDUQh/ZAld6j8IsYDyllC9b0sOaaxgjIeej+I9RsypMSQQlhWmplS\n" +
            "PWuBAoGBAKiRsusa28PuEl0Iq65EjZulVcK5KeHh53u3iVkZ2KtzrmlyIEnbmEQD\n" +
            "LosW4S5AcL0JXsiIbgbj3U+14u1bkMZtEjIu8VvvYDbE+6+4COWeb55qJ7pVCVXr\n" +
            "CiEDnP+FrAgHaMSAKovMJY3mUOz7vUSu58GhoYijGLHiDogZu6l6\n" +
            "-----END RSA PRIVATE KEY-----";

    public static final String RSA_PUBLIC="-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAidf8lqZI1wdMGJyZPDY0\n" +
            "OlcvibBhaCfSTvjglxHmmmhcS2oSloLpy+7Ff5T5k6vhisaVcCf8KZNL/VNz2g1L\n" +
            "9wHoO/RS1ReWO8MMExT6YV3LBu3jgv7K0Ti2oCdnJPW25Faxbppxog3mBFcs1Ji6\n" +
            "gUXqigKR6TVu1zzKP53bUg0fKRVai/Zi2veOInQJ4siRNyyBAINB/3qdU7ofSUBs\n" +
            "dQYPXIA+L/oX9a8SlLp739Pt78lrdWuheqNqkWjD4/8MneOewaewH57/UfGyPcaR\n" +
            "4yD34vRWEbl/eQbdpaUoXLOZaUQ0yXBk2zZOTCn/S2HBnOwSKQDMU+kZnGmbgdZ+\n" +
            "NQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

}
