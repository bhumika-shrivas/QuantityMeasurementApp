CREATE TABLE IF NOT EXISTS quantity_measurements (

    id INT AUTO_INCREMENT PRIMARY KEY,

    operand1 VARCHAR(255),

    operand2 VARCHAR(255),

    operationType VARCHAR(100),

    result VARCHAR(255),

    errorMessage VARCHAR(255),

    timestamp TIMESTAMP
);