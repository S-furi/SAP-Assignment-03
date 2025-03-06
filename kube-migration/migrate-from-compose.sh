#!/bin/bash

# This file shows the basic workflow for craeting a set of kubernetes
# manifest files from  a docker compose file.

# First, if your file makes use of .env files, run this command in order to
# substitute all variables inside the script.
docker compose config > docker-compose-resolved.yaml

# Now, using `kompose` (https://kompose.io/), we can convert the docker composes.

# NOTE: every service must have a port, event if default (e.g. postgres with port 5432),
# so in the resolved compose file, make sure to include those files.

# NOTE: when using volumes, make sure to include also the flag `--volumes hostPath` in
# order to make kubernetes properly crate PersistentVolumeClaims

kompose convert --volumes hostPath -f docker/docker-compose-resolved.yaml

# Now you can apply kubernetes manifest files with the usual `kubectl apply -f <manifest>.yaml

for manifest in $(ls -1 *yaml) ; do kubectl apply -f $file ; done
