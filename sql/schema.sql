CREATE TABLE raw_log (
	id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	timestamp DATETIME NULL DEFAULT NULL,
	ip_address VARCHAR(50) NULL DEFAULT NULL,
	request_method VARCHAR(20) NULL DEFAULT NULL,
	response_code INT(11) NULL DEFAULT NULL,
	user_agent VARCHAR(255) NULL DEFAULT NULL,
	INDEX pk_raw_log (id),
	-- this index is used for the ip address queries as the data is a larger dataset (115k rows in the sample)
	FULLTEXT INDEX idx_ip_address (ip_address)
);
