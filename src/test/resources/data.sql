-- TEST USER ONE
INSERT INTO users (user_id, first_name, last_name, email, password, enabled) 
VALUES (101, 'John', 'Doe', 'john.doe@example.com', '$2a$10$doq699IlXYLvqWasu7EPwOAbU6sU6fO6QfZMhFgaMTtSdXoUHEHce', 1);

INSERT INTO retiree (user_id, date_of_birth, retirement_date, retirement_years)
VALUES (101, '1990-03-16', '2055-03-01', 30);

INSERT INTO authorities (user_id, authority) VALUES (101, 'ROLE_USER;ROLE_ADMIN');

INSERT INTO income_source (id, user_id, name, account_balance, return_rate, return_frequency)
VALUES (101, 101, 'Money Market Savings', 10000, 4.0, 12);

INSERT INTO expense (id, user_id, name, amount, yearly_frequency)
VALUES (1001, 101, 'Electricity Bill', 200, 12);

INSERT INTO goal (id, user_id, name, disposable_income, yearly_frequency, primary_goal)
VALUES (1001, 101, 'Primary Goal', 2000, 12, 1);

INSERT INTO goal (id, user_id, name, disposable_income, yearly_frequency, primary_goal)
VALUES (1002, 101, 'Secondary Goal', 3000, 1, 0);

-- TEST USER TWO
INSERT INTO users (user_id, first_name, last_name, email, password, enabled) 
VALUES (102, 'Jane', 'Doe', 'jane.doe@example.com', '$2a$10$NWrjCIsM9CqgbMjp5wGoROIZsclQOw860KD/L6MnEpxqK4ADmC9wm', 1);

INSERT INTO retiree (user_id, date_of_birth, retirement_date, retirement_years)
VALUES (102, '1992-07-04', '2057-07-01', 31);

INSERT INTO authorities (user_id, authority) VALUES (102, 'ROLE_USER');

INSERT INTO income_source (id, user_id, name, account_balance, return_rate, return_frequency)
VALUES (102, 1020, 'Jane Money Market Savings', 20000, 4.2, 12);

INSERT INTO expense (id, user_id, name, amount, yearly_frequency)
VALUES (2001, 102, 'Water Bill', 50, 12);

INSERT INTO goal (id, user_id, name, disposable_income, yearly_frequency, primary_goal)
VALUES (2001, 102, 'Primary Goal', 2500, 12, 1);

-- TEST USER THREE
INSERT INTO users (user_id, first_name, last_name, email, password, enabled) 
VALUES (103, 'Jack', 'Doe', 'jack.doe@example.com', '$2a$10$g0OlY9aj8CJnXO0kzmiOOuRflDz.HxHniksBc4KOFGPIl31zJjZie', 1);

INSERT INTO retiree (user_id, date_of_birth, retirement_date, retirement_years)
VALUES (103, '1989-05-04', '2054-05-04', 30);