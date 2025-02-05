package br.com.bank.api_releases.dto;

import java.util.Date;
import java.util.UUID;

public record AccountDTO(
	UUID id,
	String name,
	String email,
	String cpf,
	String membershipNumber,
	Date updatedAt
) {
	public static AccountDTO with(
		UUID id,
		String name,
		String email,
		String cpf,
		String membershipNumber,
		Date updatedAt
	) {
		return new AccountDTO(
			id,
			name,
			email,
			cpf,
			membershipNumber,
			updatedAt
		);
	}
}
