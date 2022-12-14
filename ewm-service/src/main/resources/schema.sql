DROP TABLE IF EXISTS
    users,
    categories,
    locations,
    events,
    participation_request,
    compilations,
    compilations_events,
    comments
    CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(500)                            NOT NULL,
    organizer_id       BIGINT,
    category_id        BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(3000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    location_id        BIGINT,
    paid               BOOLEAN,
    participant_limit  BIGINT                                  NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(50)                             NOT NULL,
    title              VARCHAR(1000)                           NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_on_user FOREIGN KEY (organizer_id) REFERENCES users (id),
    CONSTRAINT fk_event_on_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_event_on_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS participation_request
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT,
    requester_id BIGINT,
    status       VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_on_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_request_on_user FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(100) UNIQUE                     NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT,
    event_id       BIGINT,
    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilations_events_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT fk_compilations_events_event FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text           VARCHAR(5000)                           NOT NULL,
    event_id       BIGINT,
    commentator_id BIGINT,
    comment_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    by_admin       BOOLEAN                                 NOT NULL,
    pinned         BOOLEAN                                 NOT NULL,
    changed        BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comments_users FOREIGN KEY (commentator_id) REFERENCES users (id)
    );