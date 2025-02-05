package br.com.bank.api_releases.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAmountValidator implements ConstraintValidator<ValidAmount, String> {
	private static final String AMOUNT_PATTERN = "^(\\d{1,13}(\\.\\d{1,2})?)$";

	@Override
	public void initialize(ValidAmount constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String amount, ConstraintValidatorContext context) {
		return amount != null && amount.matches(AMOUNT_PATTERN);
	}
}
