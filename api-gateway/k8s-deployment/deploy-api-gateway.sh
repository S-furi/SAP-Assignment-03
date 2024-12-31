#!/bin/bash
kubectl apply -f ./config.yaml
kubectl apply -f ./services.yaml
kubectl apply -f ./deployments.yaml
kubectl apply -f ./prometheus-config.yaml
kubectl apply -f ./grafana-config.yaml
