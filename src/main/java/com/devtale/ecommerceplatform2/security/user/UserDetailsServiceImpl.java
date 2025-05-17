package com.devtale.ecommerceplatform2.security.user;

import com.devtale.ecommerceplatform2.exceptions.ResourceNotFoundException;
import com.devtale.ecommerceplatform2.model.User;
import com.devtale.ecommerceplatform2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return UserDetailsImpl.buildUserDetails(user);

    }
}
