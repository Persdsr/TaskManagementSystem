package io.github.persdsr.taskmanagementsystem.security.services;

import io.github.persdsr.taskmanagementsystem.entity.UserEntity;
import io.github.persdsr.taskmanagementsystem.exception.response.EmailNotFoundException;
import io.github.persdsr.taskmanagementsystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("User with email: " + email + " not found"));

        return UserDetailsImpl.build(user);
    }


}