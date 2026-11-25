# CompraAmigo en AWS Academy

Una instancia EC2 aloja Angular, Spring Boot y PostgreSQL. Nginx sirve Angular y reenvía `/api/` a Spring Boot por el puerto local 8080. Spring Boot guarda los datos en PostgreSQL por el puerto local 5432. systemd inicia el backend con el servidor y lo vuelve a iniciar si falla.

## Crear la instancia en Learner Lab

1. Inicia el laboratorio y abre la consola de AWS desde el enlace **AWS** del curso. Entra a **EC2 → Launch instance**.
2. Usa el nombre `compraamigo-demo`, la imagen **Ubuntu Server 24.04 LTS**, arquitectura **x86_64**, tipo **t3.small** y disco **12 GiB gp3**. Usa el modo de créditos **Standard** si está disponible y elige una región habilitada por tu laboratorio.
3. Selecciona el par de claves del laboratorio o crea uno si el lab lo permite. Guarda la clave PEM en tu computadora.
4. Selecciona una subred pública y activa **Auto-assign public IP**. Permite **SSH TCP 22** desde **My IP** y **HTTP TCP 80** para quienes probarán la demostración. Mantén cerrados 8080 y 5432.
5. Lanza la instancia y espera que el estado sea **Running** y que pasen las comprobaciones. Copia la IPv4 pública.

## Instalar el paquete compilado

El paquete lleva `backend.jar`, los archivos compilados de Angular dentro de `frontend/` y los tres archivos de instalación de esta carpeta. Para reconstruirlo, usa Java 21 y una versión de Node compatible con el frontend del equipo.

Desde la raíz del backend ejecuta `.\mvnw.cmd clean package` en PowerShell o `./mvnw clean package` en Linux. El JAR queda en `target/backend.jar`. Desde la raíz del frontend ejecuta `npm ci` y `npm run build`. Copia **el contenido** de `dist/CompraAmigo-Frontend/browser/` a `frontend/` en una carpeta de preparación. Copia a esa misma carpeta el JAR y `install.sh`, `compraamigo.service` y `compraamigo.nginx`. Allí ejecuta `tar -czf CompraAmigo-EC2.tar.gz backend.jar frontend install.sh compraamigo.service compraamigo.nginx`.

Ejecuta en PowerShell desde la carpeta donde guardaste `CompraAmigo-EC2.tar.gz`. Sustituye la IP y la ruta de la llave.

```powershell
$CompraAmigoIp = 'IP_PUBLICA_DE_EC2'
$CompraAmigoPem = 'C:\ruta\a\la-clave.pem'
scp -i $CompraAmigoPem .\CompraAmigo-EC2.tar.gz "ubuntu@${CompraAmigoIp}:~/"
ssh -i $CompraAmigoPem "ubuntu@$CompraAmigoIp"
```

Dentro de Ubuntu:

```bash
mkdir -p ~/compraamigo-release
tar -xzf ~/CompraAmigo-EC2.tar.gz -C ~/compraamigo-release
cd ~/compraamigo-release
sudo bash install.sh IP_PUBLICA_DE_EC2
```

El instalador usa Java 21, Nginx y PostgreSQL. Arranca el backend con una base inicial nueva, carga 14 productos y crea un administrador de demostración. Las contraseñas se generan en la instancia. Desde SSH, `sudo grep '^APP_INIT_ADMIN_' /etc/compraamigo/compraamigo.env` muestra el acceso del administrador al operador de EC2. Puedes indicar un correo distinto como segundo argumento de `install.sh` durante la primera instalación.

## Verificar la sustentación

1. Abre `http://IP_PUBLICA_DE_EC2/api/health`. Debe responder `{"status":"UP"}` si el backend acepta tráfico y PostgreSQL responde.
2. Abre `http://IP_PUBLICA_DE_EC2/`. Crea una cuenta de prueba y verifica que su perfil, carrito y pedidos correspondan al cliente nuevo. Revisa el catálogo y realiza una compra de demostración.
3. Recarga una página de Angular, por ejemplo `/catalogo`, para comprobar que Nginx mantiene el acceso directo a las rutas.
4. Muestra el pedido guardado en PostgreSQL. Desde SSH, ejecuta `sudo -u postgres psql -d db_compraamigo -c 'SELECT count(*) FROM pedidos;'`.
5. Para comprobar que se conservan los datos, reinicia solo el backend con `sudo systemctl restart compraamigo` y vuelve a abrir el historial. Para diagnosticar un arranque, ejecuta `sudo journalctl -u compraamigo -n 60 --no-pager`.

El paquete sirve una demostración por HTTP. Usa cuentas ficticias y contraseñas exclusivas para la demo. El envío del recibo requiere un servidor SMTP y sus variables de configuración. Esta instalación no configura un proveedor de correo real.

Detén la instancia al terminar la presentación para dejar de consumir cómputo. El volumen EBS persiste y puede seguir consumiendo crédito. Si enciendes la instancia de nuevo, revisa la IPv4 pública. Si cambió, vuelve a ejecutar el instalador con la nueva dirección. Conserva la base y las claves de la instalación.
