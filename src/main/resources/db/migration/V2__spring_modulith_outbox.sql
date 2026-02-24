CREATE TABLE event_publication (
    id UUID PRIMARY KEY,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    listener_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    serialized_event TEXT NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    state VARCHAR(32) NOT NULL
);

CREATE INDEX idx_event_publication_state
    ON event_publication(state);