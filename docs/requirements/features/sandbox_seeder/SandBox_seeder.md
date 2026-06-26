The main purpose of this feature is to automatically create a real-world use case scenario by generating the sandbox “seeder” that will inject monetary value into the system. This value can be used to add funds to other accounts that may be created by test users. 

## *Functional requirements:*
Create Sandbox, treasure and two demo accounts (named Alice and Bob);
Initial allocation:
	Sandbox -> Treasure

Initial balance allocation for demo accounts:
	Treasure -> Alice 
	Treasure -> Bob 

Transfer simulation Client to Client:
	Alice -> Bob

A Spring Boot ApplicationRunner will be used to trigger the sandbox using a profile (sandbox)


## *Non-functional requirements*:
**Idempotency**: The Runner cannot duplicate the account creation or transaction simulations if it is already done.
**Atomicity**: Each transfer must be an isolated and secure database transaction. An error in the simulation (Alice -> Bob) should not interfere with previous transfers.