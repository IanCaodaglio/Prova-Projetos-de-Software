#!/usr/bin/env bash
# Rodar UMA vez na EC2 (depois de instalar o Docker, conforme "AWS - Arquitetura").
# Uso:
#   DB_PASSWORD='sua-senha' bash setup-ec2.sh
# Os valores de DB_NAME / DB_USER / DB_PASSWORD precisam ser IGUAIS aos secrets do GitHub.
# O secret DB_HOST deve ser o nome do container abaixo: postgres-db
set -euo pipefail

DB_NAME="${DB_NAME:-cursosdb}"
DB_USER="${DB_USER:-usuario}"
DB_PASSWORD="${DB_PASSWORD:?Defina DB_PASSWORD. Ex: DB_PASSWORD='xyz' bash setup-ec2.sh}"

docker network inspect rede >/dev/null 2>&1 || docker network create -d bridge rede

if docker ps -a --format '{{.Names}}' | grep -qx postgres-db; then
  echo "Container postgres-db já existe. Garantindo que está na rede e rodando..."
  docker network connect rede postgres-db 2>/dev/null || true
  docker start postgres-db
else
  # Sem -p 5432:5432: o banco só é acessível pela rede Docker (mais seguro).
  # Volume pgdata: os dados sobrevivem se o container for recriado.
  docker run -d --name postgres-db \
    --network rede \
    --restart unless-stopped \
    -e POSTGRES_DB="$DB_NAME" \
    -e POSTGRES_USER="$DB_USER" \
    -e POSTGRES_PASSWORD="$DB_PASSWORD" \
    -v pgdata:/var/lib/postgresql/data \
    postgres:16
fi

docker ps
