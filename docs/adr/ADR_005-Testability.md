## **ADR 005: System testability in development environment**

**Status:** Pending

**Context:** The application uses the concepts of double-entry bookkeeping and cryptographic chaining to ensure the integrity of financial data, full auditability, and the impossibility of creating or destroying value without traceability.
However, in development and testing environments, there is no integration with external systems (e.g., financial institutions, payment gateways), which prevents the legitimate entry of funds into the system. This limitation makes it difficult to:

- Test end-to-end financial flows.
- Validate the consistency of the ledger.
- Demonstrating the application’s functionality to third parties (e.g., recruiters).

Therefore, a controlled mechanism is needed to introduce a fictitious balance without compromising the application’s architectural principles.

**Decision:** A balance provisioning mechanism will be implemented in the development environment, consisting of two strategies:

**Data Seeder (Genesis Block)**
When starting the application in the development profile, a Treasury account will be created along with an initial minting transaction (Genesis Block):
- Credit: Treasury account.
- Debit: abstract “Share Capital” account.

Test users and accounts will be created for:
- ADMIN
- Alice
- Bob

The Treasury will perform initial transfers to test accounts:

- R$ 5.000,00 para Alice
    
- R$ 5.000,00 para Bob
    
All operations follow the application’s standard workflow, ensuring ledger consistency. The credentials for the created accounts (ADMIN, Alice, and Bob) will be available for access and documented in the application’s README. A user can then use them to transfer funds to their own account and see how transaction data is persisted.

**Consequences:**
- **Positive:** 
    -  Enables comprehensive testing of financial flows
    - Preserves the integrity of the double-entry model
    - Maintains full auditability (all transactions are traceable)
    - Facilitates technical demonstrations (e.g., for recruiters)
	- Avoids the need for direct database bypass  
    - Reuses the actual application flow (high test fidelity)
- **Negative:**
    - Dependency on the development profile for correct seeder execution.
	- Risk of human error when enabling improper configurations in other environments.
    - The concept of _minting_ (Genesis Block) does not exist in the application’s real domain; it is introduced solely for testing purposes.
    - The seeder may introduce shared state, making it difficult to perform isolated and deterministic tests.