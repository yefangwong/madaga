-- -------------------------------------------------------------
-- TablePlus 3.12.4(360)
--
-- https://tableplus.com/
--
-- Database: dhf.sqlite
-- Generation Time: 2023-06-27 09:52:23.4270
-- -------------------------------------------------------------


DROP TABLE IF EXISTS "department";
CREATE TABLE "department" ("id" int,"name" varchar NOT NULL,"number" int, PRIMARY KEY (id));

INSERT INTO "department" ("id", "name", "number") VALUES
('1', '資訊部', '1369'),
('2', '財務部', '2369');
