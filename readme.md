# MsSecurityApplication

Este proyecto es una aplicación Spring Boot que gestiona los permisos de roles para acceder a diferentes endpoints de una API.

## Componentes

### Configuraciones

-   **WebConfig.java**: Configura la aplicación web, incluyendo el registro de interceptores. Agrega el `SecurityInterceptor` para interceptar solicitudes a rutas `/api/**`, excluyendo `/api/public/**`.

### Controladores

-   **RolePermission_Controller.java**: Maneja las solicitudes HTTP relacionadas con las entidades `RolePermission`, incluyendo operaciones CRUD. Este controlador gestiona la creación, recuperación, actualización y eliminación de permisos de roles.

### Interceptores

-   **SecurityInterceptor.java**: Intercepta las solicitudes HTTP para validar permisos basados en la URL y el método de la solicitud. Utiliza el `ValidatorsService` para verificar si la solicitud tiene los permisos necesarios.

### Modelos

-   **RolePermission.java**: Representa la entidad `RolePermission` en la aplicación. Este modelo define la estructura de los datos de permisos de roles almacenados en la base de datos.

### Repositorios

-   **RolePermission_Repository.java**: Proporciona operaciones CRUD para las entidades `RolePermission` utilizando MongoDB. Incluye consultas personalizadas para obtener permisos basados en la URL y el método.

### Servicios

-   **ValidatorsService.java**: Contiene la lógica de negocio para validar los permisos de roles. Este servicio es utilizado por el `SecurityInterceptor` para validar las solicitudes entrantes.

### Aplicación Principal

-   **MsSecurityApplication.java**: El punto de entrada principal de la aplicación Spring Boot. Contiene el método `main` para ejecutar la aplicación.

### Recursos

-   **application.properties**: Archivo de configuración para la aplicación Spring Boot. Incluye varias configuraciones y propiedades necesarias para que la aplicación se ejecute.

## Cómo Empezar

Para ejecutar la aplicación, utiliza el siguiente comando:


## Proceso condiciones
(si es para crear una nueva entidad debemos crear el modelo y el repo)
Asignar los nuevos atributos o crear nueva entidad
Geter y seters (posiblemente modificacion en el repo (database))
crear nueva ruta en el controlador (metodo)



si necesitamos que necesitamos algo con la ruta debemos mirar el validador

```bash
mvn spring-boot:run



La aplicación se iniciará en `http://localhost:8081`.

## Endpoints

- **GET /rolePermissions**: Recuperar todos los permisos de roles.
- **GET /rolePermissions/{id}**: Recuperar un permiso de rol específico por ID.
- **POST /rolePermissions**: Crear un nuevo permiso de rol.
- **PUT /rolePermissions/{id}**: Actualizar un permiso de rol existente por ID.
- **DELETE /rolePermissions/{id}**: Eliminar un permiso de rol por ID.

## Dependencias

- Spring Boot
- Spring Data MongoDB
- Maven
````
# código de RolePermissionRepository:
```bash
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public interface RolePermissionRepository extends MongoRepository<RolePermission, String> {
    
    List<RolePermission> findAll(); // Obtener todas las relaciones

    Map<String, Integer> endpointCallCounter = new HashMap<>(); // Almacena las veces que se llama a cada endpoint

    default String findMostUsedPermissionUrlForRole(String roleId) {
        List<RolePermission> rolePermissions = findAll().stream()
                .filter(rp -> rp.getRole().get_id().equals(roleId)) // Filtra por el rol recibido
                .collect(Collectors.toList());

        if (rolePermissions.isEmpty()) {
            return null;
        }

        // Contar las veces que se usa cada permiso
        Map<String, Long> permissionCount = rolePermissions.stream()
                .collect(Collectors.groupingBy(
                        rp -> rp.getPermission().getUrl(),
                        Collectors.counting()
                ));

        // Obtener el permiso más usado
        return permissionCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    // Aumentar el contador de llamadas para esta URL
                    endpointCallCounter.put(entry.getKey(), endpointCallCounter.getOrDefault(entry.getKey(), 0) + 1);
                    return entry.getKey();
                })
                .orElse(null);
    }

    default int getCallCountForEndpoint(String url) {
        return endpointCallCounter.getOrDefault(url, 0);
    }
}
````
# Codigo de RolePermissionController:
```bash
import org.springframework.http.ResponseEntity;

@GetMapping("/most-used/{roleId}")
public ResponseEntity<String> getMostUsedPermissionForRole(@PathVariable String roleId) {
    String mostUsedPermission = theRolePermissionRepository.findMostUsedPermissionUrlForRole(roleId);
    
    if (mostUsedPermission == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No permissions found for this role");
    }

    int callCount = theRolePermissionRepository.getCallCountForEndpoint(mostUsedPermission);
    
    return ResponseEntity.ok("Most used endpoint: " + mostUsedPermission + " | Calls: " + callCount);
}
````
# potsman:
http://localhost:8080/api/role-permission/most-used/admin
