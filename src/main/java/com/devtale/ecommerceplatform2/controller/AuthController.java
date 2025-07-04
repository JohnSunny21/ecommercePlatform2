package com.devtale.ecommerceplatform2.controller;

import com.devtale.ecommerceplatform2.request.LoginRequest;
import com.devtale.ecommerceplatform2.response.ApiResponse;
import com.devtale.ecommerceplatform2.response.JwtResponse;
import com.devtale.ecommerceplatform2.security.jwt.JwtUtils;
import com.devtale.ecommerceplatform2.security.user.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try{
            Authentication authentication  = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),request.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(),jwt);
            return ResponseEntity.ok(new ApiResponse("Login Successfull",jwtResponse));
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
