package com.aph.ms_security.Controllers;

import com.aph.ms_security.Models.Permission;
import com.aph.ms_security.Models.Role;
import com.aph.ms_security.Models.RolePermission;
import com.aph.ms_security.Repositories.Permission_Repository;
import com.aph.ms_security.Repositories.RolePermission_Repository;
import com.aph.ms_security.Repositories.Role_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.aph.ms_security.Services.ValidatorsService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("role-permission")
public class RolePermission_Controller {
    @Autowired
    private RolePermission_Repository theRolePermissionRepository;
    @Autowired
    private Permission_Repository thePermissionRepository;
    @Autowired
    private Role_Repository theRoleRepository;
    @Autowired
    private ValidatorsService theValidatorService;

    @GetMapping("")
    public List<RolePermission> findAll() {
        return this.theRolePermissionRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("role/{roleId}/permission/{permissionId}")
    public RolePermission create(@PathVariable String roleId,
            @PathVariable String permissionId) {
        Role theRole = this.theRoleRepository.findById(roleId)
                .orElse(null);
        Permission thePermission = this.thePermissionRepository.findById((permissionId))
                .orElse(null);

        if (theRole == null) {
            System.out.println("Role no encontrado");
        }
        if (thePermission == null) {
            System.out.println("Permission no encontrado");
        }

        if (theRole != null && thePermission != null) {
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setRole(theRole);
            newRolePermission.setPermission(thePermission);
            return this.theRolePermissionRepository.save(newRolePermission);
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        RolePermission theRolePermission = this.theRolePermissionRepository
                .findById(id)
                .orElse(null);
        if (theRolePermission != null) {
            this.theRolePermissionRepository.delete(theRolePermission);
        }
    }

    @GetMapping("role/{roleId}")
    public List<RolePermission> findPermissionsByRole(@PathVariable String roleId) {
        return this.theRolePermissionRepository.getPermissionsByRole(roleId);
    }

    @GetMapping("/most-used/{roleId}")
    public String getMostUsedPermissionByRole(@PathVariable String roleId) {
        RolePermission mostUsedRolePermission = this.theRolePermissionRepository.findMostUsedPermissionByRole(roleId);
        if (mostUsedRolePermission == null) {
            return "No se encontraron permisos utilizados para este rol.";
        }
        Permission mostUsedPermission = mostUsedRolePermission.getPermission();
        return mostUsedPermission != null ? mostUsedPermission.getUrl() + " (" + mostUsedPermission.getMethod() + ")"
                : "Permiso no encontrado.";
    }

}