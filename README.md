This is a Spring-Boot based application designed to aggregate and sort financial transactions from multiple services.This service provides an efficient mechanism to fetch and deliver a list of transactions for a specific account.

Features:

--Transaction Aggregation: Fetches transactions from multiple microservices.

--Asynchronous Requests: Uses CompletableFuture to perform parallel calls, optimizing response times.

--Caching: Utilizes Spring's caching mechanism to store transaction data and reduce redundant calls.

--Sorting: Sorts transactions by timestamp in descending order.

--Error Handling:handles scenarios where data might be unavailable from one or more sources.
