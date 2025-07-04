package com.devtale.ecommerceplatform2.repository;

import com.devtale.ecommerceplatform2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
