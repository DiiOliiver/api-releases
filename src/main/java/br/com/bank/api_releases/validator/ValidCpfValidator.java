package br.com.bank.api_releases.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCpfValidator implements ConstraintValidator<ValidCpf, String> {
	@Override
	public void initialize(ValidCpf constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String cpf, ConstraintValidatorContext context) {
		if (cpf == null || !cpf.matches("\\d{11}")) {
			return false;
		}
		return isValidCpf(cpf);
	}

	private boolean isValidCpf(String cpf) {
		int sum = 0;
		int remainder;

		for (int i = 1; i <= 9; i++) {
			sum += (cpf.charAt(i - 1) - '0') * (11 - i);
		}
		remainder = (sum * 10) % 11;
		if (remainder == 10 || remainder == 11) remainder = 0;
		if (remainder != (cpf.charAt(9) - '0')) return false;

		sum = 0;
		for (int i = 1; i <= 10; i++) {
			sum += (cpf.charAt(i - 1) - '0') * (12 - i);
		}
		remainder = (sum * 10) % 11;
		if (remainder == 10 || remainder == 11) remainder = 0;
		return remainder == (cpf.charAt(10) - '0');
	}
}
