#!/bin/bash
find . -path "*/docker" -prune -o -name "*.yaml" -print | xargs -n 1 kubectl apply -f
