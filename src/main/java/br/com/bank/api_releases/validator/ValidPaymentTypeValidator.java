package br.com.bank.api_releases.validator;

import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidPaymentTypeValidator implements ConstraintValidator<ValidPaymentType, PaymentTypeEnum> {
	@Override
	public void initialize(ValidPaymentType constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(PaymentTypeEnum paymentType, ConstraintValidatorContext context) {
		return paymentType != null && Arrays.asList(PaymentTypeEnum.values()).contains(paymentType);
	}
}
