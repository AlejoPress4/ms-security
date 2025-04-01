package com.aph.ms_security.Controllers;

import com.aph.ms_security.Models.Permission;
import com.aph.ms_security.Models.Session;
import com.aph.ms_security.Models.User;
import com.aph.ms_security.Repositories.Session_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Services.EncryptionService;
import com.aph.ms_security.Services.JwtService;
import com.aph.ms_security.Services.ValidatorsService;
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
    @Autowired
    private ValidatorsService theValidatorsService;
    @Autowired
    private Session_Repository theSessionRepository;

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody User theNewUser, //Recibimos un objeto User en request
                                        final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>(); //Creamos un HashMap para almacenar la respuesta
        String token=""; //Creamos una variable token
        User theActualUser=this.theUserRepository.getUserByEmail(theNewUser.getEmail()); //Verificamos si el usuario existe
        if(theActualUser!=null && //Si el usuario existe
           theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))){ //Si el usuario existe y la contraseña es correcta
            String number = this.generateRandom();
            token=theJwtService.generateToken(theActualUser); //Generamos un token
            Session theSession = new Session(number, theActualUser, token);
            this.theSessionRepository.save(theSession);
            theActualUser.setPassword("");
            theResponse.put("token",token);
            theResponse.put("user",theActualUser);
            return theResponse;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return  theResponse;
        }
    }

    @PostMapping("login/2FA/{idUser}")
    public String secondFactor(@RequestBody Session theNewSession, @PathVariable String idUser, final HttpServletResponse response) throws IOException {

        User theUser = this.theUserRepository.findById(idUser).orElse(null);
        Session theOldSession = this.theSessionRepository.getSession(theUser.get_id(), theNewSession.getCode2FA());

        if (theUser != null && theOldSession != null) {
            System.out.println("entrop 1");
            if (theNewSession.getCode2FA().equals(theOldSession.getCode2FA())) {
                System.out.println("entrop 2");
                String token = theJwtService.generateToken(theUser);
                this.theSessionRepository.save(theOldSession);
                response.setStatus(HttpServletResponse.SC_OK);
                return "message: "+ token;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        return null;
    }

   @PostMapping("permissions-validation")
   public boolean permissionsValidation(final HttpServletRequest request,
                                        @RequestBody Permission thePermission) {
       boolean success=this.theValidatorsService.validationRolePermission(request,thePermission.getUrl(),thePermission.getMethod());
       return success;
   }

    public String generateRandom() {
        int number = (int)(Math.random()*90000+10000);
        return String.valueOf(number);
    }

}
