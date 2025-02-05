package br.com.bank.api_releases.repository;

import br.com.bank.api_releases.model.AccountModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepositoryJpa extends JpaRepository<AccountModel, UUID> {

	@Query(value = """
 		select a.* from accounts a where (
 			(:search is null or UPPER(a.name) like '%' || UPPER(:search) || '%') or
 			(:search is null or UPPER(a.email) like '%' || UPPER(:search) || '%') or
 			(:search is null or UPPER(a.cpf) like '%' || UPPER(:search) || '%') or
 			(:search is null or UPPER(a.membership_number) like '%' || UPPER(:search) || '%')
 		)
	""", nativeQuery = true)
	Page<AccountModel> findAll(
		String search,
		Pageable pageable
	);

	Optional<AccountModel> findByEmail(String email);


	Optional<AccountModel> findByName(String name);

	Optional<AccountModel> findByMembershipNumber(String membershipNumber);

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, UUID id);

	boolean existsByCpf(String cpf);

	boolean existsByCpfAndIdNot(String cpf, UUID id);

	boolean existsByMembershipNumber(String membershipNumber);
}
