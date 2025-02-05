package br.com.bank.api_releases.model.enums;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Arrays;

public enum PaymentTypeEnum {
	CREDIT(0, "Credit"),
	DEBIT(1, "Debit");

	private int value;

	private String initials;

	private static ImmutableMap<Integer, PaymentTypeEnum> reverseLookupValue =
		Maps.uniqueIndex(Arrays.asList(PaymentTypeEnum.values()), PaymentTypeEnum::getValue);

	private static ImmutableMap<String, PaymentTypeEnum> reverseLookupInitials =
		Maps.uniqueIndex(Arrays.asList(PaymentTypeEnum.values()), PaymentTypeEnum::getInitials);

	private PaymentTypeEnum(int value, String initials) {
		this.value = value;
		this.initials = initials;
	}

	public int getValue() {
		return value;
	}

	public String getInitials() {
		return initials;
	}

	public static PaymentTypeEnum fromValue(final int value) {
		return reverseLookupValue.getOrDefault(value, null);
	}

	public static PaymentTypeEnum existFromInitials(final String initials) {
		return reverseLookupInitials.get(initials);
	}
}