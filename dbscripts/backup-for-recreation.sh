#!/bin/bash
. dbsetup.properties

FILE=last_backup.sql
pg_dump -U postgres -f ${FILE} $SCHEMA_NAME
mv ${FILE} /home/user/backup/


