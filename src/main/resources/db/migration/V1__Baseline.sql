CREATE TABLE IF NOT EXISTS flyway_schema_history
(
    installed_rank INT           NOT NULL,
    version        VARCHAR(50),
    description    VARCHAR(200)  NOT NULL,
    type           VARCHAR(20)   NOT NULL,
    script         VARCHAR(1000) NOT NULL,
    checksum       INT,
    installed_by   VARCHAR(100)  NOT NULL,
    installed_on   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    execution_time INT           NOT NULL,
    success        BOOLEAN       NOT NULL
);

INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by,
                                   installed_on, execution_time, success)
VALUES (1, '1', 'Baseline', 'BASELINE', 'V1__Baseline.sql', NULL, 'admin', CURRENT_TIMESTAMP, 0, true);
