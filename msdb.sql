DROP DATABASE plateaudb;
CREATE DATABASE plateaudb;

SET GLOBAL time_zone = '-3:00';

create table Cliente(
id int not null auto_increment,
dni VARCHAR(15) not null,
nombre VARCHAR(50) not null,
correo VARCHAR(50) not null,
activo boolean default(true),
UNIQUE(dni),
primary key(id)
);

create table ClienteNormal(
cliente int not null,
facturacion float default(0.0),
primary key (cliente),
foreign key(cliente) references cliente(id) ON UPDATE CASCADE
);

create table ClienteVIP(
cliente int not null,
descuento int default(0),
antiguedad int default(0),
primary key (cliente),
foreign key(cliente) references cliente(id) ON UPDATE CASCADE
);

create table Sala(
id int not null auto_increment,
nombre VARCHAR(50) not null,
aforo int default(0),
activo boolean default(true),
UNIQUE(nombre),
primary key(id)
);

create table SalaEspecial(
sala int not null,
_3D boolean default(false),
_VO boolean default(false),
primary key (sala),
foreign key(sala) references sala(id) ON UPDATE CASCADE
);

create table SalaEstandar(
sala int not null,
_aseos boolean default(false),
primary key (sala),
foreign key(sala) references sala(id) ON UPDATE CASCADE
);

create table productora(
idProductora int not null auto_increment,
nombre VARCHAR(50) not null,
cif VARCHAR(15) not null,
direccion VARCHAR(50) not null,
telefono int default(0),
activo boolean default(true),
UNIQUE(nombre),
UNIQUE(cif),
primary key(idProductora)
);

create table Pelicula(
id int not null auto_increment,
titulo VARCHAR(50) not null,
genero VARCHAR(50) not null,
director VARCHAR(20) not null,
duracion int default(0),
activo boolean default(true),
UNIQUE(titulo),
primary key(id)
);

create table PeliculaProductora(
idPelicula int not null,
idProductora int not null,
primary key(idPelicula,idProductora)
);

create table Pase(
id int not null auto_increment,
horario datetime not null,
precioActual int default(0),
asientosLibres int default(0),
idPelicula int default(0),
idSala int default(0),
activo boolean default(true),
primary key(id)
);

create table Compra(
id int not null auto_increment,
precioTotal int default(0),
fecha date not null, 
idCliente int default(0),
primary key(id)
);

create table LineaCompra(
id int not null auto_increment,
idCompra int default(0),
idPase int default(0),
nEntradas int default(0),
precio int default(0),
primary key(id),
foreign key(idCompra) references compra(id) ON UPDATE CASCADE,
foreign key(idPase) references pase(id) ON UPDATE CASCADE

);