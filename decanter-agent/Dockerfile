FROM jeanblanchard/java
RUN apk add --update bash && rm -rf /var/cache/apk/*
ADD target/assembly /decanter/
WORKDIR /decanter
ENTRYPOINT ["bin/karaf", "console"]
