# Kas test

Es una aplicación que hace de proxy para hacer peticiones contra el siguiente servicio https://opendata-ajuntament.barcelona.cat/data/api/3/action/package_search

La aplicación se ejecuta en el puerto 8080 y expone un servicio rest para consultar las publicaciones que se encuentran el servicio del ayuntamiento de Barcelona.

# Funcionalidades

## Api version

Actualmente solo se encuentra disponibles la v1. Para acceder a los datos por version se utiliza la siguiente url:
http://localhost:8080/api/v1/publications

## Parámetros

### Paginado

Se han habilitado los parámetros page y rows para realizar consultas paginadas. El servicio traduce estos dos parámetros a los parámetros start (offset) y rows que utiliza la plataforma de ckan del Ayuntamiento.

### Consulta por id

Para consultar un elemento concreto se ha creado un endpoint específico:
http://localhost:8080/api/v1/publications/{id}

El servicio traduce la consulta por id recibida al parámetro q:{id} que utiliza la plataforma de opendata del Ayuntamiento.

### Procesado del idioma

Las urls proporcionadas por el opendata se encuentran diferenciadas por idioma, para ello se ha habilitado el procesado del header Accept-Language.

## HATEOAS

Para el diseño de la api se han seguido diversos principios de HATEOAS. Se han utilizado plurales para la nomenclatura de endpoints y la inclusión de un atributo href que permite la navegación al detalle de una entidad.

# Tecnologías utilizadas

## Spring Webflux

Framework usado para el procesado de las peticiones rest.

## Micrometer

Framework para la generación de métricas de la aplicación. Expone las métricas de la aplicación en la siguiente url

http://localhost:8080/actuator/prometheus

Se ha añadido la librería para que las métricas puedan ser consultadas por la plataforma Prometheus.

## Docker

Se adjunta el Dockerfile para desplegar el jar generado en un contenedor de Docker.