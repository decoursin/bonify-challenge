#! /bin/bash

set -euo pipefail

host=${HOST:-localhost}
port=${PORT:-8080}
actual=${TEST_FILENAME:-test.actual.csv}
expected=${TEST_FILENAME:-test.expected.csv}

generate_post_data()
{
    cat <<EOF
{
    "Partner_Account": "${line[0]}",
    "Partner_BLZ": "${line[2]}",
    "Bank_Name": "${line[3]}",
    "Partner_Name": "${line[4]}",
    "Booking_Text": "${line[5]}",
    "Subject": "${line[6]}",
    "Booking_Date": "${line[7]}",
    "Transfer_Type": "${line[8]}",
    "Currency": "${line[9]}",
    "Amount": "${line[10]}"
}
EOF
}

## truncate actual testing file.
## and add header
echo "account_number;datetime;spending (10-day window)" > "$actual"

## truncate table for accurate calculations.
psql -U postgres -h localhost -c 'truncate table account;' "bonify"

## Send data to API.
IFS=";"
{
    read; # skip header
    while read -a line; do
        echo "body: $(generate_post_data line)"

        curl -X PUT \
             -H "Accept: application/json" \
             -H "Content-Type: application/json" \
             --data "$(generate_post_data line)" \
             "$host:$port/transaction"
    done
} < account.csv

## Assert expected == actual
DIFF=$(diff "$expected" "$actual") 
if [ "$DIFF" != "" ] 
then
    echo "FAILING TEST! $DIFF"
else
	echo "Test pass! Nice job."
fi
