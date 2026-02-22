#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:-http://localhost:8080}"
COUNT="${2:-20}"

for i in $(seq 1 "${COUNT}"); do
  curl -sS -X POST "${BASE_URL}/events" \
    -H 'Content-Type: application/json' \
    -d "{\"customerId\":\"CUST-${i}\",\"amount\":199.90}" > /dev/null
done

echo "Sent ${COUNT} events."
