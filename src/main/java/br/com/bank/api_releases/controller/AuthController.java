package br.com.bank.api_releases.controller;

import br.com.bank.api_releases.dto.AuthDTO;
import br.com.bank.api_releases.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/auth")
@Tag(name = "Auth", description = "Logar no sistema")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody AuthDTO authDTO) {
		try {
			Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = this.jwtTokenProvider.generateToken(authentication);
			return ResponseEntity.ok(token);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("Usuário inexistente ou senha inválida");
		}
	}
}
