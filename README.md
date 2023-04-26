# MLProductsInformation
Aplicación que permite buscar y obtener detalle sobre productos utilizando la API pública de MercadoLibre.

## Estructura del Proyecto

La aplicación contiene una estructura por paquetes (*Packages*), donde cada uno de ellos representa una funcionalidad (*Feature*) bien específica. Entre los paquetes se incluyen:

- **Search** -  La sección gestiona la palabra utilizada para hacer la búsqueda de producto. En este proyecto, una vez que el usuario presiona el boton de búsqueda, la palabra escrita se guarda en un sistema de almacen de datos (*Preferences*) y queda a disposición de los otros (*features*) para ser usado.
- **Products** - Sección que muestra un listado de productos al consultar a la API de Mercadolibre. Necesita como parámetro de búsqueda la palabra proporcionada por *Search*. El listado indica al usuario la información básica del producto; su imágen referencial, título, precio, cuotas disponibles para pago y si posee envío gratuito o no.
- **Common** - Paquete que contiene modelos de datos y clases que son compartidas entre los *features*.

Dentro de cada paquete se maneja *Clean Architecture*. De ser necesario, se incluye la capa de Presentación (Presentation), Dominio (Domain) y Dato (Data) relacionado a cada funcionalidad en su paquete correspondiente. Con esto tenemos:
- **Domain** -  Contiene el modelado de entidades que pertenecen al universo relacionado al negocio. En este caso incluye a la entidad Producto (*Product*), así como sus *Objetos de Valor* relacionados (*Shipping*, *Installments*,etc.).
- **Data** - Contiene clases que permiten tener acceso a las fuentes de datos. Los modelos de esta capa están directamente relacionados a la fuente y se traducen en modelos de la capa de Dominio mediante el uso de *mapeadores* (Mappers). En este proyecto la fuente de datos se orienta al uso solicitudes REST al API de Mercado Libre y al acceso a las (*Preferences*) del sistema.
- **Presentation**- Encapsula todo lo relacionado a la interfaz de usuario de cada vista de la aplicación. Se hace uso de la estructura de *"Flujo de datos Unidireccional"* donde la UI gatilla un evento que va hasta una fuente de información y esta retorna una respuesta que genera un cambio de estado en la UI.  

- **Testing** - De forma adicional, se incluyen pruebas de integración desarrolladas en el viewModel (ProductViewModel). Se toma en cuenta esta clase ya que es la que concentra mayor funcionalidad, manejo de estados y eventos.

## Librerías utilizadas

- [Navigation Component] : Sirve para enlazar la navegación entre las diferentes secciones de la aplicación. Para esto se implementó una Actividad base del que se crea un contenedor (*Navigation Host*) que sirve para intercambiar los fragmentos de los que se compone cada vista del app.

- [Retrofit] :  Permite la gestión de las conexiones REST entre la aplicación y los servidores de Mercado Libre. A través de su implementación es posible incluir un cliente HTTP para realizar las peticiones ([OkHttp]), interceptores de las peticiones para el manejo de errores, librerías que permiten parsear respuestas JSON que vienen del servicio ([Moshi]), entre otros.

- [Hilt] : Gestiona la inyección de dependencias entre las diferentes secciones que conforman la aplicación.

- [Coroutines]: Framework utilizado para gestionar subprocesos utilizando mecanismos de suspensión de hilos. Ideal para el manejo de operaciones asincronas.

- [Glide] : Permite la gestión y manejo de carga de imágenes para ser mostrados en las diferentes vistas de la aplicación.

[Navigation Component]: <https://developer.android.com/guide/navigation/navigation-getting-started>
[Retrofit]: <https://square.github.io/retrofit/>
[Hilt]: <https://developer.android.com/training/dependency-injection/hilt-android?hl=es-419>
[Coroutines]: <https://developer.android.com/kotlin/coroutines?gclid=Cj0KCQjw2_OWBhDqARIsAAUNTTGzjPPVu6cGHUpfO16QDrBRXXr81ppbUw0gbpMv5EKsBHOnknFdFSwaAremEALw_wcB&gclsrc=aw.ds>
[Glide]: <https://github.com/bumptech/glide>
[OkHttp]: <https://square.github.io/okhttp/>
[Moshi]: <https://github.com/square/moshi>
