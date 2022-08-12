package es.test.bank.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "ACCOUNT")
@SQLDelete(sql = "UPDATE ACCOUNT " + "SET deleted = true " + "WHERE id = ?")
@Where(clause = "deleted = false")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Account extends Auditable {

	@Id
	@Column(name = "iban", nullable = false, updatable = false, unique = true, columnDefinition = "VARCHAR(255)")
	@NonNull
	protected String iban;

	@Column(name = "amount", nullable = false, precision = 10, scale = 2)
	@NonNull
	private Double amount;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountIban")
	@EqualsAndHashCode.Exclude
	private Set<Transaction> transactions;

}
