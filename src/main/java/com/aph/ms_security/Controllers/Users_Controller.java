package com.aph.ms_security.Controllers;

import com.aph.ms_security.Models.Role;
import com.aph.ms_security.Models.User;
import com.aph.ms_security.Repositories.Role_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/public/users")
public class Users_Controller {

    @Autowired
    private User_Repository theUserRepository;

    @Autowired
    private EncryptionService theEncryptionService;

    @Autowired
    private Role_Repository theRoleRepository;

    @GetMapping("")
    public List<User> find() {
        return this.theUserRepository.findAll();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable String id) {
        return this.theUserRepository.findById(id).orElse(null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User store(@RequestBody User newUser) {
        if (theUserRepository.getUserByEmail(newUser.getEmail()) == null) {
            newUser.setPassword(theEncryptionService.convertSHA256(newUser.getPassword()));//Encriptamos la contraseña
            return this.theUserRepository.save(newUser);
        } //Metodo para que usuario no se pueda volver a registrar
        else {
            return null;
        }
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User newUser) {
        User actualUser = this.theUserRepository.findById(id).orElse(null);
        if (actualUser != null) {
            actualUser.setName(newUser.getName());
            actualUser.setEmail(newUser.getEmail());
            actualUser.setPassword(newUser.getPassword());
            return this.theUserRepository.save(actualUser);
        } else {
            return null;
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        User theUser = this.theUserRepository.findById(id).orElse(null);
        if (theUser != null) {
            this.theUserRepository.delete(theUser);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("{userId}/role/{roleId}")
    public User matchUserRole(@PathVariable String userId, @PathVariable String roleId) {
        User theActualUser = this.theUserRepository.findById(userId).orElse(null);
        Role theActualRole = this.theRoleRepository.findById(roleId).orElse(null);
        if (theActualUser != null && theActualRole != null) {
            theActualUser.setRole(theActualRole);
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

    //Falta delete


}