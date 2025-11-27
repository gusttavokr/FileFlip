-- Auth Service
CREATE DATABASE auth_db;
CREATE USER auth_user WITH PASSWORD 'auth_password';
GRANT ALL PRIVILEGES ON DATABASE auth_db TO auth_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO auth_user;
ALTER SCHEMA public OWNER TO auth_user;

-- Arquivo Service
CREATE DATABASE arquivo_db;
CREATE USER arquivo_user WITH PASSWORD 'arquivo_password';
GRANT ALL PRIVILEGES ON DATABASE arquivo_db TO arquivo_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO arquivo_user;
ALTER SCHEMA public OWNER TO arquivo_user;
