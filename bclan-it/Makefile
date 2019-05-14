include .env
export

FABRIC_CA_NAME	:= civisblockchain/bclan-ca
FABRIC_CA_IMG	:= ${FABRIC_CA_NAME}:${VERSION}
FABRIC_CA_LATEST := ${FABRIC_CA_NAME}:latest

FABRIC_PEER_NAME	:= civisblockchain/bclan-peer
FABRIC_PEER_IMG	:= ${FABRIC_PEER_NAME}:${VERSION}
FABRIC_PEER_LATEST := ${FABRIC_PEER_NAME}:latest

FABRIC_ORDERER_NAME	:= civisblockchain/bclan-orderer
FABRIC_ORDERER_IMG	:= ${FABRIC_ORDERER_NAME}:${VERSION}
FABRIC_ORDERER_LATEST := ${FABRIC_ORDERER_NAME}:latest

FABRIC_CLI_NAME	:= civisblockchain/bclan-cli
FABRIC_CLI_IMG	:= ${FABRIC_CLI_NAME}:${VERSION}
FABRIC_CLI_LATEST := ${FABRIC_CLI_NAME}:latest

build: build-ca build-peer build-orderer build-cli

tag-latest: tag-latest-ca tag-latest-peer tag-latest-orderer tag-latest-cli

push: push-ca push-peer push-orderer push-cli

build-ca:
	@docker build \
         --build-arg COOP_CA_HOSTNAME=$$CA_HOSTNAME \
         --build-arg COOP_PEER_DOMAIN=$$cli_ORGA \
         --build-arg ca__CA_KEYFILE=$$ca__CA_KEYFILE \
         --build-arg ca__TLS_KEYFILE=$$ca__TLS_KEYFILE \
         --build-arg ca__ADMIN=$$ca__ADMIN \
         --build-arg ca__PASSWD=$$ca__PASSWD \
         -f docker/Ca_Dockerfile \
         -t ${FABRIC_CA_IMG} .

tag-latest-ca:
	@docker tag ${FABRIC_CA_IMG} ${FABRIC_CA_LATEST}

push-ca:
	@docker push ${FABRIC_CA_IMG}

build-peer:
	@docker build \
		--build-arg COOP_PEER=peer0 \
    	--build-arg COOP_PEER_MSP=$$peer_msp \
    	--build-arg COOP_PEER_DOMAIN=$$cli_ORGA \
    	-f docker/Peer_Dockerfile \
    	-t ${FABRIC_PEER_IMG} .

tag-latest-peer:
	@docker tag ${FABRIC_PEER_IMG} ${FABRIC_PEER_LATEST}

push-peer:
	@docker push ${FABRIC_PEER_IMG}

build-orderer:
	docker build \
    	--build-arg COOP_CA_DOMAIN=$$orderer_org \
    	--build-arg COOP_ORDERER_HOSTNAME=$$orderer_hostname \
    	-f docker/Orderer_Dockerfile \
    	-t ${FABRIC_ORDERER_IMG} .

tag-latest-orderer:
	@docker tag ${FABRIC_ORDERER_IMG} ${FABRIC_ORDERER_LATEST}

push-orderer:
	@docker push ${FABRIC_ORDERER_IMG}

build-cli:
	docker build \
    	--build-arg COOP_CA_DOMAIN=$$orderer_org \
    	--build-arg COOP_PEER=peer0 \
    	--build-arg COOP_PEER_DOMAIN=$$cli_ORGA \
    	--build-arg COOP_PEER_MSP=$$peer_msp \
    	--build-arg CLI_USER=$$cli_user \
    	-f docker/Cli_Dockerfile \
    	-t ${FABRIC_CLI_IMG} .

tag-latest-cli:
	@docker tag ${FABRIC_CLI_IMG} ${FABRIC_CLI_LATEST}

push-cli:
	@docker push ${FABRIC_CLI_IMG}
