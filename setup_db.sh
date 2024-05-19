#!/bin/bash

# Update package list and install necessary packages
sudo apt update
sudo apt install git curl gnupg2 wget -y

# Install Postgres and configure (if needed)
sudo apt install gnupg2 wget
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/postgresql.gpg
sudo apt update
sudo apt install postgresql-16 postgresql-contrib-16 -y

# Start and enable PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Configure PostgreSQL database and user
sudo -i -u postgres psql <<EOF
CREATE DATABASE ss_demo_1;
ALTER USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ss_demo_1 TO postgres;
\q
EOF

# Modify the PostgreSQL configuration
sudo sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /etc/postgresql/16/main/postgresql.conf
sudo sed -i "s/host    all             all             127.0.0.1\/32            scram-sha-256/host    all             all             0.0.0.0\/0               scram-sha-256/g" /etc/postgresql/16/main/pg_hba.conf
sudo ufw allow 5432/tcp

# Restart PostgreSQL
sudo service postgresql restart
