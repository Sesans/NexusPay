**ADR 008:** Idempotency mechanism and Seeder execution

**Status:** Pending

**Context:** The ApplicationRunner runs at every Spring Boot initialization. We must ensure that entities and transfers already recorded in the database are not recreated.

***Considered Options:*** 
- **State verification:** A query search in the database for accounts with some aliases or flags are already created.
- **Control table (migrations):** Create the table *system_events*. This table will be managed by the application itself.

**Decision:** Create the dedicated table for system events and jobs. This table will be used to record some data, such as: When it was run, event status, execution time, event description and a unique event key. The ApplicationRunner will then verify this table, searching for the seeder event and his status.

**Consequences:** 
- **Positive:** 
	- Strict idempotency. If the seeder fail, the status will be recorded with FAILED, allowing it to be re-executed.
	- Audit of scheduled events and their respective execution statuses.
	- Context isolation. Business tables won't be cluttered with aliases or flags.
	- Future scaling. This table will be the base of every scheduled job.
- **Negative:** 
	- Risk of Dual-Write problem. Need a careful management with the transactional scope to avoid running the transfers but don't recording it.
	- More boilerplate code.  The new table needs to be created, managed and mapped to JPA entities.