package com.devtale.ecommerceplatform2.repository;

import com.devtale.ecommerceplatform2.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByProductId(Long id);
}
