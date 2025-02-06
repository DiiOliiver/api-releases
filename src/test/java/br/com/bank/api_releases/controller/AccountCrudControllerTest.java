package br.com.bank.api_releases.controller;


import br.com.bank.api_releases.config.SecurityConfig;
import br.com.bank.api_releases.dto.AuthDTO;
import br.com.bank.api_releases.forms.AccountForm;
import br.com.bank.api_releases.model.AccountModel;
import br.com.bank.api_releases.repository.AccountRepositoryJpa;
import br.com.bank.api_releases.security.JwtTokenProvider;
import br.com.bank.api_releases.service.CrudService;
import br.com.bank.api_releases.validator.UniqueAccountValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountCrudController.class)
@Import(SecurityConfig.class)
class AccountCrudControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CrudService crudService;

	@MockBean
	private AccountRepositoryJpa accountRepositoryJpa;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private UserDetailsService userDetailsService;

	@MockBean
	private UniqueAccountValidator uniqueAccountValidator;

	private AccountForm accountForm;

	@BeforeEach
	void setUp() throws Exception {
		when(accountRepositoryJpa.findById(any(UUID.class))).thenReturn(Optional.of(new AccountModel()));
		when(uniqueAccountValidator.isValid(any(), any())).thenReturn(true);

		accountForm = new AccountForm();
		accountForm.setName("John Doe");
		accountForm.setEmail("john.doe@example.com");
		accountForm.setCpf("55242545020");
		accountForm.setPassword("Admin@123");
	}

	@Test
	void createAccount_Success() throws Exception {
		when(crudService.create(any(AccountForm.class))).thenReturn(UUID.randomUUID().toString());

		mockMvc.perform(post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(accountForm)))
			.andExpect(status().isCreated())
			.andExpect(content().string("A conta de John Doe foi cadastrado!"));
	}

	@Test
	void updateAccount_Success() throws Exception {
		String accountId = UUID.randomUUID().toString();
		when(crudService.update(eq(accountId), any(AccountForm.class))).thenReturn("Conta atualizada!");

		mockMvc.perform(put("/api/accounts/{id}", accountId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(accountForm)))
			.andExpect(status().isOk())
			.andExpect(content().string("A conta de John Doe foi atualizada!"));
	}

	@Test
	void deleteAccount_Success() throws Exception {
		String accountId = UUID.randomUUID().toString();
		doNothing().when(crudService).delete(accountId);

		mockMvc.perform(delete("/api/accounts/{id}", accountId))
			.andExpect(status().isOk())
			.andExpect(content().string("A conta não está mais disponível!"));
	}
}

