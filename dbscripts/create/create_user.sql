DROP USER IF EXISTS "%USER_NAME%";
CREATE ROLE "%USER_NAME%" LOGIN ENCRYPTED PASSWORD 'password' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
