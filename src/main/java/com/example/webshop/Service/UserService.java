package com.example.webshop.Service;

import com.example.webshop.Model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    User findById(String username);

    boolean userExists(String username);

    User save(User user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User registerUser(User user);
}
