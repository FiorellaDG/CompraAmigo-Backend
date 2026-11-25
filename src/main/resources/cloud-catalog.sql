-- Datos de demostración del prototipo integrado. Ejecutar en db_compraamigo.

INSERT INTO categorias (nombre)
SELECT origen.nombre
FROM (VALUES ('Accesorios'), ('Calzado'), ('Casacas'), ('Pantalones'), ('Polos')) AS origen(nombre)
WHERE NOT EXISTS (SELECT 1 FROM categorias c WHERE c.nombre = origen.nombre);

WITH prototipo (nombre, descripcion, precio, categoria, imagen, co2, tallas) AS (
    VALUES
        ('Casaca Impermeable Azul', 'Casaca impermeable confeccionada con poliéster reciclado. Ligera, cortaviento y con capucha ajustable.', 120, 'Casacas', 'images/productos/casaca-impermeable-azul.jpg', 3.2, 'S,M,L,XL'),
        ('Casaca de Mezclilla Reciclada', 'Clásica casaca de mezclilla hecha con algodón reciclado. Resistente, versátil y con un teñido que gasta menos agua.', 135, 'Casacas', 'images/productos/casaca-mezclilla.jpg', 3.6, 'S,M,L,XL'),
        ('Parka Térmica Verde Oliva', 'Parka abrigadora con relleno reciclado y capucha. Ideal para las noches frías y los días de viento.', 180, 'Casacas', 'images/productos/parka-verde.jpg', 4.1, 'S,M,L,XL'),
        ('Zapatillas Eco-Friendly', 'Zapatillas cómodas para el día a día, hechas con suela de caucho natural y tela de botellas recicladas.', 150, 'Calzado', 'images/productos/zapatillas-eco.jpg', 4.5, '38,39,40,41,42,43'),
        ('Zapatillas Urbanas de Lona', 'Zapatillas de lona de algodón orgánico con suela de caucho. Ligeras y fáciles de combinar.', 110, 'Calzado', 'images/productos/zapatillas-lona.jpg', 3.4, '38,39,40,41,42,43'),
        ('Sandalias de Caucho Reciclado', 'Sandalias frescas y resistentes fabricadas con caucho reciclado. Perfectas para el verano y la playa.', 75, 'Calzado', 'images/productos/sandalias-caucho.jpg', 2.2, '38,39,40,41,42,43'),
        ('Polo de Algodón Orgánico', 'Polo suave y fresco de algodón orgánico certificado, cultivado sin pesticidas y con menor consumo de agua.', 45, 'Polos', 'images/productos/polo-organico.jpg', 2.1, 'S,M,L,XL'),
        ('Polo Básico Blanco', 'El polo básico que no puede faltar. Algodón orgánico de tacto suave y cuello reforzado que no se deforma.', 40, 'Polos', 'images/productos/polo-blanco.jpg', 1.9, 'S,M,L,XL'),
        ('Polo Deportivo Reciclado', 'Polo deportivo transpirable hecho con botellas recicladas. Seca rápido y es ideal para entrenar.', 55, 'Polos', 'images/productos/polo-deportivo.jpg', 2.0, 'S,M,L,XL'),
        ('Pantalón de Cáñamo', 'Pantalón resistente y transpirable de cáñamo mezclado con algodón. Una fibra que requiere muy poca agua.', 95, 'Pantalones', 'images/productos/pantalon-canamo.jpg', 2.8, 'S,M,L,XL'),
        ('Jean de Algodón Orgánico', 'Jean de corte recto con algodón orgánico y un proceso de lavado que ahorra agua. Duradero y cómodo.', 130, 'Pantalones', 'images/productos/jean-organico.jpg', 3.9, 'S,M,L,XL'),
        ('Mochila de Material Reciclado', 'Mochila de 20 litros con compartimento para laptop, fabricada con lona reciclada y cierres duraderos.', 89, 'Accesorios', 'images/productos/mochila-reciclada.jpg', 1.9, 'Única'),
        ('Botella Reutilizable de Acero', 'Botella de acero inoxidable de 750 ml que mantiene el frío por 24 h. Reemplaza cientos de botellas de plástico.', 35, 'Accesorios', 'images/productos/botella-acero.jpg', 0.8, 'Única'),
        ('Bolso Tote de Algodón', 'Bolso tote de algodón grueso para tus compras diarias. Dile adiós a las bolsas plásticas.', 25, 'Accesorios', 'images/productos/tote-algodon.jpg', 0.7, 'Única')
)
INSERT INTO productos (nombre, descripcion, precio, stock, co2, imagen, tallas, categoria_id)
SELECT p.nombre, p.descripcion, p.precio, 10, p.co2, p.imagen, p.tallas,
       (SELECT min(c.id) FROM categorias c WHERE c.nombre = p.categoria)
FROM prototipo p
WHERE NOT EXISTS (SELECT 1 FROM productos actual WHERE actual.nombre = p.nombre);
