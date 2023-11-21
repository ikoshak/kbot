APP=$(shell basename $(shell git remote get-url origin))
REGISTRY=ikote
VERSION=${shell git describe --tags --abbrev=0}-${shell git rev-parse --short HEAD}
TARGETOS=linux
TARGETARCH=amd64 #${shell dpkg --print-architecture}

format:
	gofmt -s -w ./

lint:
	golint

test:
	go test-v

#setup dependencies
get:
	go get

build: format get
	CGO_ENABLED=0 GOOS=${TARGETOS} GOARCH=${TARGETARCH} go build -v -o kbot -ldflags "-X="github.com/ikoshak/kbot/cmd.appVersion=${VERSION}

image:
	docker build . -t ${REGISTRY}/${APP}:${VERSION}-${TARGETARCH}

push:
	docker push ${REGISTRY}/${APP}:${VERSION}-${TARGETARCH}

clean:
	rm -rf kbot