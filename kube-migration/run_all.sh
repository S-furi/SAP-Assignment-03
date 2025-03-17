#!/bin/bash

if [[ "$1" != "apply" && "$1" != "delete" ]]; then
  echo "Usage: $0 [apply|delete]"
  exit 1
fi

ACTION=$1

find . -path "*/docker" -prune -o -name "*.yaml" -print | xargs -n 1 kubectl $ACTION -f

