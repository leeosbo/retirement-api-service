CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (user_id)
);

-- https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/jdbc.html#servlet-authentication-jdbc-schema
CREATE TABLE authorities (
    user_id BIGINT,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, authority)
);

CREATE TABLE retiree (
    user_id BIGINT,
    date_of_birth DATE NOT NULL,
    retirement_date DATE NOT NULL,
    retirement_years INT NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE spouse (
    user_id BIGINT,
    spouse_id BIGINT,
    PRIMARY KEY (user_id, spouse_id),
    FOREIGN KEY (user_id) REFERENCES retiree (user_id) ON DELETE CASCADE,
    FOREIGN KEY (spouse_id) REFERENCES retiree (user_id) ON DELETE CASCADE
);

CREATE TABLE income_source (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    account_balance INT DEFAULT 0,
    return_rate DECIMAL(10,2) DEFAULT 0,
    return_frequency INT DEFAULT 0,
    secondary_owner BIGINT DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES retiree (user_id),
    FOREIGN KEY (secondary_owner) REFERENCES retiree (user_id)
);

CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    amount INT DEFAULT 0,
    yearly_frequency INT DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES retiree (user_id)
);

CREATE TABLE goal (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    disposable_income INT NOT NULL,
    yearly_frequency INT DEFAULT 12,
    primary_goal BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES retiree (user_id)
);