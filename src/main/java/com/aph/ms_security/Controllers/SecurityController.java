package com.aph.ms_security.Controllers;

import com.aph.ms_security.Models.Permission;
import com.aph.ms_security.Models.User;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Services.EncryptionService;
import com.aph.ms_security.Services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {
    @Autowired
    private User_Repository theUserRepository;
    @Autowired
    private EncryptionService theEncryptionService;
    @Autowired
    private JwtService theJwtService;

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody User theNewUser, //Recibimos un objeto User en request
                                        final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>(); //Creamos un HashMap para almacenar la respuesta
        String token=""; //Creamos una variable token
        User theActualUser=this.theUserRepository.getUserByEmail(theNewUser.getEmail()); //Verificamos si el usuario existe
        if(theActualUser!=null && //Si el usuario existe
           theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))){ //Si el usuario existe y la contraseña es correcta
            token=theJwtService.generateToken(theActualUser); //Generamos un token
            theActualUser.setPassword("");
            theResponse.put("token",token);
            theResponse.put("user",theActualUser);
            return theResponse;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return  theResponse;
        }
    }
//    @PostMapping("permissions-validation")
//    public boolean permissionsValidation(final HttpServletRequest request,
//                                         @RequestBody Permission thePermission) {
//        boolean success=this.theValidatorsService.validationRolePermission(request,thePermission.getUrl(),thePermission.getMethod());
//        return success;
//    }

}
