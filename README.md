# Bitacora_Corte2_JulianGiral
# Código Azteca 🌵🌮

**Código Azteca** es un restaurante de comida mexicana que combina la gastronomía tradicional de México con una identidad inspirada en la tecnología.

El restaurante ofrece diferentes opciones de comida mexicana, incluyendo **tacos, burritos, quesadillas, bebidas y postres**, buscando brindar una experiencia que combine el sabor y la cultura mexicana con una propuesta moderna.

### 🍽️ Funcionalidades principales

* **Menú:** consulta y gestión de los platos disponibles.
* **Pedidos:** creación y gestión de pedidos realizados por los clientes.
* **Reservas:** gestión de reservas de mesas.
* **Clientes:** manejo de la información básica de los clientes.
* **Categorías:** organización de los productos del menú en categorías como tacos, burritos, quesadillas, bebidas y postres.

### 💻 Tecnologías

El proyecto corresponde al desarrollo de un backend para la gestión del restaurante, aplicando principios de diseño y arquitectura vistos en el curso de **Desarrollo Orientado a Servicios (DOSW)**.

**Slogan:** *“Sabor mexicano, tecnología moderna.”*

## Persistencia

Para el proyecto Código Azteca se seleccionó una base de datos
relacional utilizando PostgreSQL junto con JPA/Hibernate.

### Justificación

Se seleccionó PostgreSQL porque el sistema maneja información
estructurada y relacionada, como usuarios, clientes, platos,
categorías y pedidos.

El modelo presenta relaciones claras entre las entidades. Por ejemplo,
un cliente puede realizar varios pedidos, un pedido puede contener
varios platos y cada plato pertenece a una categoría.

El uso de una base de datos relacional permite representar estas
relaciones mediante claves primarias y foráneas, manteniendo la
integridad de los datos.

JPA/Hibernate se utilizará como mecanismo de persistencia para
mapear las entidades Java a las tablas de PostgreSQL y Spring Data
JPA permitirá implementar los repositories para las operaciones
CRUD.

### Tecnología seleccionada

- Base de datos: PostgreSQL
- ORM: JPA / Hibernate
- Framework de persistencia: Spring Data JPA
- Tipo de persistencia: SQL / Relacional
- NoSQL: No se utilizará en esta versión del proyecto.
