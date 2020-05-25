FROM maven:3.6.3-jdk-11 as build

ARG WORK_DIR=/build

COPY . ${WORK_DIR}/
WORKDIR ${WORK_DIR}

RUN mkdir -p /root/.m2 /usr/tsi/verification-portal
RUN mvn --batch-mode ${MAVEN_ARGS} install
RUN cp target/cwa-verification-portal*.jar /usr/tsi/verification-portal/verification-portal.jar

FROM gcr.io/distroless/java:11
COPY --from=build /usr/tsi/verification-portal/verification-portal.jar .
CMD ["verification-portal.jar"]
EXPOSE 8080