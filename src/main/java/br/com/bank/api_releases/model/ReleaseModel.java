package br.com.bank.api_releases.model;

import br.com.bank.api_releases.dto.ReleaseDTO;
import br.com.bank.api_releases.forms.ReleaseForm;
import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import java.io.Serial;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "releases")
@Where(clause = "deleted_at is null")
public class ReleaseModel extends BusinessObject {

	@Serial
	private static final long serialVersionUID = 3699012624511301882L;

	@ManyToOne()
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private AccountModel account;

	private Double amount;

	@Enumerated
	@Column(name = "payment_type")
	private PaymentTypeEnum paymentType;

	public ReleaseModel(
		UUID id,
		AccountModel account,
		Double amount,
		PaymentTypeEnum paymentType,
		Date createdAt,
		Date updatedAt
	) {
		super.setId(id);
		super.setCreatedAt(createdAt);
		super.setUpdatedAt(updatedAt);

		this.setAccount(account);
		this.setAmount(amount);
		this.setPaymentType(paymentType);
	}

	public static ReleaseModel fromCreate(AccountModel accountModel, ReleaseForm form) {
		Date now = new Date();

		Double value = Double.valueOf(form.getAmount());
		if (value < 0) {
			throw new IllegalArgumentException("O valor do lançamento deve ser positivo.");
		}

		return new ReleaseModel(
			null,
			accountModel,
			Double.valueOf(form.getAmount()),
			form.getPaymentType(),
			now,
			now
		);
	}

	public static ReleaseModel from(ReleaseModel releaseModel, ReleaseForm form) {
		Double value = Double.valueOf(form.getAmount());
		if (value < 0) {
			throw new IllegalArgumentException("O valor do lançamento deve ser positivo.");
		}
		releaseModel.setPaymentType(form.getPaymentType());
		releaseModel.setAmount(Double.valueOf(form.getAmount()));
		releaseModel.setUpdatedAt(new Date());
		return releaseModel;
	}

	public ReleaseDTO toAggregate() {
		return ReleaseDTO.with(
			this.getId(),
			this.getAccount().toAggregate(),
			this.getAmount(),
			this.getPaymentType(),
			this.getUpdatedAt()
		);
	}

	public static List<ReleaseDTO> toAggregateList(List<ReleaseModel> accountModelList) {
		return accountModelList.stream().map(ReleaseModel::toAggregate).collect(Collectors.toList());
	}
}
