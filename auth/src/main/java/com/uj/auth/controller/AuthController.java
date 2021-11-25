package com.uj.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uj.auth.model.AuthResponse;
import com.uj.auth.model.LoginRequest;
import com.uj.auth.security.TokenProvider;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenProvider tokenProvider;

	@PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

 			if (authentication.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String token = tokenProvider.createToken(authentication);
				return ResponseEntity.ok(new AuthResponse(token));
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Invalid username/password...", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<String>("Bad credentials", HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
