#!/usr/bin/env bash
set -euo pipefail

if [[ "$EUID" != 0 ]]; then
    echo "Ejecuta con sudo: sudo bash install.sh IP_PUBLICA [CORREO_ADMIN]" >&2
    exit 1
fi

ip=${1:?Indica la IPv4 pública de la instancia}
if [[ ! "$ip" =~ ^[0-9]{1,3}(\.[0-9]{1,3}){3}$ ]]; then
    echo "La dirección debe ser la IPv4 pública que muestra EC2." >&2
    exit 1
fi
admin_email=${2:-administrador@compraamigo.test}
if [[ ! "$admin_email" =~ ^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$ ]]; then
    echo "El correo del administrador no tiene un formato válido." >&2
    exit 1
fi

cd "$(dirname "${BASH_SOURCE[0]}")"
test -s backend.jar
test -s frontend/index.html
test -s compraamigo.service
test -s compraamigo.nginx

export DEBIAN_FRONTEND=noninteractive
apt-get update
apt-get install -y nginx postgresql openjdk-21-jre-headless curl openssl
systemctl enable --now postgresql
if ! id compraamigo >/dev/null 2>&1; then
    useradd --system --user-group --home-dir /opt/compraamigo --shell /usr/sbin/nologin compraamigo
fi

install -d -m 700 /etc/compraamigo
env_file=/etc/compraamigo/compraamigo.env
if [[ ! -s "$env_file" ]]; then
    db_password=$(openssl rand -hex 24)
    jwt_secret=$(openssl rand -base64 64 | tr -d '\n')
    admin_password=$(openssl rand -hex 12)
    ( umask 077
      cat > "$env_file" <<EOF
DB_URL=jdbc:postgresql://127.0.0.1:5432/db_compraamigo
DB_USERNAME=compraamigo
DB_PASSWORD=$db_password
JWT_SECRET=$jwt_secret
FRONTEND_URL=http://$ip
APP_INIT_ADMIN_EMAIL=$admin_email
APP_INIT_ADMIN_PASSWORD=$admin_password
EOF
    )
else
    sed -i "s|^FRONTEND_URL=.*|FRONTEND_URL=http://$ip|" "$env_file"
    db_password=$(sed -n 's/^DB_PASSWORD=//p' "$env_file")
fi
if [[ ! "$db_password" =~ ^[0-9a-f]{48}$ ]]; then
    echo "Revisa DB_PASSWORD en el archivo de esta instalación." >&2
    exit 1
fi

runuser -u postgres -- psql -X -v ON_ERROR_STOP=1 -d postgres <<SQL
SELECT 'CREATE ROLE compraamigo LOGIN PASSWORD ''$db_password'''
WHERE NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'compraamigo')
\gexec
SELECT 'CREATE DATABASE db_compraamigo OWNER compraamigo'
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'db_compraamigo')
\gexec
SQL

install -d /opt/compraamigo /var/www/compraamigo
install -m 644 backend.jar /opt/compraamigo/backend.jar
cp -R frontend/. /var/www/compraamigo/
chmod -R a+rX /var/www/compraamigo
install -m 644 compraamigo.service /etc/systemd/system/compraamigo.service
systemctl daemon-reload
systemctl enable compraamigo
systemctl restart compraamigo

ready=0
for attempt in {1..60}; do
    if curl --silent --fail --max-time 2 http://127.0.0.1:8080/api/health >/dev/null; then
        ready=1
        break
    fi
    sleep 2
done
if [[ "$ready" != 1 ]]; then
    journalctl -u compraamigo -n 50 --no-pager >&2
    exit 1
fi

install -m 644 compraamigo.nginx /etc/nginx/sites-available/compraamigo
ln -sfn /etc/nginx/sites-available/compraamigo /etc/nginx/sites-enabled/compraamigo
if [[ -L /etc/nginx/sites-enabled/default ]]; then
    unlink /etc/nginx/sites-enabled/default
fi
nginx -t
systemctl enable --now nginx
systemctl reload nginx
curl --silent --show-error --fail http://127.0.0.1/api/health
echo
echo "CompraAmigo: http://$ip/"
echo "Para consultar los datos de acceso de la cuenta de demostración desde SSH:"
echo "sudo grep '^APP_INIT_ADMIN_' /etc/compraamigo/compraamigo.env"
