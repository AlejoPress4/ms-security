package com.aph.ms_security.Controllers;

import com.aph.ms_security.DTO.LoginRequest;
import com.aph.ms_security.DTO.PasswordRecoveryRequest;
import com.aph.ms_security.Models.Permission;
import com.aph.ms_security.Models.Session;
import com.aph.ms_security.Models.User;
import com.aph.ms_security.Repositories.Session_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {
    @Autowired
    private NotificationService theNotificationService;
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
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody LoginRequest loginRequest,
                                        final HttpServletResponse response) throws IOException {
        HashMap<String,Object> theResponse = new HashMap<>();

        // Validar captcha
        if (!captchaService.verifyCaptcha(loginRequest.getCaptchaToken())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Captcha inválido");
            return theResponse;
        }

        User theActualUser = this.theUserRepository.getUserByEmail(loginRequest.getEmail());
        if (theActualUser != null &&
                theActualUser.getPassword().equals(theEncryptionService.convertSHA256(loginRequest.getPassword()))) {
            String number = this.generateRandom();
            String token = theJwtService.generateToken(theActualUser);
            Session theSession = new Session(number, theActualUser, token);
            this.theSessionRepository.save(theSession);
            theNotificationService.sendCodeByEmail(theActualUser, number);
            theActualUser.setPassword("");
            theResponse.put("user", theActualUser);
            return theResponse;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return theResponse;
        }
    }

    @PostMapping("login/2FA/{idUser}")
    public HashMap<String,Object> secondFactor(@RequestBody Session theNewSession, @PathVariable String idUser, final HttpServletResponse response) throws IOException {

        User theUser = this.theUserRepository.findById(idUser).orElse(null);
        Session theOldSession = this.theSessionRepository.getSession(theUser.get_id(), theNewSession.getCode2FA());
        HashMap<String,Object> theResponse=new HashMap<>();
        if (theUser != null && theOldSession != null) {
            System.out.println("entrop 1");
            if (theNewSession.getCode2FA().equals(theOldSession.getCode2FA())) {
                System.out.println("entrop 2");
                String token = theJwtService.generateToken(theUser);
                this.theSessionRepository.save(theOldSession);
                theResponse.put("token", token);
                response.setStatus(HttpServletResponse.SC_OK);
                return theResponse;
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

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/singout/session/{idSession}")
//    public void closeSession(@PathVariable String idUser, @PathVariable String idSession, final HttpServletResponse response)throws IOException {
//        Session theSession = this.theSessionRepository.findById(idSession).orElse(null);
//        if (theSession != null) {
//            this.theSessionRepository.delete(theSession);
//        }
//    }

//    @PostMapping("/resetpassword/{userId}")
//    public String resetPassword(@PathVariable String userId, final HttpServletResponse response) throws IOException{
//        User theActualUser = this.theUserRepository.findById(userId).orElse(null);
//        if (theActualUser != null){
//            String number = this.generateRandom();
//            theActualUser.setPasswordResetToken(number);
//            this.theUserRepository.save(theActualUser);
//            theNotificationService.sendResetLink(theActualUser, number);
//        }else{
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//            return "message: User not found";
//        }
//        return "message: Reset code sent";
//    }
//
//    @PostMapping("/resetpassword/{userId}/{code}")
//    public String resetPassword(@PathVariable String userId , @PathVariable String code, @RequestBody String password, final HttpServletResponse response) throws IOException{
//        User theActualUser = this.theUserRepository.findById(userId).orElse(null);
//        if (theActualUser.getPasswordResetToken().equals(code)){
//            theActualUser.setPassword(theEncryptionService.convertSHA256(password));
//            theActualUser.setPasswordResetToken("");
//            this.theUserRepository.save(theActualUser);
//            return "message: Password reseted";
//        }
//        return "message: algo salio mal";
//
//    }

    @PostMapping("/resetpassword/email")
    public String resetPasswordByEmail(@RequestBody PasswordRecoveryRequest request, final HttpServletResponse response)
            throws IOException {
        if (!captchaService.verifyCaptcha(request.getCaptchaToken())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Captcha inválido");
            return "message: Captcha inválido";
        }
        User theActualUser = this.theUserRepository.getUserByEmail(request.getEmail());
        if (theActualUser != null) {
            String number = this.generateRandom();
            theActualUser.setPasswordResetToken(number);
            this.theUserRepository.save(theActualUser);
            theNotificationService.sendResetLink(theActualUser, number);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return "message: User not found";
        }
        return "message: Reset code sent";
    }

    @PostMapping("/resetpassword/email/{code}")
    public String resetPasswordByEmailAndCode(@RequestBody HashMap<String, String> body, @PathVariable String code,
                                              final HttpServletResponse response) throws IOException {
        String email = body.get("email");
        String password = body.get("password");
        User theActualUser = this.theUserRepository.getUserByEmail(email);
        if (theActualUser != null && theActualUser.getPasswordResetToken().equals(code)) {
            theActualUser.setPassword(theEncryptionService.convertSHA256(password));
            theActualUser.setPasswordResetToken("");
            this.theUserRepository.save(theActualUser);
            return "message: Password reseted";
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return "message: algo salio mal";
    }

}
