package com.itti.digital.atm.atm_authorization_api.service;


import com.itti.digital.atm.atm_authorization_api.entities.User;

public interface IUserService {

    public User findByUsername(String username);
}
