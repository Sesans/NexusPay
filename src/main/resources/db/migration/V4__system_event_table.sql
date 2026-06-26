CREATE TABLE system_events (
    id BIGINT PRIMARY KEY,
    migration_key VARCHAR(24) NOT NULL,
    description VARCHAR(64),
    applied_at TIMESTAMP WITH TIME ZONE NOT NULL,
    execution_time_ms BIGINT NOT NULL,
    status VARCHAR(10)
);

CREATE INDEX idx_event_migration_status ON system_events (migration_key, status);