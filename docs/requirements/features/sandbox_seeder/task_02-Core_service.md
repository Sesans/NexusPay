Implement an orchestration component (`SystemEventRegistry`) to perform verification and store the history, thereby avoiding duplication of Runners.
For now, only the `runIfPending` method will be implemented, which will be used by the sandbox seeder. 

The method described will receive a event name and a Runnable (the execute method from UseCase). This execution must be monitored; if errors occur, the systemEvents object must be saved with the FAILED status.

Tests must be implemented to verify the event registration in `runIfPending`, which will include a check using the `systemEventsRepository` method to locate the event and verify whether it already has the SUCCESS status.