package com.example.transactionaggregator.controller;

import com.example.transactionaggregator.model.Transaction;
import com.example.transactionaggregator.service.AggregatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class AggregatorController {
    private final AggregatorService aggregatorService;

    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @GetMapping("/aggregate")
    public ResponseEntity<List<Transaction>> getAggregator(@RequestParam(name = "account") String account) throws ExecutionException, InterruptedException {
        List<Transaction> transactions = aggregatorService.getTransactions(account);
        if (transactions == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
