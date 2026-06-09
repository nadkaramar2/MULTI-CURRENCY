#!/usr/bin/env bash
set -euo pipefail

cd ~/MultiCurrency

if [[ ! -f .env ]]; then
  echo "Copy .env.prod to .env and set MYSQL_ROOT_PASSWORD + EC2_PUBLIC_IP"
  exit 1
fi

set -a
source .env
set +a

BE=backend/CMSAccountManagementService_app
UI=ui/Multcurrency_updatedCode-31-10-2024
DEPLOY=scripts/aws-deploy

mkdir -p "$BE/docker" "$UI/docker"
export MYSQL_ROOT_PASSWORD EC2_PUBLIC_IP
envsubst '${MYSQL_ROOT_PASSWORD}' < "$DEPLOY/dbconfig.docker.properties" > "$BE/docker/dbconfig.properties"
cp "$DEPLOY/application-docker.properties" "$BE/docker/application-docker.properties"
cp "$DEPLOY/Dockerfile.backend" "$BE/Dockerfile"
cp "$DEPLOY/Dockerfile.frontend" "$UI/Dockerfile"
cp "$DEPLOY/nginx.conf" "$UI/nginx.conf"

echo "Building images (first run: 15–25 min)..."
docker compose -f docker-compose.prod.yml build

echo "Starting services..."
docker compose -f docker-compose.prod.yml up -d

echo ""
echo "============================================"
echo "Multi-Currency deployment started"
echo "  UI:      http://${EC2_PUBLIC_IP}:8380"
echo "  API:     http://${EC2_PUBLIC_IP}:8385/AccountManagementAPI/"
echo "  MySQL:   account_management_sequro (internal)"
echo ""
echo "Open EC2 Security Group inbound: TCP 8380, 8385"
echo "Check: docker compose -f docker-compose.prod.yml ps"
echo "Logs:  docker compose -f docker-compose.prod.yml logs -f backend"
echo "============================================"
