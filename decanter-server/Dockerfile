FROM jeanblanchard/java
ADD target/assembly /decanter/
WORKDIR /decanter
ENTRYPOINT ["sh", "bin/karaf"]
