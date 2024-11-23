#### Software Architecture and Platforms - a.y. 2024-2025
 
# Assignment #02 - 20241028 

v1.0.0-20241111

**Description** 

Consider the "EBike application" seen in the first assignment. The objective of the second assignment is to design and develop a distributed version based on microservices and microservices patterns - as discussed in modules 2.X and in Lab Notes about microservices patterns, and adopting DDD as reference method for the analysis and development stage.

In particular:

- A knowledge crunching process must be enacted so to gather and represent architectural drivers, defining (following DDD) a proper Ubiquitous Language, structured around a domain model, and using it to define artifacts useful to both define requirements and quality attributes (user stories, use cases, domain story telling, quality attribute scenarios), as well as bounded contexts and context map.
- Moving from strategical to tactical design, a model-driven design should be adopted, applying -- when useful -- DDD domain model pattern.
- The architecture should be based on microservices and hexagonal/ports & adapters/clean architecture style, for the design of the individual service.
- The architecture should integrate microservices patterns that are considered relevant, given the case study, considering the different examples and categories seen in the course:
  - Application-level patterns
    - Mandatory: API Gateways, Service Discovery 
  - Deployment patterns
    - Mandatory: Service-as-Container
  - Observabilty Patterns
    - Mandatory: Heath Check API, Application Metrics
  - Runtime Configuration pattern
    - Mandatory: Externalized Configuration
  - Testing patterns
    - Mandatory: One test for each level of the Test Pyramid
- At the implementation level, at least one microservice (but not necessarily all) should be implemented using one of the existing frameworks/platforms available from the community
- A strategy for validating the proposed architecture should be devised and enacted, given the requirements and the quality attributes defined in the analysis stage.  
    
**Deliverable**

A zipped folder ``Assignment-02-<Surname>`` including a maven-based or gradle-based project, with sources and the report in PDF. The deliverable can be submitted using a link on the course web site.

**Deadline** 

December 6, 2024 - 9:00 AM
 
