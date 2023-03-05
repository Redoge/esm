CREATE TABLE gift_certificate (
    gift_certificate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    gift_certificate_name VARCHAR(255) NOT NULL,
    gift_certificate_description VARCHAR(255),
    gift_certificate_price DECIMAL(10, 2) NOT NULL,
    gift_certificate_duration INT NOT NULL,
    gift_certificate_create_date TIMESTAMP NOT NULL,
    gift_certificate_last_update_date TIMESTAMP NOT NULL,
    PRIMARY KEY (gift_certificate_id)
);

CREATE TABLE tag (
    tag_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    tag_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (tag_id)
);

CREATE TABLE gift_certificate_tag (
    gift_certificate_id BIGINT UNSIGNED NOT NULL,
    tag_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate(gift_certificate_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag(tag_id) ON DELETE CASCADE
);
