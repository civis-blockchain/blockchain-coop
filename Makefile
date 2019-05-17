FABRIC_CLI_CHAINCODE_NAME	:= civisblockchain/fabric-cli-chaincode
FABRIC_CLI_CHAINCODE_IMG	:= ${FABRIC_CLI_CHAINCODE_NAME}:${VERSION}
FABRIC_CLI_CHAINCODE_LATEST := ${FABRIC_CLI_CHAINCODE_NAME}:latest

SDK_REST_JAVA_NAME	    := civisblockchain/coop-rest-java
SDK_REST_JAVA_IMG	    := ${SDK_REST_JAVA_NAME}:${VERSION}
SDK_REST_JAVA_LATEST	:= ${SDK_REST_JAVA_NAME}:latest

build: build-docker-fabric-cli-chaincode build-docker-sdk-rest-java build-bclan-it

tag-latest: tag-latest-docker-fabric-cli-chaincode tag-latest-docker-sdk-rest-java tag-latest-bclan-it

push: push-docker-fabric-cli-chaincode push-docker-sdk-rest-java push-docker-bclan-it

build-docker-fabric-cli-chaincode:
	@docker build -f docker/FabricCli_Dockerfile -t ${FABRIC_CLI_CHAINCODE_IMG} .

tag-latest-docker-fabric-cli-chaincode:
	@docker tag ${FABRIC_CLI_CHAINCODE_IMG} ${FABRIC_CLI_CHAINCODE_LATEST}

push-docker-fabric-cli-chaincode:
	@docker push ${FABRIC_CLI_CHAINCODE_NAME}

inspect-docker-fabric-cli-chaincode:
	@docker run -it ${FABRIC_CLI_CHAINCODE_IMG} /bin/bash

build-docker-sdk-rest-java:
	@docker build -f sdk/java/Dockerfile -t ${SDK_REST_JAVA_IMG} ./sdk/java

tag-latest-docker-sdk-rest-java:
	@docker tag ${SDK_REST_JAVA_IMG} ${SDK_REST_JAVA_LATEST}

push-docker-sdk-rest-java:
	@docker push ${SDK_REST_JAVA_NAME}

build-bclan-it:
	@make -C bclan-it build -e VERSION=${VERSION}

tag-latest-bclan-it:
	@make -C bclan-it tag-latest -e VERSION=${VERSION}

push-docker-bclan-it:
	@make -C bclan-it push -e VERSION=${VERSION}
