#!/bin/bash
mvn install:install-file -DgroupId=org.postgresql -DartifactId=postgresql -Dversion=9.3-1100 -Dpackaging=jar -Dfile=postgresql-9.3-1100.jdbc4.jar -DgeneratePom=true