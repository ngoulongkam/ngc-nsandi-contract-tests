#!/bin/bash

function printExceptionsFromLog {
    echo "Printing log exception for:" $1
    sm --logs $1 | grep -i '^[[:space:]]*at' --before-context=7
}

SERVICES=`sm -d | grep -E '^[[:space:]]*NGC_HELP_TO_SAVE_CONTRACT_TESTS' | sed -e 's/^.*=>//' | sed -e 's/,//g'`

for SERVICE in $SERVICES; do
    printExceptionsFromLog $SERVICE
done
