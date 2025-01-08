#!/bin/bash

RIDE_SERVICE_PATH=./ride-service/
RIDE_REGISTRY_PATH=./ride-registry/
VEHICLE_SERVICE_PATH=./vehicle-service/

kube_scripts=( \
	"./ride-registry/k8s-deployment/deploy-ride-registry.sh" \
	"./ride-service/k8s-deployment/deploy-ride-service.sh" \
	"./user-service/k8s-deployment/deploy-user-service.sh" \
	"./api-gateway/k8s-deployment/deploy-api-gateway.sh" \
	"./vehicle-service/k8s/k8s-deployment/deploy-vehicle-service.sh"
)

for script in ${kube_scripts[@]}; do
	echo "Deploying: $script"
	bash $script &
done

wait

echo "Deploying vehicle service..."
kubectl apply -f ./vehicle-service/vehicle-service-deployment.yml

echo "All kubernetes environemnts have been deployed!"
