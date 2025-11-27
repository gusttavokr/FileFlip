-- Auth Service
CREATE USER auth_user WITH PASSWORD 'auth_password';
CREATE DATABASE auth_db OWNER auth_user;

\connect auth_db;

GRANT ALL PRIVILEGES ON SCHEMA public TO auth_user;
ALTER SCHEMA public OWNER TO auth_user;

-- Arquivo Service
CREATE USER arquivo_user WITH PASSWORD 'arquivo_password';
CREATE DATABASE arquivo_db OWNER arquivo_user;

\connect arquivo_db;

GRANT ALL PRIVILEGES ON SCHEMA public TO arquivo_user;
ALTER SCHEMA public OWNER TO arquivo_user;
