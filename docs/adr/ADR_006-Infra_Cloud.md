**ADR 006:** Deploying the application to a cloud provider

**Status:** Pending

**Context:** For the application to be properly made available on the internet, we need a compatible and reliable provider that is affordable, meets our cost-benefit requirements, and can correctly host and handle the application and its requests. 

**Decision:** We will use the AWS Cloud service. AWS is a suitable provider for our needs because, in addition to being one of the largest cloud services with extensive documentation available, it offers a range of services that can benefit us, such as RDS, SQS, Lambda, and others. To ensure there are no discrepancies between the application running in the local test environment and the cloud production environment, **Docker** containers will be used to ensure consistency, with **Docker Compose** orchestrating these containers. To mitigate costs in the local development environment, the **LocalStack** cloud services emulator will be used. This approach reduces costs, allows for faster identification of errors or necessary fixes, and enables the creation of more practical CI/CD pipelines.

**Consequences:**
- **Positives:**
    - Pay-as-you-go pricing model.
    - Well-established infrastructure in the market with comprehensive documentation.
    - CI/CD pipelines ensure that software is delivered functioning as expected.
    - The system is accessible to anyone with internet access.
	- Ease of scalability and elasticity.
- **Negative:**
    - Complex configuration requiring careful analysis and definition to avoid rework and unnecessary costs (already reduced by using LocalStack).
	- Poor configuration, such as access permissions, can create security risks.
    - Instances must be closely monitored, as the Pay-Per-Use model can scale unexpectedly.
    - Migration is difficult and costly due to the incompatibility of tools and services with other providers.