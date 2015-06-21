#!/bin/bash
. dbsetup.properties

VERSION_TABLE="DB_SCHEMA_VERSION"

CREATE_SCRIPTS_DIR="create"
ALWAYS_SCRIPTS_DIR="always"
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
    psql -h $HOST -p $PORT -U $USER -f $1 $2|| exit 1
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
            if [ "$DO_FORCE" = "YES" ]; then
                $(psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "UPDATE db_schema_version SET version = $FORCE_VERSION_TO;")
                LAST_PATCH="$FORCE_VERSION_TO";
            fi
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
        # echo "current patch number: $PATCH_NUMBER"
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

function applyRegularScripts {
    echo "applyRegularScripts"
    echo ""
    echo "--------------------------------------------------"
    echo "Starting processing $SCHEMA_NAME database"
    echo "--------------------------------------------------"
    echo ""

    for CURRENT_PATCH in $(ls $ALWAYS_SCRIPTS_DIR | grep -e '^[0-9]\{4\}.*\.sql')
    do
        PATCH_NUMBER=${CURRENT_PATCH:0:4}
        echo "current patch number: $PATCH_NUMBER"
        runAsSys "$ALWAYS_SCRIPTS_DIR/$CURRENT_PATCH" "$SCHEMA_NAME"
    done
    echo "The DB is up-to-date"
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
    for CURRENT_SCRIPT in $(ls $CREATE_SCRIPTS_DIR | grep -e '^[0-9]\{4\}.*\.sql')
    do
        echo "Replacing %USER_NAME% with $USER_NAME"
        sed 's/%USER_NAME%/'"$USER_NAME"'/g' $CREATE_SCRIPTS_DIR/$CURRENT_SCRIPT > $TEMP_DIR/$CURRENT_SCRIPT
        echo "Creating tables and other stuff for $SCHEMA_NAME"
        runAsSys "$TEMP_DIR/$CURRENT_SCRIPT" "$SCHEMA_NAME"
    done
    echo "psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c \"insert into "$VERSION_TABLE" (version) values(0);\""
    psql -h $HOST -p $PORT -d $SCHEMA_NAME -U $USER -c "insert into "$VERSION_TABLE" (version) values(0);" || exit 1
}

function printUsage {
    echo "Usage: run [force]"
    echo "force - will drop DBs and create them from scratch"
    echo "Usage: run [fromVersion N]"
    echo "fromVersion N - will set database version to N"
}

if [ "$1" = "--help" ]; then
    printUsage
    exit 1
fi

mkdir $TEMP_DIR
# check whether the first parameter is "force". If yes, we will try to drop all DBs and create them from scratch.
if [ "$1" = "force" ]; then
    echo "'force' argument detected. Creating database from scratch..."
    createFromScratch
fi
if [ "$1" = "fromVersion" ]; then
    DO_FORCE="YES";
    FORCE_VERSION_TO="$2"
    echo "Force database version to $FORCE_VERSION_TO";
fi
applyPatches
applyRegularScripts

rm -rf ${TEMP_DIR}