select
ip_address, count(ip_address) num_requests
from
raw_log
where
timestamp >= '2017-01-01.13:00:00' and
timestamp < '2017-01-01.14:00:00'
group by ip_address
having count(ip_address) >= 100
order by num_requests desc