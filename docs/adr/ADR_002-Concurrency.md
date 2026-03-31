## **ADR 002: Concurrency Strategy (Optimistic Locking)**

**Status:** Accepted

**Context:** The Ledger will experience high concurrency on popular accounts (e.g., the receivables account of a large retailer). We need to ensure that the balance never becomes inconsistent without sacrificing the database’s overall throughput.

**Decision:** We will use Optimistic Locking via version control (@Version in JPA). In case of a conflict, the application will catch the exception and apply a Retry strategy with Exponential Backoff + Jitter.

**Consequences:**
- **Positive:** Higher database availability (no indefinite retention of row locks) and better performance for most transactions.
- **Negative:** In scenarios with extremely high contention on the same account, the retry rate may increase the latency perceived by the end user.