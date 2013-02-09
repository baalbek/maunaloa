#!/bin/bash

# mvn compile
if [ "$1" = "c" ]; then
  mvn compile
fi

mvn exec:java -Dexec.mainClass="maunaloa.App"

exit 0
