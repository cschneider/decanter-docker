FROM jeanblanchard/busybox-java:8
ADD target/assembly /decanter/
WORKDIR /decanter
ENTRYPOINT ["sh", "bin/karaf"]
