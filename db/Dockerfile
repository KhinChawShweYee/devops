# Use latest MySQL server
FROM mysql/mysql-server:latest

# Set working directory
WORKDIR /tmp

# Copy SQL files for initial data
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/
COPY test_db/employees.sql /docker-entrypoint-initdb.d/

# Set root password and create root@% for remote access
ENV MYSQL_ROOT_PASSWORD=example

# Add remote access grant script (executed on container startup)
COPY remote-access.sql /docker-entrypoint-initdb.d/