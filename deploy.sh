#!/bin/bash

sbt dist

docker run --rm -ti -v $PWD/target/universal/cleaning-1.0.zip:/opt/cleaning-1.0.zip -p 8080:8080 --link=c-postgres:postgres-host cleaning:latest
