DROP TABLE IF EXISTS PARTS;

CREATE TABLE PARTS (
                       ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                       PART_NUMBER VARCHAR(100) NOT NULL UNIQUE,
                       NAME VARCHAR(255) NOT NULL,
                       TYPE VARCHAR(50) NOT NULL,
                       MOUNT_TYPE VARCHAR(10) NOT NULL,
                       STOCK_QUANTITY INT NOT NULL,
                       PURCHASE_PRICE_EUR DECIMAL(10,3) NOT NULL
);