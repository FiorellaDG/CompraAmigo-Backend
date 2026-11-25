#!/bin/sh
set -eu

# Render provides two persistent 32-byte seeds. HS512 needs at least 64 bytes.
: "${JWT_SEED_A:?Falta JWT_SEED_A}"
: "${JWT_SEED_B:?Falta JWT_SEED_B}"
jwt_primera=$(printf '%s' "$JWT_SEED_A" | base64 -d | wc -c)
jwt_segunda=$(printf '%s' "$JWT_SEED_B" | base64 -d | wc -c)
if [ "$jwt_primera" -ne 32 ] || [ "$jwt_segunda" -ne 32 ]; then
    echo "Configura JWT_SEED_A y JWT_SEED_B con generateValue de Render." >&2
    exit 1
fi
JWT_SECRET=$(
    {
        printf '%s' "$JWT_SEED_A" | base64 -d
        printf '%s' "$JWT_SEED_B" | base64 -d
    } | base64 -w0
)
export JWT_SECRET

exec java -Xms64m -Xmx224m -Xss512k -XX:+UseSerialGC -XX:ActiveProcessorCount=2 \
    -XX:ReservedCodeCacheSize=48m -XX:+ExitOnOutOfMemoryError -jar /app/backend.jar
