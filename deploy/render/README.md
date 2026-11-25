# CompraAmigo en Render

El servicio web Docker compila Angular desde el commit indicado en el Dockerfile y sirve los archivos junto a la API de Spring Boot. Render entrega una URL HTTPS para ambos. La API se conecta por la red privada a PostgreSQL en la misma región.

1. Inicia sesión en Render con GitHub y abre [Preparar CompraAmigo en Render](https://render.com/deploy?repo=https%3A%2F%2Fgithub.com%2FFiorellaDG%2FCompraAmigo-Backend%2Ftree%2Frender-demo). También puedes elegir **New → Blueprint**, conectar **FiorellaDG/CompraAmigo-Backend** y seleccionar la rama **render-demo**.
2. Elige un workspace **Hobby** sin medio de pago. Revisa que la pantalla muestre dos recursos **Free**. Son el servicio `compraamigo-demo-upc` y la base `compraamigo-demo-db`. Aplica el Blueprint.
3. Cuando el servicio esté **Live**, abre su URL y revisa `/api/health`. El resultado esperado es `{"status":"UP"}`.
4. Crea un cliente de prueba, agrega un producto y confirma una compra. Recarga la pantalla de pedidos para comprobar que quedó guardada en PostgreSQL.

Render genera las credenciales de la base y las de la aplicación. El acceso de administración se configura con `APP_INIT_ADMIN_EMAIL` y `APP_INIT_ADMIN_PASSWORD` en el panel de entorno del servicio.

Para actualizar el frontend, cambia `FRONTEND_COMMIT` por el SHA que probaste y lanza un **Manual Deploy**. La rama usa el plan gratuito para una demo. La base vence a los 30 días y el primer acceso tras la inactividad puede demorar. El correo real requiere un proveedor compatible con los puertos disponibles de Render.
