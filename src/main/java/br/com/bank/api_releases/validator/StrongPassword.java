package br.com.bank.api_releases.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
	String message() default "A senha deve conter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma letra minúscula, um número e um caractere especial.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
