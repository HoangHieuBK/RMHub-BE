FROM alpine:3.10
RUN apk update && \
    apk add ca-certificates openssh  && \
    # sed -i s/#PermitRootLogin.*/PermitRootLogin\ no/ /etc/ssh/sshd_config && \
    sed -i s/#PasswordAuthentication.*/PasswordAuthentication\ no/ /etc/ssh/sshd_config && \
    PASSWORD=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1) && \
    # deluser xfs && \
    # addgroup -S www-data -g 33 && adduser -S www-data -G www-data -u 33 -s /bin/sh && \
    # mkdir -p /home/www-data/.ssh && chown www-data:www-data /home/www-data/.ssh && chmod 700 /home/www-data/.ssh && \
    # passwd -d root && \
    # echo "www-data:${PASSWORD}" | chpasswd && \
    mkdir /root/.ssh && chmod 700 /root/.ssh && \
    echo "root:${PASSWORD}" | chpasswd && \
    ssh-keygen -A && \
    rm -rf /var/cache/apk/* && \
    echo "Password for root: ${PASSWORD}"
# VOLUME /home/www-data/.ssh
VOLUME /root/.ssh
EXPOSE 22/tcp
CMD ["/usr/sbin/sshd", "-De"]