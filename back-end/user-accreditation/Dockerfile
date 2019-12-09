FROM adoptopenjdk:11-jdk-hotspot
EXPOSE 9000
ADD https://piccolo.link/sbt-1.3.4.tgz /tmp/sbt.tgz
RUN tar -C /opt -xzf /tmp/sbt.tgz && \
    /opt/sbt/bin/sbt run && \
    mkdir /opt/user-accreditation
VOLUME /opt/user-accreditation
WORKDIR /opt/user-accreditation
CMD /opt/sbt/bin/sbt run
