#### Software Architecture and Platforms - a.y. 2024-2025
 # Assignment #02 - 20241028 

### Running

1. Run `pip install -r requirements.txt`
2. Deploy all services throgh the script `./deploy-all.py up`
3. Go to `dashboard-service` and run `./gradlew run`


### Tear Down
1. Stop the JVM application
2. Go back to the root of the project
3. Run
  - `./deploy-all.py down` for tearing down all services
  - `./deploy-all.py downrmi` for stopping all containers, and remove all related images and volumes
