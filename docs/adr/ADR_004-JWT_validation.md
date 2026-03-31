## **ADR 004: Stateless Identity Validation via JWT Claims**

**Status:** Accepted

**Context:** Given the use of a Modular Monolith architecture designed for scalability and
future migration to microservices, we need permission validation to be fast
and not burden the database with every request.

**Decision:** We will use custom claims in the JWT to carry the user’s validation state.

**Consequences:**
- **Positive:** 
    - High performance and low latency;
    - Easy integration with future API gateways.
	- The token can store the user’s roles/status and can be used to ensure authorization at protected endpoints.
- **Negatives:** 
    - If a user is banned, the token remains valid until it expires (to be resolved in the future with a blocklist or short expiration times).
	- After user verification, a new token must be generated and returned with the new status included. And for the backend to respond to future requests, this new token must be sent to ensure that a user who has already been validated is not prevented from accessing endpoints.