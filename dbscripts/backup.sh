#!/bin/bash
. dbsetup.properties

FILE=`date | sed "s/[ ,:]/_/g"`_backup.sql
pg_dump -U postgres -f ${FILE} $SCHEMA_NAME
mv ${FILE} /home/user/backup/


