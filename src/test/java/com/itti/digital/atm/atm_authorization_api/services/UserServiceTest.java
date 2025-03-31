package com.itti.digital.atm.atm_authorization_api.services;


import com.itti.digital.atm.atm_authorization_api.dao.IUserDao;
import com.itti.digital.atm.atm_authorization_api.entities.Role;
import com.itti.digital.atm.atm_authorization_api.entities.User;
import com.itti.digital.atm.atm_authorization_api.service.impl.UserService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    IUserDao dao = mock(IUserDao.class);
    String username = "k3app";
    UserService service = new UserService(dao);

    @AfterEach
    public void cleanup() {
        Mockito.clearAllCaches();
    }

    @Test
    void findByUsername_validUserNamePresented_userReturned()throws Exception{
        User user = new User();
        user.setUsername("k3app");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setId(1l);
        user.setRoles(new ArrayList<Role>());

        when(dao.findByUsername(username)).thenReturn(user);

        //WHEN
        User  response = service.findByUsername(username);

        //THEN
        assertThat(response).extracting("username").isEqualTo(username);
        verify(dao).findByUsername(username);

    }

    @Test
    void loadUserByUsername_validUserNamePresented_userReturned()throws Exception{
        User user = new User();
        user.setUsername("k3app");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setId(1l);
        user.setRoles(new ArrayList<Role>());
        when(dao.findByUsername(username)).thenReturn(user);

        //WHEN
        UserDetails response = service.loadUserByUsername(username);

        //THEN
        assertThat(response).extracting("username").isEqualTo(username);
        verify(dao).findByUsername(username);

    }

    @Test
    void loadUserByUsername_validUserNamePresented_nullUserReturned()throws Exception{
        User user = new User();
        user.setUsername("k3app");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setId(1l);
        user.setRoles(new ArrayList<Role>());

        when(dao.findByUsername(username)).thenReturn(null);

        //WHEN
        ThrowableAssert.ThrowingCallable throwingCallable = () -> service.loadUserByUsername(username);

        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(UsernameNotFoundException.class);
        verify(dao).findByUsername(username);

    }

    @Test
    void loadUserByUsername_validUserNamePresented_userReturnedWithMultiRole()throws Exception{
        User user = new User();
        user.setUsername("k3app");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setId(1l);
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role();
        role.setId(1l);
        role.setName("ROLE_ADMIN");
        if(role.getId()>0) {
            roles.add(role);
        }
        roles.add(new Role(2l,"ROLE_USER"));
        user.setRoles(roles);
        System.out.println(user.getId());
        when(dao.findByUsername(username)).thenReturn(user);

        //WHEN
        UserDetails response = service.loadUserByUsername(username);

        //THEN
        assertThat(response).extracting("username").isEqualTo(username);
        verify(dao).findByUsername(username);

    }

}
