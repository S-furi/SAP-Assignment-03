#!/usr/bin/env python3

import argparse
from python_on_whales import DockerClient
from python_on_whales.components.compose.cli_wrapper import ComposeCLI
import logging
import subprocess

logger = logging.getLogger(__name__)

files = [
    "api-gateway/docker-compose.yml",
    "vehicle-service/docker-compose.yml",
    "user-service/docker-compose.yml",
    "ride-registry/docker-compose.yml",
    "ride-service/docker-compose.yml",
]


def build_compose(file_path) -> ComposeCLI:
    return DockerClient(compose_files=[file_path]).compose


def handle_compose(action: str):
    if action == "up":
        for i, compose in enumerate(map(lambda x: build_compose(x), files)):
            compose.up(detach=True, wait=True, quiet=False)
            logger.debug(f"Service ${files[i]} is now up and running")
        logger.info("All images are set up and running!")
    elif action == "down":
        for i, compose in enumerate(map(lambda x: build_compose(x), files)):
            compose.down(remove_orphans=True)
            logger.debug(f"Service ${files[i]} is deleted")
        logger.info("All containers are now deleted.")
    elif action == "downrmi":
        for i, compose in enumerate(map(lambda x: build_compose(x), files)):
            compose.down(remove_orphans=True, remove_images="all")
            logger.debug(f"Service ${files[i]} is deleted")
            network_name = "service-net"
            subprocess.Popen(f"docker network ls --format '{{.Name }} {{ .ID }}' | grep {network_name} | cut -d ' ' -f 2 | xargs docker network rm").wait()
            logger.info(f"Network: {network_name} has been deleted.")
        logger.info("All containers, iamges and shared network are now deleted.")
    else:
        logger.error("No command was found for: " + action)
        exit(1)


if __name__ == "__main__":
    logging.basicConfig(level=logging.DEBUG)

    parser = argparse.ArgumentParser(
        description="Manage Docker Compose services for SAP-Assignment-02."
    )
    parser.add_argument(
        "action",
        choices=["up", "down", "downrmi"],
        help="Action to perform: 'up' to start all services, 'down' to stop all services.",
    )

    args = parser.parse_args()

    if args.action in ["up", "down", "downrmi"]:
        handle_compose(args.action)
        exit(0)
    else:
        parser.print_help()
        exit(1)
