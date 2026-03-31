 **ADR 007:** Using Messaging for Asynchronous Processing

**Status:** Pending

**Context:** Asynchronous message processing, such as sending emails, is handled by Spring’s Event Publisher. This approach works well for simple monoliths, but with growing processing demands and the publisher’s drawbacks—such as in-memory messages that can be lost if the service goes offline, and limited or no individual scalability since it runs in the same container as the application core—it creates a weak link in the system.

**Decision:** Use AWS SQS as an external messaging service. This service will be responsible for storing messages in the queue to be consumed by specific listeners, such as the notifications module.

**Consequences:**
- **Positive:** 
    - Full compatibility with the provider.
    - Messages are persisted in the queue.
	- Easy scalability since it is an external service.
    - Reduced processing cost in the main module, removing the burden of processing events and sending emails alongside Ledger Core transactions.
- **Negatives:** 
    - Higher cost than using Spring’s internal events.
	- More complex configuration, requiring careful management.
    - Messages may be processed more than once, requiring idempotency on the consumer side.
    - Close monitoring to prevent costs from escalating unexpectedly.