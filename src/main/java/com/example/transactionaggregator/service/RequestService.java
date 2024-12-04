package com.example.transactionaggregator.service;

import com.example.transactionaggregator.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RequestService {
    @Async
    public CompletableFuture<List<Transaction>> retryRequest(String uri) {
        RestTemplate restTemplate = new RestTemplate();
        int attempts = 0;
        while (attempts < 5) {
            try {
                ResponseEntity<Transaction[]> response = restTemplate.getForEntity(uri, Transaction[].class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    return CompletableFuture.completedFuture(Arrays.asList(response.getBody()));
                }
            } catch (Exception e) {
                attempts++;
            }
        }
        return null;
    }

}
