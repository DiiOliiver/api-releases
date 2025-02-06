package br.com.bank.api_releases.repository;

import br.com.bank.api_releases.model.ReleaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReleasesRepositoryJpa extends JpaRepository<ReleaseModel, UUID> {
}
