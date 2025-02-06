package br.com.bank.api_releases.dto;

import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import java.util.Date;
import java.util.UUID;

public record ReleaseDTO(

	UUID id,
	AccountDTO account,
	String amount,
	String paymentType,
	Date updatedAt
) {
	public static ReleaseDTO with(
		UUID id,
		AccountDTO account,
		String amount,
		PaymentTypeEnum paymentType,
		Date updatedAt
	) {
		return new ReleaseDTO(
			id,
			account,
			amount,
			paymentType.getInitials(),
			updatedAt
		);
	}
}
