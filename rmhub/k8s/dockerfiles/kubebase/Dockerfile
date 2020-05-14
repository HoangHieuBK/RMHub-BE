FROM alpine:3.10 as downloader
RUN wget https://storage.googleapis.com/kubernetes-release/release/$(wget -O- https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl -O kubectl
RUN chmod +x ./kubectl
RUN wget https://get.helm.sh/helm-v2.14.1-linux-amd64.tar.gz
RUN tar -xzf helm-v2.14.1-linux-amd64.tar.gz
RUN mv linux-amd64/helm ./helm
RUN chmod +x ./helm

FROM alpine:3.10
COPY --from=downloader kubectl /usr/local/bin/kubectl
COPY --from=downloader helm /usr/local/bin/helm
RUN apk update && apk add ca-certificates && rm -rf /var/cache/apk/*
