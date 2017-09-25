FROM h3poteto/sbt:0.13.16

COPY target/universal/ayanerer-1.0-SNAPSHOT.zip ayanerer-1.0-SNAPSHOT.zip

RUN unzip ayanerer-1.0-SNAPSHOT.zip

CMD ayanerer-1.0-SNAPSHOT/bin/ayanerer -Dconfig.file=/var/opt/app/ayanerer-1.0-SNAPSHOT/conf/prod.conf