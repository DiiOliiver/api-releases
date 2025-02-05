package br.com.bank.api_releases.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidCpfValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCpf {
	String message() default "CPF inválido!";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
