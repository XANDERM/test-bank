# !binbash

psql -U dev -tc "SELECT 1 FROM pg_database WHERE datname = 'kong-migration'" | grep -q 1 || psql -U dev -c "CREATE DATABASE kong_db"

psql -U dev -c "CREATE USER kong WITH PASSWORD 'admin'"