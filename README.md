# Core Banking System

## Project Overview
This project is a Core Banking backend application developed using Spring Boot.  
It provides REST APIs for managing customers, accounts, and transactions.

## Modules
1. Customer Module
    - Create Customer
    - Update Customer
    - Get Customer
    - KYC Verification

2. Account Module
    - Create Account
    - Get Account Details
    - Update Account Status

3. Transaction Module
    - Deposit
    - Withdraw
    - Fund Transfer
    - Transaction History

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- MySQL
- REST APIs
- Maven

## Architecture
Controller → Service → Repository → Database

## API Example
POST /api/transactions/transfer

## Future Enhancements
- JWT Authentication
- API Gateway
- Microservices architecture