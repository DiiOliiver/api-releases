package br.com.bank.api_releases.model;

import br.com.bank.api_releases.dto.AccountDTO;
import br.com.bank.api_releases.forms.AccountForm;
import br.com.bank.api_releases.helper.ContaBankHelper;
import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@Where(clause = "deleted_at is null")
public class AccountModel extends BusinessObject implements UserDetails {

	@Serial
	private static final long serialVersionUID = 772420609782266544L;

	@Column(length = 100)
	private String name;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	private String password;

	@Column(length = 20, nullable = false, unique = true)
	private String cpf;

	@Column(name = "membership_number", length = 80, nullable = false, unique = true)
	private String membershipNumber;

	@Column(nullable = false)
	private BigDecimal balance = BigDecimal.ZERO;

	@Version
	private Integer version;

	public AccountModel(
		UUID id,
		String name,
		String email,
		String cpf,
		String password,
		String membershipNumber,
		Date createdAt,
		Date updatedAt
	) {
		super.setId(id);
		super.setCreatedAt(createdAt);
		super.setUpdatedAt(updatedAt);

		this.setName(name);
		this.setEmail(email);
		this.setCpf(cpf);
		this.setPassword(password);
		this.setMembershipNumber(membershipNumber);
		this.setBalance(BigDecimal.ZERO);
	}

	public static AccountModel fromCreate(AccountForm form) {
		String password = new BCryptPasswordEncoder().encode(form.getPassword());
		Date now = new Date();

		return new AccountModel(
			null,
			form.getName(),
			form.getEmail(),
			form.getCpf().replaceAll("[^0-9]", ""),
			password,
			ContaBankHelper.gerarNumeroConta(),
			now,
			now
		);
	}

	public synchronized void applyLancamento(ReleaseModel release) {
		if (release.getAmount() < 0) {
			throw new IllegalArgumentException("O valor do lançamento não pode ser negativo.");
		}
		if (release.getPaymentType() == PaymentTypeEnum.CREDIT) {
			this.balance = this.balance.add(BigDecimal.valueOf(release.getAmount()));
		} else if (release.getPaymentType() == PaymentTypeEnum.DEBIT) {
			this.balance = this.balance.subtract(BigDecimal.valueOf(release.getAmount()));
		}
	}

	public static AccountModel from(AccountModel model, AccountForm form) {
		String newPass = new BCryptPasswordEncoder().encode(form.getPassword());

		if (!model.getPassword().matches(newPass)) {
			System.out.println("Senha atualizada!");
			model.setPassword(newPass);
		}

		model.setName(form.getName());
		model.setEmail(form.getEmail());
		model.setCpf(form.getCpf().replaceAll("[^0-9]", ""));
		model.setUpdatedAt(new Date());

		return model;
	}

	public AccountDTO toAggregate() {
		return AccountDTO.with(
			this.getId(),
			this.getName(),
			this.getEmail(),
			this.getCpf(),
			this.getMembershipNumber(),
			this.getUpdatedAt()
		);
	}

	public static List<AccountDTO> toAggregateList(List<AccountModel> accountModelList) {
		return accountModelList.stream().map(AccountModel::toAggregate).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
