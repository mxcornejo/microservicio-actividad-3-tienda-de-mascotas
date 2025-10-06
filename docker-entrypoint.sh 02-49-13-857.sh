#!/bin/sh
set -e

echo "---- docker-entrypoint: starting ----"
echo "TNS_ADMIN=${TNS_ADMIN:-<not set>}"
echo "ORACLE_WALLET_DIR=${ORACLE_WALLET_DIR:-<not set>}"

if [ -d "${TNS_ADMIN}" ]; then
  echo "Listing contents of $TNS_ADMIN"
  ls -la "$TNS_ADMIN"
else
  echo "TNS_ADMIN directory not found: $TNS_ADMIN"
fi

echo "Starting application with TNS_ADMIN=${TNS_ADMIN:-/app/Wallet_BDFORO}"
exec java -Doracle.net.tns_admin=${TNS_ADMIN:-/app/Wallet_BDFORO} -jar /app/app.jar
