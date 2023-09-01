CREATE TABLE subscriptions
(
    subscriber_username VARCHAR(50) NOT NULL,
    target_username     VARCHAR(50) NOT NULL,
    viewed              BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (subscriber_username, target_username),
    FOREIGN KEY (subscriber_username) REFERENCES users (username),
    FOREIGN KEY (target_username) REFERENCES users (username)
);
CREATE INDEX subscriptions_target_user_name_index
    ON subscriptions (target_username);
CREATE INDEX subscriptions_subscriber_username
    ON subscriptions (subscriber_username);