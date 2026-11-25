FROM node:26-bookworm-slim AS frontend
WORKDIR /proyecto
ARG FRONTEND_COMMIT=216b85ab8405141e24ff388879daf03743801e25
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates \
    && rm -rf /var/lib/apt/lists/*
RUN curl --fail --silent --show-error --location --retry 3 \
      "https://codeload.github.com/FiorellaDG/CompraAmigo-Frontend/tar.gz/${FRONTEND_COMMIT}" \
      -o frontend.tar.gz \
    && tar -xzf frontend.tar.gz --strip-components=1 \
    && rm frontend.tar.gz
RUN npm ci --no-audit --no-fund && npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /proyecto
COPY pom.xml ./
COPY src ./src
RUN mvn -B -ntp package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=backend /proyecto/target/backend.jar ./backend.jar
COPY --from=frontend /proyecto/dist/CompraAmigo-Frontend/browser/ ./frontend/
COPY deploy/render/start.sh ./start.sh
ENV SPRING_PROFILES_ACTIVE=cloud,render
ENV APP_STATIC_PATH=file:/app/frontend/
USER 10001:10001
EXPOSE 10000
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
