#!/bin/bash
. dbsetup.properties

VERSION_TABLE="DB_SCHEMA_VERSION"

CREATE_SCRIPTS_DIR="create"
UPDATE_SCRIPTS_DIR="update"
TEMP_DIR="temp"
BEFORE_CREATION_SQL="create_user.sql"

#
# $1 - script file name
# $2 - schema name
#
function runAsSys {
    echo "Running script $1 as SYS for schema $2"
    echo "$USER@$HOST/$2 @$1"
    psql -v ON_ERROR_STOP= -h $HOST -p $PORT -U $USER -f $1 $2|| exit 1
}

function restoreFromBackup {
    echo "Restoring $SCHEMA_NAME from backup"
    psql -v ON_ERROR_STOP= -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -f ../backup/last_backup.sql || exit 1
}

function createDbTables {
    echo "Creating $SCHEMA_NAME tables"
    psql -v ON_ERROR_STOP= -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -f ./schema/schema.sql || exit 1
    for tbl in `psql -qAt -c "select tablename from pg_tables where schemaname = 'public';" -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER` ; do  psql -c "alter table $tbl owner to ${USER_NAME}" -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER || exit 1; done
    for tbl in `psql -qAt -c "select sequence_name from information_schema.sequences where sequence_schema = 'public';" -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER` ; do  psql -c "alter table $tbl owner to ${USER_NAME}" -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER || exit 1; done
    for tbl in `psql -qAt -c "select table_name from information_schema.views where table_schema = 'public';" -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER` ; do  psql -c "alter table $tbl owner to ${USER_NAME}" -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER || exit 1; done
    psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "REVOKE ALL ON SCHEMA public FROM PUBLIC;" || exit 1;
    psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "REVOKE ALL ON SCHEMA public FROM postgres;" || exit 1;
    psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "GRANT ALL ON SCHEMA public TO postgres;" || exit 1;
    psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "GRANT ALL ON SCHEMA public TO PUBLIC;" || exit 1;
}

function createFromScratch {
    echo "Dropping and recreating DB $SCHEMA_NAME"
        echo "Checking if databse $SCHEMA_NAME exists..."
        RES=$(psql -h $HOST -p $PORT -U $USER -c "select count(*) from pg_catalog.pg_database where datname = '$SCHEMA_NAME';")
    RES=$(echo "$RES" | tr -d "[:cntrl:][:blank:]")
        RES=$(expr match $RES '\([a-z-]*[0-9]*\)' | tr -d "[a-z-]" )
        echo "Found $RES databases"
    if [ $RES == 1 ]; then
          echo "Dropping all connections $SCHEMA_NAME"
          psql -h $HOST -p $PORT -U $USER -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='$SCHEMA_NAME' AND pid <> pg_backend_pid();" > /dev/null || exit 1
          echo "Dropping database $SCHEMA_NAME or exiting"
          echo "psql -h $HOST -p $PORT -U $USER -c \"DROP DATABASE \"$SCHEMA_NAME\";\""
          psql -h $HOST -p $PORT -U $USER -c "DROP DATABASE \"$SCHEMA_NAME\";" || exit 1
    fi
        echo "Creating database $SCHEMA_NAME or exiting"
        psql -h $HOST -p $PORT -U $USER -c "CREATE DATABASE "$SCHEMA_NAME" WITH OWNER = $USER ENCODING='UTF8' TABLESPACE=pg_default CONNECTION LIMIT = -1;"
    echo "Done creating database"

    echo "Creating database user"
    sed 's/%USER_NAME%/'"$USER_NAME"'/g' $CREATE_SCRIPTS_DIR/$BEFORE_CREATION_SQL > $TEMP_DIR/$BEFORE_CREATION_SQL
    runAsSys "$TEMP_DIR/$BEFORE_CREATION_SQL" "$CURRENT_DATABASE"
}

function applyPatches {
    echo "applyPatches"
    echo ""
    echo "--------------------------------------------------"
    echo "Starting processing $SCHEMA_NAME database"
    echo "--------------------------------------------------"
    echo "checking that at most one row exists in patches table ($SCHEMA_NAME.$VERSION_TABLE)..."
    echo "psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c 'SELECT COUNT(*) FROM $VERSION_TABLE';"
    ROW_COUNT=$(psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "SELECT COUNT(*) FROM $VERSION_TABLE;") || exit 1

    #removing unprintable/control cheracters
    ROW_COUNT=$(echo "$ROW_COUNT" | tr -d "[:cntrl:][:blank:]")
    ROW_COUNT=$(expr match $ROW_COUNT '\([a-z-]*[0-9]*\)' | tr -d "[a-z-]" )

    echo "rows returned $ROW_COUNT"
    LAST_PATCH=0
    if [ $ROW_COUNT -ne 0 ]; then
        if [ $ROW_COUNT -ne 1 ]; then
            echo "More than one row found. Please fix patches table."
            exit 1
        else
            LAST_PATCH=$(psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "SELECT version FROM db_schema_version;") || exit 1
        fi
    fi
    LAST_PATCH=$(echo "$LAST_PATCH" | tr -d "[:cntrl:][:blank:]")
    LAST_PATCH=$(expr match $LAST_PATCH '\([a-z-]*[0-9]*\)' | tr -d "[a-z-]" )
    echo "Last patch was [$LAST_PATCH]"
    LAST_APPLIED_PATCH=0
    echo "Dropping all connections to $SCHEMA_NAME"
    psql -h $HOST -p $PORT -U $USER -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='$SCHEMA_NAME' AND pid <> pg_backend_pid();" > /dev/null || exit 1
    for CURRENT_PATCH in $(ls $UPDATE_SCRIPTS_DIR | grep -e '^[0-9]\{4\}.*\.sql')
    do
        PATCH_NUMBER=${CURRENT_PATCH:0:4}
        #echo "current patch number: $PATCH_NUMBER"
        if [ $LAST_PATCH -lt $PATCH_NUMBER ]; then
            LAST_APPLIED_PATCH=$(expr match "$PATCH_NUMBER" '[0]*\([1-9].*\)')
            echo "Replacing %USER_NAME% with $USER_NAME"
            sed 's/%USER_NAME%/'"$USER_NAME"'/g' $UPDATE_SCRIPTS_DIR/$CURRENT_PATCH > $TEMP_DIR/$CURRENT_PATCH
            echo "$CURRENT_PATCH will be applyied on $SCHEMA_NAME database"
            echo "current patch number: $LAST_APPLIED_PATCH"
            runAsSys "$TEMP_DIR/$CURRENT_PATCH" "$SCHEMA_NAME"
            echo "psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c 'UPDATE db_schema_version SET version = $LAST_APPLIED_PATCH';"
            psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "UPDATE db_schema_version SET version = $LAST_APPLIED_PATCH;" || exit 1
        fi
    done
    echo "The DB is up-to-date"
}

function printUsage {
    echo "Usage: recreate [-b]"
}

if [ "$1" = "--help" ]; then
    printUsage
    exit 1
fi

mkdir $TEMP_DIR
createFromScratch
if [ "$1" = "-b" ]; then
    echo "'-b' argument detected. Restoring database from backup..."
    restoreFromBackup
else
    echo "Creating tables from scratch..."
    createDbTables
fi
applyPatches

rm -rf $TEMP_DIR