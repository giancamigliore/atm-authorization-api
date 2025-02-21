package com.itti.digital.atm.atm_authorization_api.dao;


import com.itti.digital.atm.atm_authorization_api.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User,Long> {
    public User findByUsername(String username);


}
