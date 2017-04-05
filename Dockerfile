FROM h3poteto/activator

COPY target/universal/ayanerer-1.0-SNAPSHOT.zip ayanerer-1.0-SNAPSHOT.zip
COPY conf conf

RUN unzip ayanerer-1.0-SNAPSHOT.zip


CMD ayanerer-1.0-SNAPSHOT/bin/ayanerer -Dconfig.file=/var/opt/app/conf/prod.conf