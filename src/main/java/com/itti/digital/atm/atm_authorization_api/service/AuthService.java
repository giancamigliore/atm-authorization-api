package com.itti.digital.atm.atm_authorization_api.service;


import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.models.authentication.RequestGetWebToken;
import com.itti.digital.atm.atm_authorization_api.models.authentication.ResponseGetWebToken;

/**
 * @author Giancarlo Migliore
 */

public interface AuthService {



    ResponseGetWebToken getWebToken(RequestGetWebToken getWebToken) throws AlfaMsException;
}
