package br.com.bank.api_releases.controller;

import br.com.bank.api_releases.exceptions.AccountException;
import br.com.bank.api_releases.forms.ReleaseForm;
import br.com.bank.api_releases.service.release.ReleaseCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/releases")
@Tag(name = "Releases", description = "Lançamentos de Contas Bancárias")
public class ReleaseCrudController {

	@Autowired
	private ReleaseCrudService crudService;


	@PostMapping
	@Transactional
	@Operation(summary = "Cria um novo lançamento")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Lançamento criado com sucesso"),
		@ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<Object> create(@RequestBody @Valid ReleaseForm form) throws Exception {
		try {
			this.crudService.create(form);
			return ResponseEntity.status(HttpStatus.CREATED)
				.body("Conta " + form.getMembershipNumber() + ": Lançamento registrado com sucesso!");
		} catch (AccountException accountException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(accountException.getMessage());
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	@Operation(summary = "Exclui um lançamento pelo ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lançamento removido com sucesso"),
		@ApiResponse(responseCode = "404", description = "Lançamento não encontrado"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<Object> delete(@PathVariable String id) throws Exception {
		try {
			this.crudService.delete(id);
			return ResponseEntity.ok("O lançamento não está mais disponível!");
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@GetMapping
	@Operation(summary = "Lista todos os lançamentos")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<Object> findAll() {
		try {
			return ResponseEntity.ok().body(this.crudService.findAll());
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um lançamento pelo ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Lançamento encontrado"),
		@ApiResponse(responseCode = "404", description = "Lançamento não encontrado"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	public ResponseEntity<Object> findById(String id) throws Exception {
		try {
			return ResponseEntity.ok().body(this.crudService.findById(id));
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.internalServerError().body(exception.getMessage());
		}
	}
}
