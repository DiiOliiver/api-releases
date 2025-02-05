package br.com.bank.api_releases.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPaymentTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentType {
	String message() default "Tipo de pagamento inválido!";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
