#Prompts de feign

## Prompt 1: PPedi una breve explicacion de feign y su uso

### Prompt Utilizado:

me podrias explicar que es feign en java y sus estructuras y usos basicos

### Respuesta Recibida:

¡Claro! Vamos paso a paso. 😄

Qué es Feign

Feign es un cliente HTTP declarativo en Java, desarrollado por Netflix, que permite consumir servicios REST de manera muy sencilla.
En lugar de escribir código manual con RestTemplate o HttpClient, con Feign definís interfaces que representan las llamadas a los servicios, y Feign se encarga de hacer la conexión, la serialización y el manejo de errores.

Se usa mucho en microservicios con Spring Cloud, donde un servicio necesita consumir otro servicio REST.

Estructura básica

Dependencia en Maven:

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

Habilitar Feign en tu aplicación:

@SpringBootApplication
@EnableFeignClients
public class MiAplicacion {
public static void main(String[] args) {
SpringApplication.run(MiAplicacion.class, args);
}
}

Definir un cliente Feign (interfaz):

@FeignClient(name = "servicio-productos", url = "http://localhost:8080")
public interface ProductoClient {

    @GetMapping("/productos")
    List<Producto> obtenerTodos();

    @GetMapping("/productos/{id}")
    Producto obtenerPorId(@PathVariable("id") Long id);

    @PostMapping("/productos")
    Producto crearProducto(@RequestBody Producto producto);

    @PutMapping("/productos/{id}")
    Producto actualizarProducto(@PathVariable("id") Long id, @RequestBody Producto producto);

    @DeleteMapping("/productos/{id}")
    void eliminarProducto(@PathVariable("id") Long id);
}

Observa que la interfaz no tiene implementación; 
Feign genera la implementación automáticamente en tiempo de ejecución.

Uso básico
Una vez definida la interfaz, la podés inyectar y usar como cualquier otro bean de Spring:
@Service
public class ProductoService {

    private final ProductoClient productoClient;

    public ProductoService(ProductoClient productoClient) {
        this.productoClient = productoClient;
    }

    public List<Producto> listarProductos() {
        return productoClient.obtenerTodos();
    }

    public Producto crear(Producto p) {
        return productoClient.crearProducto(p);
    }
}
Ventajas de Feign

Código más limpio y declarativo.
Integración automática con Spring Cloud y Eureka para descubrimiento de servicios.
Manejo fácil de errores, timeouts y reintentos si se combina con Ribbon o Resilience4j.
Compatible con anotaciones de Spring (@GetMapping, @PostMapping, etc.), no necesitás reinventar nada.

Estructura resumida

Interfaz con @FeignClient → define endpoints.
Anotaciones de mapeo (@GetMapping, @PostMapping, @PathVariable, @RequestBody) → especifican cómo se llaman los endpoints.
Inyección en un servicio o controlador → se usa como cualquier otro bean.