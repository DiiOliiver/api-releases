package br.com.bank.api_releases.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

public interface CrudController<F, ID> {
	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid F form) throws Exception;


	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> update(@PathVariable ID id, @RequestBody @Valid F form) throws Exception;

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Object> delete(@PathVariable ID id) throws Exception;

	@GetMapping("/pageable")
	ResponseEntity<Object> pageable(String search, Pageable pageable) throws Exception;

	@GetMapping
	ResponseEntity<Object> findAll();

	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable ID id) throws Exception;
}
