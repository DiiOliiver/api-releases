package br.com.bank.api_releases.helper;

import java.util.Random;

public class ContaBankHelper {

	private static final Random RANDOM = new Random();

	/**
	 * Gera um número de conta bancária no formato XXXXXX-D.
	 * @return Número de conta gerado.
	 */
	public static String gerarNumeroConta() {
		int numeroConta = 100000 + RANDOM.nextInt(900000); // Gera um número de 6 dígitos
		int digitoVerificador = calcularDigitoVerificador(numeroConta);
		return String.format("%06d-%d", numeroConta, digitoVerificador);
	}

	/**
	 * Calcula o dígito verificador usando o módulo 11.
	 * @param numeroConta Número base da conta.
	 * @return Dígito verificador calculado.
	 */
	private static int calcularDigitoVerificador(int numeroConta) {
		String numeroStr = String.valueOf(numeroConta);
		int soma = 0;
		int peso = 2;

		// Percorre os dígitos de trás para frente
		for (int i = numeroStr.length() - 1; i >= 0; i--) {
			soma += (numeroStr.charAt(i) - '0') * peso;
			peso = (peso == 9) ? 2 : peso + 1; // Reseta o peso para 2 quando atinge 9
		}

		int resto = soma % 11;
		int digito = 11 - resto;

		// Regra do módulo 11: Se o dígito for 10 ou 11, vira 0
		return (digito == 10 || digito == 11) ? 0 : digito;
	}
}