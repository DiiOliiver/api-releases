package br.com.bank.api_releases.forms;

import br.com.bank.api_releases.validator.StrongPassword;
import br.com.bank.api_releases.validator.UniqueAccount;
import br.com.bank.api_releases.validator.ValidCpf;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UniqueAccount
public class AccountForm {
	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
	@Schema(description = "Nome do usuário", example = "Diego Feitosa")
	private String name;

	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "Formato de e-mail inválido")
	@Schema(description = "E-mail do usuário", example = "diego@email.com")
	private String email;

	@NotBlank(message = "O CPF é obrigatório")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
	@ValidCpf
	@Schema(description = "CPF do usuário (somente números)", example = "12345678901")
	private String cpf;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
	@StrongPassword
	@Schema(description = "Senha de acesso do usuário", example = "Senha@123")
	private String password;
}
