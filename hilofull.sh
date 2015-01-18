#!/bin/bash

cp build/libs/maunaloa-4.0.jar dist

scp dist/*.jar hilo:/home/rcs/opt/java/maunaloa/dist

exit 0


