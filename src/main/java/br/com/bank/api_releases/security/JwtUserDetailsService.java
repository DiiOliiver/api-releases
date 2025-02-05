package br.com.bank.api_releases.security;

import br.com.bank.api_releases.model.AccountModel;
import br.com.bank.api_releases.repository.AccountRepositoryJpa;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final AccountRepositoryJpa accountRepository;

	public JwtUserDetailsService(AccountRepositoryJpa accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AccountModel user = accountRepository.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new User(user.getEmail(), user.getPassword(), List.of());
	}
}
