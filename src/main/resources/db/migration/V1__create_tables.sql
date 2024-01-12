CREATE TABLE IF NOT EXISTS user (
								id 			INT BIGSERIAL PRIMARY KEY NOT NULL,
								user_name VARCHAR(45) CHECK (LENGTH(user_name) >=2) NOT NULL,
								email VARCHAR(45) NOT NULL,
								user_password VARCHAR(50) NOT NULL
								);
							
CREATE TABLE IF NOT EXISTS link (
                                id              INT BIGSERIAL PRIMARY KEY NOT NULL,
                                user_id         INT                       NOT NULL,
                                short_link      VARCHAR(8)                NOT NULL,
                                long_link       VARCHAR(500)              NOT NULL,
                                data_create     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                active_status   TINYINT   DEFAULT true,
                                count_of_clicks INT       DEFAULT 0,
                                FOREIGN KEY (user_id) REFERENCES user(id)
                                );
