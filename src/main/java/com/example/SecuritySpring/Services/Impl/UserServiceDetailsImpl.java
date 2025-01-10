package com.example.SecuritySpring.Services.Impl;

import com.example.SecuritySpring.Application.Model.SecurityUser;
import com.example.SecuritySpring.Application.Model.User;
import com.example.SecuritySpring.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {

    // metodo para logear un usario
    // se compara el username con lo que hay en la base de datos si existe
    // se validad los datos
    @Autowired
    private IUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
// buscar al usuario en la base de datos
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("no se encontro usuario con username: " + username));
        return new SecurityUser(user);
    }
}
