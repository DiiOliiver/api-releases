package br.com.bank.api_releases.repository;

import br.com.bank.api_releases.model.ReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReleasesRepositoryJpa extends JpaRepository<ReleaseModel, UUID> {
}
