#!/bin/sh

sbt clean test
SBT_EXIT_STATUS=$?

if [ $SBT_EXIT_STATUS -ne 0 ]; then
    ./output-log-exceptions.sh
    exit $SBT_EXIT_STATUS
fi
