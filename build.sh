#!/bin/bash

docker pull postgres:12

docker build -t cleaning ./deploy/

docker run --name c-postgres -p 5432:5432 -e 'POSTGRES_PASSWORD=123' -e 'POSTGRES_USER=prince' -d postgres

sleep 2s

docker exec -it c-postgres psql -U prince -c "create database cleaning"

cat ./conf/db/db.sql | docker exec -i c-postgres psql -U prince -d cleaning

source deploy.sh