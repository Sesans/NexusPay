## Requirements Document: NexusPay Ledger Core

**Overview and Technical Definitions**
- **Core Ledger:** An immutable record-keeping system using the Append-Only design pattern.

- **Balance Definition:** An account’s balance is conceptually defined as the difference
between the sum of credits and the sum of debits recorded in the transaction ledger
at a given point in time (t). In the adopted model, the ledger is treated as an
immutable, append-only structure, while the balance is persisted as a mutable field in the
accounts table, functioning as a snapshot of the account’s financial state.

- **Accounting Model:** Strict implementation of double-entry bookkeeping. Every
transaction must have a corresponding entry, ensuring that the sum of the transaction entries is
zero.

**Functional Requirements:**
- **RF01 –** Wallet Onboarding: User registration with validation of CPF, email (OTP),
age (18 < x < 130), full name, and password.
- **RF02 –** Multi-Currency: Support for multiple currencies (BRL, USD, EUR) segregated by sub-
accounts.
- **RF03 –** Atomic Transactions: Transfers between wallets must be processed as
an atomic unit of work.
- **RF04 –** Overdraft Prevention: The system must reject debit transactions if the
resulting balance of the source account is less than zero.
- **RF05 –** Idempotence: Ensure that the same transaction request, if sent twice,
does not result in a duplicate debit.
- **RF06 –** Data Security: Passwords must be stored in the database with
secure encryption and a salt to ensure password uniqueness and protection against
rainbow tables. Login passwords must be alphanumeric, 8 characters long, containing both uppercase and
lowercase letters, and at least one special character, while transaction PINs must consist
of 6 numeric digits.

**Non-Functional Requirements:**
- **NFR01 –** Strong Consistency: Read operations following a write must immediately reflect the
latest state. Use of transaction isolation in the database to prevent
lost updates.
- **NFR02 –** Concurrency: Implementation of Optimistic Locking + idempotence techniques
with retries, to maximize concurrency, ensure lower latencies, and high security in
transactions.
- **NFR03 –** Latency: p99 less than 200 ms for transfer operations.
- **RNF04 –** Immutability and Integrity: Transaction history protected by
Cryptographic Chaining. Each transaction record must contain a hash with the data from the
current transaction + the hash of the previous transaction, making retroactive changes detectable.
- **RNF05 –** Availability: 99.9%, with a Blue-Green deployment strategy to prevent
downtime.
- **RNF06 –** Asynchronous Reconciliation: A job that periodically (once a day) sums
the ledger entries and compares them with the balance (current snapshot) in the accounts table. If there is a
discrepancy, the system generates a critical alert.
- **RNF07 –** Auditing and Monitoring: Real-time monitoring of system
integrity and detailed logs.