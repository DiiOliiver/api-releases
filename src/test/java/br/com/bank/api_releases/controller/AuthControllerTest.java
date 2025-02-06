package br.com.bank.api_releases.controller;

import br.com.bank.api_releases.config.SecurityConfig;
import br.com.bank.api_releases.dto.AuthDTO;
import br.com.bank.api_releases.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {
	@Value("${auth.username}")
	private String authUsername;

	@Value("${auth.password}")
	private String authPassword;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private UserDetailsService userDetailsService;

	private AuthDTO authDTOSuccess;

	private AuthDTO authDTOFailure;

	@BeforeEach
	void setUp() {
		authDTOSuccess = new AuthDTO();
		authDTOSuccess.setUsername(authUsername);
		authDTOSuccess.setPassword(authPassword);

		authDTOFailure = authDTOSuccess;
		authDTOFailure.setPassword("SenhaInvalida");
	}

	@Test
	@WithMockUser
	public void testLoginSuccess() throws Exception {
		Authentication mockAuthentication = Mockito.mock(Authentication.class);
		Mockito.when(authenticationManager.authenticate(Mockito.any()))
			.thenReturn(mockAuthentication);
		Mockito.when(mockAuthentication.getPrincipal()).thenReturn(authDTOSuccess.getUsername());
		Mockito.when(jwtTokenProvider.generateToken(Mockito.any())).thenReturn("mocked-token");

		ObjectMapper mapper = new ObjectMapper();
		String jsonRequest = mapper.writeValueAsString(authDTOSuccess);

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(content().string("mocked-token"));
	}

	@Test
	@WithMockUser
	public void testLoginFailure() throws Exception {
		Mockito.when(authenticationManager.authenticate(Mockito.any()))
			.thenThrow(new RuntimeException("Usuário inexistente ou senha inválida"));

		ObjectMapper mapper = new ObjectMapper();
		String jsonRequest = mapper.writeValueAsString(authDTOFailure);

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest))
			.andExpect(status().isUnauthorized())
			.andExpect(content().string("Usuário inexistente ou senha inválida"));
	}
}
