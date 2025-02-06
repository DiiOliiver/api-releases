package br.com.bank.api_releases.controller;

import br.com.bank.api_releases.dto.AccountDTO;
import br.com.bank.api_releases.forms.AccountForm;
import br.com.bank.api_releases.service.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/accounts")
@Tag(name = "Accounts", description = "Gerenciamento de Contas Bancárias")
public class AccountCrudController implements CrudController<AccountForm, String> {
	@Autowired
	private CrudService<AccountDTO, AccountForm> crudService;

	@Override
	@Operation(summary = "Cria uma nova conta")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<Object> create(AccountForm form) throws Exception {
		try {
			this.crudService.create(form);
			return ResponseEntity.status(HttpStatus.CREATED).body("A conta de " + form.getName() + " foi cadastrado!");
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@Override
	@Operation(summary = "Atualiza uma conta existente")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
		@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	public ResponseEntity<Object> update(String id, AccountForm form) throws Exception {
		try {
			this.crudService.update(id, form);
			return ResponseEntity.ok("A conta de " + form.getName() + " foi atualizada!");
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@Override
	@Operation(summary = "Deleta uma conta")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conta deletada com sucesso"),
		@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	public ResponseEntity<Object> delete(String id) throws Exception {
		try {
			this.crudService.delete(id);
			return ResponseEntity.ok("A conta não está mais disponível!");
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@Override
	@Operation(summary = "Busca contas com paginação")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de contas paginada"),
		@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	public ResponseEntity<Object> pageable(String search, Pageable pageable) throws Exception {
		try {
			return ResponseEntity.ok().body(this.crudService.findPageable(search, pageable));
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@Override
	@Operation(summary = "Busca todas as contas")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista de todas as contas"),
		@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	public ResponseEntity<Object> findAll() {
		try {
			return ResponseEntity.ok().body(this.crudService.findAll());
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@Override
	@Operation(summary = "Busca uma conta por ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Conta encontrada"),
		@ApiResponse(responseCode = "404", description = "Conta não encontrada"),
		@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
	})
	public ResponseEntity<Object> findById(String id) throws Exception {
		try {
			return ResponseEntity.ok().body(this.crudService.findById(id));
		} catch (Exception exception) {
			log.error("Erro inesperado: {}", exception.getMessage(), exception);
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}
}
