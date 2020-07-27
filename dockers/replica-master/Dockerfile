FROM mongo:4.2.1
ADD ./replicaSet.js /replicaSet.js
ADD ./setup.sh /setup.sh
RUN chmod a+x /setup.sh
ENTRYPOINT ["./setup.sh"]
