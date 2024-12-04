package com.example.transactionaggregator.service;

import com.example.transactionaggregator.model.Transaction;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AggregatorService {
    private final RequestService requestUtil;

    public AggregatorService(RequestService requestUtil) {
        this.requestUtil = requestUtil;
    }

    @Cacheable(value = "transactions", key = "#account")
    public List<Transaction> getTransactions(String account) throws ExecutionException, InterruptedException {
        String uri1 = "http://localhost:8888/transactions?account=" + account;
        String uri2 = "http://localhost:8889/transactions?account=" + account;

        CompletableFuture<List<Transaction>> future1 = requestUtil.retryRequest(uri1);
        CompletableFuture<List<Transaction>> future2 = requestUtil.retryRequest(uri2);

        CompletableFuture.allOf(future1, future2).join();

        List<Transaction> transactionsFromServer1 = future1.get();
        List<Transaction> transactionsFromServer2 = future2.get();

        if (transactionsFromServer1 == null && transactionsFromServer2 == null) {
            return null;
        }

        ArrayList<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(transactionsFromServer1);
        allTransactions.addAll(transactionsFromServer2);

        allTransactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));

        return allTransactions;
    }
}
