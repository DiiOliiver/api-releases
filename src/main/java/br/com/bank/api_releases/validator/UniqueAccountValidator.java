package br.com.bank.api_releases.validator;

import br.com.bank.api_releases.dto.RequestParameterDTO;
import br.com.bank.api_releases.forms.AccountForm;
import br.com.bank.api_releases.model.AccountModel;
import br.com.bank.api_releases.repository.AccountRepositoryJpa;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

public class UniqueAccountValidator implements ConstraintValidator<UniqueAccount, AccountForm> {
	@Autowired
	private AccountRepositoryJpa accountRepositoryJpa;

	@Override
	public void initialize(UniqueAccount constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(AccountForm form, ConstraintValidatorContext context) {
		if (form == null) {
			return true;
		}

		HttpServletRequest request =
			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		return request.getMethod().equals("POST") ? validPost(form, context) : validPut(request, form, context);
	}

	public boolean validPost(AccountForm form, ConstraintValidatorContext context) {
		boolean emailExists = accountRepositoryJpa.existsByEmail(form.getEmail());
		boolean cpfExists = accountRepositoryJpa.existsByCpf(form.getCpf());

		if (emailExists || cpfExists) {
			context.disableDefaultConstraintViolation();
			if (emailExists) {
				context.buildConstraintViolationWithTemplate("O e-mail já está cadastrado!")
					.addPropertyNode("email").addConstraintViolation();
			}
			if (cpfExists) {
				context.buildConstraintViolationWithTemplate("O CPF já está cadastrado!")
					.addPropertyNode("cpf").addConstraintViolation();
			}
			return false;
		}

		return true;
	}

	public boolean validPut(HttpServletRequest request, AccountForm form, ConstraintValidatorContext context) {

		Object pathVariables = Objects.requireNonNull(request.getAttribute("org.springframework.web.servlet.View.pathVariables"));
		RequestParameterDTO requestParameterDTO = new Gson().fromJson(pathVariables.toString(), RequestParameterDTO.class);

		AccountModel accountModel = this.accountRepositoryJpa.findById(UUID.fromString(requestParameterDTO.getId()))
			.orElseThrow(() -> new RuntimeException("Conta não encontrada!"));

		boolean emailExists = accountRepositoryJpa.existsByEmailAndIdNot(form.getEmail(), accountModel.getId());
		boolean cpfExists = accountRepositoryJpa.existsByCpfAndIdNot(form.getCpf(), accountModel.getId());

		if (emailExists || cpfExists) {
			context.disableDefaultConstraintViolation();
			if (emailExists) {
				context.buildConstraintViolationWithTemplate("O e-mail já está cadastrado!")
					.addPropertyNode("email").addConstraintViolation();
			}
			if (cpfExists) {
				context.buildConstraintViolationWithTemplate("O CPF já está cadastrado!")
					.addPropertyNode("cpf").addConstraintViolation();
			}
			return false;
		}

		return true;
	}
}
