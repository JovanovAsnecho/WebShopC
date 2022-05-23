package com.example.webshop.Service.IMPL;


import com.example.webshop.Model.Transaction;
import com.example.webshop.Persistance.TransactionRepository;
import com.example.webshop.Service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }
}
