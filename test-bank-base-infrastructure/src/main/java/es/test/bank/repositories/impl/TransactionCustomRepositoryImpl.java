package es.test.bank.repositories.impl;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import es.test.bank.entity.Transaction;
import es.test.bank.repositories.TransactionCustomRepository;
import es.test.bank.repositories.TransactionsRepository;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TransactionCustomRepositoryImpl implements TransactionCustomRepository {

    private TransactionsRepository transactionsRepository;

    @Override
    public Transaction create(@NotNull Transaction transaction) {
        Assert.notNull(transaction, "Transaction is null");
        return this.transactionsRepository.save(transaction);
    }

}
