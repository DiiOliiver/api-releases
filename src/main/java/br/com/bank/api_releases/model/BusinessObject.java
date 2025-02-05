package br.com.bank.api_releases.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt", "deletedAt"})
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BusinessObject implements Serializable {

	@Serial
	private static final long serialVersionUID = 7403342702661301458L;

	/**
	 * Chave primaria da entidade.
	 */
	@Id
	@NotNull
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	private UUID id;

	/**
	 * Armazena a data e hora em que o registro foi criado.
	 */
	@JsonIgnore
	@CreationTimestamp
	private Date createdAt;

	/**
	 * Armazena a data e hora da ultima vez em que o registro sofreu alguma alteracao.
	 */
	@JsonIgnore
	@UpdateTimestamp
	private Date updatedAt;

	/**
	 * Indica se um registro da entidade esta excluido.
	 * Esta campo eh usado para excluir de forma logica um registro da
	 * entidade quando o mesmo nao puder ser excluido fisicamente.
	 */
	@JsonIgnore
	private Date deletedAt;
}
