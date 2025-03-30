//package com.aph.ms_security.Controllers;
//
//import com.aph.ms_security.Models.Permission;
//import com.aph.ms_security.Models.Role;
//import com.aph.ms_security.Models.RolePermission;
//import com.aph.ms_security.Repositories.Permission_Repository;
//import com.aph.ms_security.Repositories.RolePermission_Repository;
//import com.aph.ms_security.Repositories.Role_Repository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/rolePermissions")
//public class RolePermission_Controller {
//
//    @Autowired
//    @CrossOrigin
//    @RestController
//    @RequestMapping("/role-permission")
//    public class RolePermissionController {
//        @Autowired
//        private RolePermission_Repository theRolePermissionRepository;
//        @Autowired
//        private Permission_Repository thePermissionRepository;
//        @Autowired
//        private Role_Repository theRoleRepository;
//
//        @ResponseStatus(HttpStatus.CREATED)
//        @PostMapping("role/{roleId}/permission/{permissionId}")
//        public RolePermission create(@PathVariable String roleId,
//                                     @PathVariable String permissionId){
//            Role theRole=this.theRoleRepository.findById(roleId)
//                    .orElse(null);
//            Permission thePermission=this.thePermissionRepository.findById((permissionId))
//                    .orElse(null);
//            if(theRole!=null && thePermission!=null){
//                RolePermission newRolePermission=new RolePermission();
//                newRolePermission.setRole(theRole);
//                newRolePermission.setPermission(thePermission);
//                return this.theRolePermissionRepository.save(newRolePermission);
//            }else{
//                return null;
//            }
//        }
//        @ResponseStatus(HttpStatus.NO_CONTENT)
//        @DeleteMapping("{id}")
//        public void delete(@PathVariable String id) {
//            RolePermission theRolePermission = this.theRolePermissionRepository
//                    .findById(id)
//                    .orElse(null);
//            if (theRolePermission != null) {
//                this.theRolePermissionRepository.delete(theRolePermission);
//            }
//        }
//        @GetMapping("role/{roleId}")
//        public List<RolePermission> findPermissionsByRole(@PathVariable String roleId){
//            return this.theRolePermissionRepository.getPermissionsByRole(roleId);
//        }
//
//    }
//    private RolePermission_Repository theRolePermissionRepository;
//
//    @GetMapping("")
//    public List<RolePermission> find() {
//        return this.theRolePermissionRepository.findAll();
//    }
//
//    @GetMapping("{id}")
//    public RolePermission findById(@PathVariable String id) {
//        return this.theRolePermissionRepository.findById(id).orElse(null);
//    }
//
//    @PostMapping
//    public RolePermission create(@RequestBody RolePermission newRolePermission) {
//        return this.theRolePermissionRepository.save(newRolePermission);
//    }
//
//    @PutMapping("{id}")
//    public RolePermission update(@PathVariable String id, @RequestBody RolePermission newRolePermission) {
//        RolePermission actualRolePermission = this.theRolePermissionRepository.findById(id).orElse(null);
//        if (actualRolePermission != null) {
//            // Update fields here if there are any
//            return this.theRolePermissionRepository.save(actualRolePermission);
//        } else {
//            return null;
//        }
//    }
//
//    @DeleteMapping("{id}")
//    public void delete(@PathVariable String id) {
//        RolePermission theRolePermission = this.theRolePermissionRepository.findById(id).orElse(null);
//        if (theRolePermission != null) {
//            this.theRolePermissionRepository.delete(theRolePermission);
//        }
//    }
//}