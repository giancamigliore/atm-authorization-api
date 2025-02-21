package com.itti.digital.atm.atm_authorization_api.service;


import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.models.authorization.RequestAuthorization;
import com.itti.digital.atm.atm_authorization_api.models.authorization.ResponseAuthorization;

/**
 * @author Giancarlo Migliore
 */

public interface AuthorizationService {


    ResponseAuthorization processAtmTransaction(RequestAuthorization request) throws AlfaMsException;
}
