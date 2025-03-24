#### Software Architecture and Platforms - a.y. 2024-2025
 
# Assignment #03 - 20241209

v0.9.0-20241209

**Description** 

- Develop a solution of the "EBike application" based on an event-driven microservices architecture, applying the Event Sourcing pattern where considered useful.

- Define a deployment of the application on a distributed infrastructure (e.g. a cluster) based on Kubernetes, exploiting the features provided by the framework.

- Consider an extension of the EBike case study, featuring a new kind of experimental e-bike, i.e. autonomous e-bike ("a-bike") for smart city environment. Main features of the a-bikes:
  - can autonomously reach the nearest station, after being used;
  - can autonomously reach a user, asking for the service.
  
  The a-bikes are meant to be deployed into a smart city featuring a digital twin which provides basic functionalities to support a-bike autonomous mobility. Propose a solution, discussing the design and including a demo implementation of a core part, capturing main aspects.  

    
**Deliverable**

A GitHub repo including sources and documentation. The link to the repo must be included in a file ``Assignment-03-<Surname>`` and submitted using a link on the course web site.

**Deadline** 

No deadlines.

---
## Running the application through Kubernetes

1. Go to `kube-migration/` directory
2. Make sure you're in a kubernetes cluster (or minikube is up and running)
3. Run all kubernetes manifests in subdirectories with `./run_all.sh apply` for applying all of them
4. Check deployments with `kubectl get pods` or through `minikube dashboard` if deployed on minikube
5. When all services are up and running, port forward api-gateway service and kafka service with:

```bash
kubectl port-forward svc/kafka 9092:9092
kubectl port-forward svc/api-gateway 4001:4001
```

6. Go to back to project root and move to `dashboard-service` and run `./gradlew run` in order to run the application
