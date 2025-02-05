package br.com.bank.api_releases.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAmountValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAmount {
	String message() default "O valor deve estar no formato válido: '99999.99' ou '999,99'";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
