

*system_events table:*

| field name        | Type      | Description  |
| ----------------- | --------- | ------------ |
| id                | LONG      |              |
| migration_key     | VARCHAR   |Ex. case: INITIAL_SEEDER_V1 |
| description       | VARCHAR   |              |
| applied_at        | TIMESTAMP |              |
| execution_time_ms | LONG      |time in ms to execute the event|
| status            | VARCHAR   | SUCCESS, FAILED|

