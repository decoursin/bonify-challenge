
db_name=${DB_NAME:-"bonify"}
port=${PORT:-5432}

## run docker
postgres_container=$(docker run -d --name=postgres-9.5 -e POSTGRES_PASSWORD= -e POSTGRES_USER=postgres -e POSTGRES_DB="$db_name" -p $port:5432 -v "/var/log/postgresql/" -v "/etc/postgresql/" postgres:9.5)

echo "postgres container id: $postgres_container"

## wait until the postgres db is up.
until nc -z $(sudo docker inspect --format='{{.NetworkSettings.IPAddress}}' $postgres_container) $port
do
    echo "waiting for postgres container..."
    sleep 0.5
done

## create table schema
psql -U postgres -h localhost -f ./schema.sql "$db_name"
