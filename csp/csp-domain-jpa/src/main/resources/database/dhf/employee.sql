-- -------------------------------------------------------------
-- TablePlus 3.12.4(360)
--
-- https://tableplus.com/
--
-- Database: dhf.sqlite
-- Generation Time: 2023-06-27 09:56:46.5400
-- -------------------------------------------------------------


DROP TABLE IF EXISTS "employee";
CREATE TABLE "employee" ("id" int NOT NULL,"age" int,"gender" varchar,"name" varchar NOT NULL,"number" int,"dep_id" int NOT NULL, PRIMARY KEY (id));

INSERT INTO "employee" ("id", "age", "gender", "name", "number", "dep_id") VALUES
('1', '18', '男', '翁Ｘ芳', '10001', '1'),
('2', '18', '女', '吳Ｘ瑄', '10002', '2');
