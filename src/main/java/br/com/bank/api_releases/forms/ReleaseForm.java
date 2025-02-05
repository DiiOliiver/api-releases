package br.com.bank.api_releases.forms;

import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import br.com.bank.api_releases.validator.ValidAmount;
import br.com.bank.api_releases.validator.ValidPaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseForm {
	@NotBlank(message = "O número de associação é obrigatório")
	@Size(min = 5, max = 80, message = "O número de associação deve ter entre 5 e 80 caracteres")
	@Schema(description = "Número de associação do usuário", example = "12345-ABCDE")
	private String membershipNumber;

	@NotBlank(message = "O valor é obrigatório")
	@Pattern(regexp = "^\\d{1,13}(\\.\\d{1,2})?$", message = "O valor deve estar no formato correto (máximo 13 dígitos inteiros e 2 decimais, ex: 999999999999.99)")
	@ValidAmount
	@Schema(description = "Valor do lançamento. Máximo de 13 dígitos inteiros e 2 decimais.", example = "1000.50")
	private String amount;

	@NotNull(message = "O tipo de pagamento é obrigatório")
	@ValidPaymentType
	@Schema(description = ".", example = "DEBIT")
	private PaymentTypeEnum paymentType;
}
