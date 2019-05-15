# Network configuration: bclan-it

Network with a generated crypto to reproduce test environment

## Use
 * Copy [docker-coompose-it.yaml](docker-coompose-it.yaml) in your project
 * Start compose:
```
docker-compose -f docker-compose-it.yaml up -d
```
 * Copy configuration
```
mkdir -p infra
docker cp  cli-bclan-network-it:/opt/blockchain-coop-dev/ ./infra/dev
```


## Release
make build tag-latest -e VERSION=0.3.1
