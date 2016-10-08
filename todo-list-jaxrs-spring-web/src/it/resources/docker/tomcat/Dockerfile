FROM tomcat:8-jre8

ARG POSTGRES_DRV_VER=9.4.1209.jre7
ARG ARTIFACT

RUN curl -o $CATALINA_HOME/lib/postgresql-$POSTGRES_DRV_VER.jar http://repo1.maven.org/maven2/org/postgresql/postgresql/$POSTGRES_DRV_VER/postgresql-$POSTGRES_DRV_VER.jar

COPY maven/$ARTIFACT $CATALINA_HOME/webapps
COPY setenv.sh $CATALINA_HOME/bin