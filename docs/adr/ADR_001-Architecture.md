## **ADR 001: Architecture Strategy (Modular Monolith)**

**Status:** Accepted

**Context:** The operational complexity of microservices (network latency, distributed tracing, partial failures) is disproportionate to NexusPay’s current stage. We need delivery speed and strong consistency (ACID), while maintaining the possibility of future scaling.

**Decision:** We will adopt the Modular Monolith pattern using Spring Modulith. The system will be a single deployment, but with physical separation of packages by context (Auth, Ledger, Notification). Communications between modules must be done via domain interfaces or internal events.

**Consequences:**
- **Positive:** Ease of refactoring, database-level transactions (guaranteed atomicity), and a simplified testing environment.
- **Negative:** Single vertical scaling (we cannot scale only the Ledger without scaling the Auth), requiring rigorous monitoring to avoid undue coupling between packages.