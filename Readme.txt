API URL for different Services


API Gateway(For all the services)

1. create-customer - > http://localhost:8084/customer

	pass the following details in post method
	{
    		"name" : "test",
    		"surname" :"test"
	}

2. Get Customers -> http://localhost:8084/customer

3. Get single customer -> http://localhost:8084/customer/ff81836b-3d75-4247-9718-70a54072ba6a

4. Create Account -> http://localhost:8084/account

	pass the following details in post method

	{
    		"balance" : 100,
    		"customerId" : 1
	}

5. Get Accounts -> http://localhost:8084/account

6. Get Single Account -> http://localhost:8084/account/{accountId}

7. Delete Customer -> http://localhost:8084/customer/ff81836b-3d75-4247-9718-70a54072ba6a



CUSTOMER-SERVICE is running on port 8082
ACCOUNT-SERVICE is running on port 8083
API-GATEWAY is running on port 8084
SERVICE-CONFIG is running on port 8085
SERVICE-Registry(EUREKA) is running on port 8761
NOTIFICATION-SERVICE
GREYLOG-SERVICE


CREATE TABLE account_table (
    customer_id INT PRIMARY KEY,
    balance DECIMAL(10, 2) DEFAULT 100.00,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- top-up, purchase, refund, etc.
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    gsm_number VARCHAR(15) NOT NULL
);
