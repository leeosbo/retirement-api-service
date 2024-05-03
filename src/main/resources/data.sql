INSERT INTO users (user_id, first_name, last_name, email, password, enabled) 
VALUES (1010, 'John', 'Doe', 'john.doe+test@example.com', '$2a$10$doq699IlXYLvqWasu7EPwOAbU6sU6fO6QfZMhFgaMTtSdXoUHEHce', 1);

INSERT INTO retiree (user_id, date_of_birth, retirement_date, retirement_years)
VALUES (1010, '1990-03-16', '2055-03-01', 30);

INSERT INTO authorities (user_id, authority) VALUES (1010, 'ROLE_USER');

INSERT INTO income_source (id, user_id, name, account_balance, return_rate, return_frequency)
VALUES (1010, 1010, 'Money Market Savings', 10000, 4.0, 12);

INSERT INTO income_source (id, user_id, name, account_balance, return_rate, return_frequency)
VALUES (1011, 1010, 'Certificate of Deposit', 5000, 5.0, 12);

INSERT INTO income_source (id, user_id, name, account_balance, return_rate, return_frequency)
VALUES (1012, 1010, 'IRA Retirement Account', 20000, 8.0, 12);

INSERT INTO expense (id, user_id, name, amount, yearly_frequency)
VALUES (1010, 1010, 'Electricity Bill', 200, 12);

INSERT INTO expense (id, user_id, name, amount, yearly_frequency)
VALUES (1011, 1010, 'Water Bill', 50, 12);

INSERT INTO goal (id, user_id, name, disposable_income, yearly_frequency, primary_goal)
VALUES (1010, 1010, 'Primary Goal', 2000, 12, 1);

INSERT INTO users (user_id, first_name, last_name, email, password, enabled) 
VALUES (1020, 'Jane', 'Doe', 'jane.doe+test@example.com', '$2a$10$NWrjCIsM9CqgbMjp5wGoROIZsclQOw860KD/L6MnEpxqK4ADmC9wm', 1);

INSERT INTO retiree (user_id, date_of_birth, retirement_date, retirement_years)
VALUES (1020, '1992-07-04', '2057-07-01', 31);

INSERT INTO authorities (user_id, authority) VALUES (1020, 'ROLE_USER');

INSERT INTO income_source (id, user_id, name, account_balance, return_rate, return_frequency)
VALUES (1020, 1020, 'Jane Money Market Savings', 20000, 4.2, 12);