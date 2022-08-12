package es.test.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.test.bank.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
	
}