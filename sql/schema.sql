create database if not exists log;
use log;

-- This is a table that loads the raw log file into the DB.  I chose this approach as it is the easiest way to read
-- and query the log file in the required ways. The disadvantage is that it can take a bit to load (30-60 seconds
-- on my machine with the sample log file).  The load time is worth the savings though.
create table if not exists raw_log (
	id bigint(20) unsigned not null auto_increment,
	timestamp datetime null default null,
	ip_address varchar(50) null default null,
	request_method varchar(20) null default null,
	response_code int(11) null default null,
	user_agent varchar(255) null default null,
	-- standard primary key, probably overkill for such a simple solution, but good practice.
	index pk_raw_log (id),
	-- this index is used for the ip address queries as the data is a larger dataset (115k rows in the sample)
	fulltext index idx_ip_address (ip_address)
);

-- This is the table that meets the "log why the ip is blocked" requirement.  Note the useful name :)
create table  blocked_ip_log  (
  	 id bigint(20) not null auto_increment,
  	 ip_address varchar(20) null default null,
  	 block_reason varchar(255) null default null,
  	 loaded_at datetime not null default current_timestamp,
  	 index pk_blocked_ip_log(id)
 );