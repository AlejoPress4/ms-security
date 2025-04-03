package com.aph.ms_security.Repositories;
import com.aph.ms_security.Models.RolePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface RolePermission_Repository  extends MongoRepository<RolePermission, String> {
    @Query("{'role.$id': ObjectId(?0)}")
    List<RolePermission> getPermissionsByRole(String roleId);
    @Query("{'role.$id': ObjectId(?0),'permission.$id': ObjectId(?1)}")
    public RolePermission getRolePermission(String roleId,String permissionId);

    List<RolePermission> findAll(); // Recuperar todas las relaciones

    default String findMostUsedPermissionUrl() {
        List<RolePermission> allRolePermissions = findAll();

        if (allRolePermissions.isEmpty()) {
            return null;
        }

        // Contar la frecuencia de cada permiso
        Map<String, Long> permissionCount = allRolePermissions.stream()
                .collect(Collectors.groupingBy(
                        rp -> rp.getPermission().getUrl(),
                        Collectors.counting()
                ));

        //se puede implementar tambien un
        //.filter(rp -> rp.getPermission().getUrl() != null)
        // para

        // Obtener el permiso más usado
        return permissionCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
