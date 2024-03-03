CREATE TABLE device_type (
    id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE device (
    id INT GENERATED ALWAYS AS IDENTITY,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    booked_date_time TIMESTAMP,
    released_date_time TIMESTAMP,
    device_type INT NOT NULL,
    booked_by VARCHAR(255),
    PRIMARY KEY(id),
    CONSTRAINT fk_device_type
        FOREIGN KEY(device_type)
            REFERENCES device_type(id)
);