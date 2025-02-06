package br.com.bank.api_releases.controller;

import br.com.bank.api_releases.config.SecurityConfig;
import br.com.bank.api_releases.dto.ReleaseDTO;
import br.com.bank.api_releases.forms.ReleaseForm;
import br.com.bank.api_releases.helper.ContaBankHelper;
import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import br.com.bank.api_releases.security.JwtTokenProvider;
import br.com.bank.api_releases.service.release.ReleaseCrudService;
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
import java.util.Arrays;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReleaseCrudController.class)
@Import(SecurityConfig.class)
class ReleaseCrudControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ReleaseCrudService releaseCrudService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private UserDetailsService userDetailsService;

	private ReleaseForm releaseForm;

	private String numeroConta = ContaBankHelper.gerarNumeroConta();

	private UUID uuid = UUID.randomUUID();

	@BeforeEach
	void setUp() {
		releaseForm = new ReleaseForm();
		releaseForm.setMembershipNumber(numeroConta);
		releaseForm.setAmount("500.00");
		releaseForm.setPaymentType(PaymentTypeEnum.CREDIT);
	}

	@Test
	void create_Success() throws Exception {
		ReleaseDTO releaseDTO = new ReleaseDTO(UUID.randomUUID(), null, "500.00", "CREDIT", null);
		when(releaseCrudService.create(any(ReleaseForm.class))).thenReturn(releaseDTO);

		mockMvc.perform(post("/api/releases")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(releaseForm)))
			.andExpect(status().isCreated())
			.andExpect(content().string("Conta "+numeroConta+": Lançamento registrado com sucesso!"));
	}

	@Test
	void delete_Success() throws Exception {
		doNothing().when(releaseCrudService).delete(anyString());

		mockMvc.perform(delete("/api/releases/{id}", UUID.randomUUID().toString()))
			.andExpect(status().isOk())
			.andExpect(content().string("O lançamento não está mais disponível!"));
	}

	@Test
	void findAll_Success() throws Exception {
		ReleaseDTO releaseDTO = new ReleaseDTO(uuid, null, "500.00", "CREDIT", null);
		when(releaseCrudService.findAll()).thenReturn(Arrays.asList(releaseDTO));

		mockMvc.perform(get("/api/releases"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(uuid.toString()))
			.andExpect(jsonPath("$[0].amount").value("500.00"))
			.andExpect(jsonPath("$[0].paymentType").value("CREDIT"));
	}

	@Test
	void findById_Success() throws Exception {
		ReleaseDTO releaseDTO = new ReleaseDTO(uuid, null, "500.00", "CREDIT", null);

		when(releaseCrudService.findById(anyString())).thenReturn(releaseDTO);

		mockMvc.perform(get("/api/releases/{id}", uuid.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(uuid.toString()))
			.andExpect(jsonPath("$.amount").value("500.00"))
			.andExpect(jsonPath("$.paymentType").value("CREDIT"));
	}
}