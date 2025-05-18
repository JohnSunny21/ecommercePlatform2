package com.devtale.ecommerceplatform2;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class Ecommerceplatform2Application {

	public static void main(String[] args) {
		byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
		String base64Key = Base64.getEncoder().encodeToString(keyBytes);
		System.out.println("Base64-encoded key: " + base64Key);
		SpringApplication.run(Ecommerceplatform2Application.class, args);
	}

}
