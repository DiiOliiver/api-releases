package br.com.bank.api_releases.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueAccountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueAccount {
	String message() default "E-mail, CPF ou número de associação já cadastrado!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
