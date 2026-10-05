INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;

INSERT INTO users(nombre, username, password, role_id)
SELECT 'Cliente Demo','cliente@gmail.com','$2y$12$qDN96.sBmI.57EqGYl47k.5GB2KcvaT3jLJFjCjNE6pCnsJibCmhC', r.id
FROM roles r WHERE r.name='ROLE_USER'
ON CONFLICT (username) DO NOTHING;

INSERT INTO users(nombre, username, password, role_id)
SELECT 'Administrador','admin@gmail.com','$2y$12$G7ZehUD4GB8fqBmRqEpx1eoEbfEU90zD52MqrJwuKokM9ZTFfFgMW', r.id
FROM roles r WHERE r.name='ROLE_ADMIN'
ON CONFLICT (username) DO NOTHING;

INSERT INTO categorias (nombre)
SELECT 'Casacas' WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre='Casacas');
INSERT INTO categorias (nombre)
SELECT 'Calzado' WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre='Calzado');
INSERT INTO categorias (nombre)
SELECT 'Polos' WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre='Polos');
INSERT INTO categorias (nombre)
SELECT 'Pantalones' WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre='Pantalones');
INSERT INTO categorias (nombre)
SELECT 'Accesorios' WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre='Accesorios');

INSERT INTO productos (nombre, descripcion, precio, stock, co2, imagen, tallas, categoria_id)
SELECT 'Casaca Impermeable Azul','Casaca impermeable confeccionada con poliester reciclado.',120,18,3.2,'images/casaca.png','S,M,L',c.id
FROM categorias c WHERE c.nombre='Casacas'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Casaca Impermeable Azul');

INSERT INTO productos (nombre, descripcion, precio, stock, co2, imagen, tallas, categoria_id)
SELECT 'Zapatillas Eco-Friendly','Zapatillas fabricadas con materiales reciclados.',150,8,4.5,'images/zapatillas.png','38,39,40,41,42',c.id
FROM categorias c WHERE c.nombre='Calzado'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Zapatillas Eco-Friendly');

INSERT INTO productos (nombre, descripcion, precio, stock, co2, imagen, tallas, categoria_id)
SELECT 'Polo de Algodon Organico','Polo de algodon organico certificado.',45,20,2.1,'images/polo.png','S,M,L,XL',c.id
FROM categorias c WHERE c.nombre='Polos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre='Polo de Algodon Organico');
