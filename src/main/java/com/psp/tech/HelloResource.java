package com.psp.tech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.psp.tech.model.AuthenticationRequest;
import com.psp.tech.model.AuthenticationResponse;
import com.psp.tech.util.JwtUtil;

@RestController
public class HelloResource {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	MyUserDetailService userDetailService;

	@Autowired
	JwtUtil jwtUtil;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello world";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username and password");
		}

		UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
