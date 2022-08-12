package es.test.bank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.test.bank.entity.Transaction;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, String> {
	
	Optional<Transaction> findByReference(String reference);
	List<Transaction> findAllByAccountIbanIbanOrderByAmountDesc(String accountIban);
	List<Transaction> findAllByAccountIbanIbanOrderByAmountAsc(String accountIban);
	
}