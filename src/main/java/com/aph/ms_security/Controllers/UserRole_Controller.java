package com.aph.ms_security.Controllers;

import com.aph.ms_security.Repositories.Role_Repository;
import com.aph.ms_security.Repositories.UserRole_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Models.Role;
import com.aph.ms_security.Models.User;
import com.aph.ms_security.Models.UserRole;
import com.aph.ms_security.Repositories.Role_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Repositories.UserRole_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user_role")
public class UserRole_Controller {
    @Autowired
    private User_Repository theUserRepository;

    @Autowired
    private Role_Repository theRoleRepository;

    @Autowired
    private UserRole_Repository theUserRoleRepository;

    @GetMapping("")
    public  List<UserRole>findAll(){
        return this.theUserRoleRepository.findAll();
    }

    @PostMapping("user/{userId}/role/{roleId}")
    public UserRole create(@PathVariable String userId,
                           @PathVariable String roleId){
        User theUser=this.theUserRepository.findById(userId).orElse(null);
        Role theRole=this.theRoleRepository.findById(roleId).orElse(null);
        if(theUser!=null && theRole!=null){
            UserRole newUserRole=new UserRole();
            newUserRole.setUser(theUser);
            newUserRole.setRole(theRole);
            return this.theUserRoleRepository.save(newUserRole);
        }else{
            return null;
        }


    }
    @GetMapping("user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable String userId){
        return this.theUserRoleRepository.getRolesByUser(userId);
    }

    @GetMapping("role/{roleId}")
    public List<UserRole> getUsersByRole(@PathVariable String roleId){
        return this.theUserRoleRepository.getUsersByRole(roleId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        // Busca el UserRole por su ID
        UserRole userRole = this.theUserRoleRepository.findById(id).orElse(null);
        // Verifica si el UserRole existe y lo elimina
        if (userRole != null) {
            this.theUserRoleRepository.delete(userRole);
}
    }


}
