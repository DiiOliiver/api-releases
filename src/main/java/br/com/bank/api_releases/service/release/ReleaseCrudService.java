package br.com.bank.api_releases.service.release;

import br.com.bank.api_releases.dto.ReleaseDTO;
import br.com.bank.api_releases.exceptions.AccountException;
import br.com.bank.api_releases.forms.DeleteListForm;
import br.com.bank.api_releases.forms.ReleaseForm;
import br.com.bank.api_releases.model.AccountModel;
import br.com.bank.api_releases.model.ReleaseModel;
import br.com.bank.api_releases.model.enums.PaymentTypeEnum;
import br.com.bank.api_releases.repository.AccountRepositoryJpa;
import br.com.bank.api_releases.repository.ReleasesRepositoryJpa;
import br.com.bank.api_releases.service.kafka.KafkaProducerService;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReleaseCrudService {

	private final ReleasesRepositoryJpa releasesRepositoryJpa;
	private final AccountRepositoryJpa accountRepositoryJpa;
	private final KafkaProducerService kafkaProducerService;

	public ReleaseCrudService(
		ReleasesRepositoryJpa releasesRepositoryJpa,
		AccountRepositoryJpa accountRepositoryJpa,
		KafkaProducerService kafkaProducerService
	) {
		this.releasesRepositoryJpa = releasesRepositoryJpa;
		this.accountRepositoryJpa = accountRepositoryJpa;
		this.kafkaProducerService = kafkaProducerService;
	}

	public List<ReleaseDTO> findAll() {
		List<ReleaseModel> accountModelList = this.releasesRepositoryJpa.findAll();
		return ReleaseModel.toAggregateList(accountModelList);
	}

	public ReleaseDTO findById(String id) throws Exception {
		Optional<ReleaseModel> releaseModel = this.releasesRepositoryJpa.findById(UUID.fromString(id));
		if (releaseModel.isPresent()) {
			return releaseModel.map(ReleaseModel::toAggregate).get();
		}
		throw new RuntimeException("Lançamento não encontrado!");
	}

	public ReleaseDTO create(ReleaseForm form) throws Exception {
		AccountModel accountModel = this.accountRepositoryJpa
			.findByMembershipNumber(form.getMembershipNumber())
			.orElseThrow(() -> new RuntimeException("Conta não encontrada!"));

		if (
			form.getPaymentType().equals(PaymentTypeEnum.DEBIT) &&
			accountModel.getBalance().doubleValue() < Double.valueOf(form.getAmount())
		) {
			throw new AccountException("Conta " + accountModel.getMembershipNumber() + ": Não possui saldo suficiente!");
		}

		ReleaseModel releaseModel = ReleaseModel.fromCreate(accountModel, form);
		accountModel.applyLancamento(releaseModel);
		this.releasesRepositoryJpa.save(releaseModel);
		this.accountRepositoryJpa.save(accountModel);

		this.kafkaProducerService.sendMessage(
			"Novo lançamento na conta " + accountModel.getMembershipNumber() + " com valor " + releaseModel.getAmount()
		);

		return releaseModel.toAggregate();
	}

	public void delete(String id) throws Exception {
		ReleaseModel releaseModel = this.releasesRepositoryJpa.findById(UUID.fromString(id))
			.orElseThrow(() -> new RuntimeException("Lançamento não encontrado!"));
		releaseModel.setDeletedAt(new Date());
		this.releasesRepositoryJpa.save(releaseModel);
	}

	public void deleteByIdIn(DeleteListForm deleteListForm) throws Exception {
		List<ReleaseModel> releaseModelList = this.releasesRepositoryJpa.findAllById(deleteListForm.getIds());
		releaseModelList.forEach(releaseModel -> releaseModel.setDeletedAt(new Date()));
		this.releasesRepositoryJpa.saveAll(releaseModelList);
	}
}
