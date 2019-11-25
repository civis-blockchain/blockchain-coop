FABRIC_CLI_CHAINCODE_NAME	:= civisblockchain/fabric-cli-chaincode
FABRIC_CLI_CHAINCODE_IMG	:= ${FABRIC_CLI_CHAINCODE_NAME}:${VERSION}
FABRIC_CLI_CHAINCODE_LATEST := ${FABRIC_CLI_CHAINCODE_NAME}:latest

build: build-docker-fabric-cli-chaincode

tag-latest: tag-latest-docker-fabric-cli-chaincode

push: push-docker-fabric-cli-chaincode

build-docker-fabric-cli-chaincode:
	@docker build -f docker/FabricCli_Dockerfile -t ${FABRIC_CLI_CHAINCODE_IMG} .

tag-latest-docker-fabric-cli-chaincode:
	@docker tag ${FABRIC_CLI_CHAINCODE_IMG} ${FABRIC_CLI_CHAINCODE_LATEST}

push-docker-fabric-cli-chaincode:
	@docker push ${FABRIC_CLI_CHAINCODE_NAME}

inspect-docker-fabric-cli-chaincode:
	@docker run -it ${FABRIC_CLI_CHAINCODE_IMG} /bin/bash