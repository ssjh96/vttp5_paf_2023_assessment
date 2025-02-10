# vttp5_paf_2023_assessment

use mybnb;

show tables;

select * from acc_occupancy;

describe reservations;
select * from reservations;

-- Check vacancy first
SELECT vacancy FROM acc_occupancy where acc_id = "13530122";
select * from acc_occupancy where acc_id = "13530122";

-- Insert the reservation
INSERT INTO reservations (resv_id, name, email, acc_id, arrival_date, duration) 
VALUES ('1A2B3C4D', 'Sheryl', 'sheryl@email.com', 13530122, '2025-09-20', 3);

-- Update the vacancy 
UPDATE acc_occupancy SET vacancy = 100 where acc_id = 13530122;
UPDATE acc_occupancy SET vacancy = 100 where acc_id = 30479760;

delete from reservations where name ='Sheryl';

SHOW VARIABLES LIKE 'read_only';

show grants for current_user();
SHOW VARIABLES LIKE 'read_only';

SELECT @@transaction_read_only;
SET SESSION transaction_read_only = ON;

made booking to 6228208
select * from acc_occupancy where acc_id = "6228208";
select * from reservations; // reference created was 83415130