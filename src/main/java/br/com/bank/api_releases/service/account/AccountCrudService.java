package br.com.bank.api_releases.service.account;

import br.com.bank.api_releases.dto.AccountDTO;
import br.com.bank.api_releases.forms.DeleteListForm;
import br.com.bank.api_releases.model.AccountModel;
import br.com.bank.api_releases.repository.AccountRepositoryJpa;
import br.com.bank.api_releases.forms.AccountForm;
import br.com.bank.api_releases.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountCrudService implements CrudService<AccountDTO, AccountForm> {

	@Autowired
	private AccountRepositoryJpa accountRepositoryJpa;

	@Override
	public List<AccountDTO> findAll() {
		List<AccountModel> accountModelList = this.accountRepositoryJpa.findAll();
		return AccountModel.toAggregateList(accountModelList);
	}

	@Override
	public Page<AccountDTO> findPageable(String search, Pageable pageable) throws Exception {
		Page<AccountModel> accountModelPage = this.accountRepositoryJpa.findAll(search, pageable);
		List<AccountDTO> accountList = AccountModel.toAggregateList(accountModelPage.getContent());
		return new PageImpl<>(
			accountList,
			pageable,
			accountModelPage.getTotalElements()
		);
	}

	@Override
	public AccountDTO findById(String id) throws Exception {
		Optional<AccountModel> accountModel = accountRepositoryJpa.findById(UUID.fromString(id));
		return accountModel.map(AccountModel::toAggregate).get();
	}

	@Override
	public AccountDTO create(AccountForm form) throws Exception {
		AccountModel accountModel = AccountModel.fromCreate(form);
		this.accountRepositoryJpa.save(accountModel);
		return accountModel.toAggregate();
	}

	@Override
	public AccountDTO update(String id, AccountForm form) throws Exception {
		AccountModel accountModel = accountRepositoryJpa.findById(UUID.fromString(id))
			.orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
		AccountModel accountModelUpdate = AccountModel.from(accountModel, form);
		this.accountRepositoryJpa.save(accountModelUpdate);
		return accountModel.toAggregate();
	}

	@Override
	public void delete(String id) throws Exception {
		AccountModel accountModel = accountRepositoryJpa.findById(UUID.fromString(id))
			.orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
		accountModel.setDeletedAt(new Date());
		this.accountRepositoryJpa.save(accountModel);
	}

	@Override
	public void deleteByIdIn(DeleteListForm deleteListForm) throws Exception {
		List<AccountModel> accountModelList = accountRepositoryJpa.findAllById(deleteListForm.getIds());
		accountModelList.forEach(accountModel -> accountModel.setDeletedAt(new Date()));
		this.accountRepositoryJpa.saveAll(accountModelList);
	}
}
