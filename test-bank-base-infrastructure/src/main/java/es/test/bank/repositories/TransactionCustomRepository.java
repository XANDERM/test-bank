package es.test.bank.repositories;

import javax.validation.constraints.NotNull;

import es.test.bank.entity.Transaction;

public interface TransactionCustomRepository {

	Transaction create(@NotNull Transaction transaction);
}