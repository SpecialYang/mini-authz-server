HUB ?= specialyang
IMG ?= mini-authz-server
TAG ?= latest

.PHONY: build
build:
	@mvn clean package -Dmaven.test.skip=true

.PHONY: docker-build
docker-build: build
	@docker build -f Dockerfile -t $(IMG):$(TAG) .

.PHONY: docker-push
docker-push:
	@docker tag $(IMG):$(TAG) $(HUB)/$(IMG):$(TAG)
	@docker push $(HUB)/$(IMG):$(TAG)

.PHONY: k8s-deploy
k8s-deploy:
	@kubectl apply -f manifest/deployment.yaml