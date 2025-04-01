package com.aph.ms_security.Services;

import com.aph.ms_security.Models.User;


import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** This class represents a service for sending notifications. */
@Service
public class NotificationService {
    @Value("${ms.notifications.url}")
    private String notificationsUrl;
    @Value("${ms.security.url}")
    private String SecurityUrl;

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    /**
     * Sends a notification to the specified URL with the given body.
     *
     * @param url the URL to send the notification to
     * @param body the body of the notification
     */
    public boolean send(String url, HashMap<String, String> body) {
        headers.set("Content-Type", "application/json");
        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println(response.getBody());
            return true;
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sends the generated code to the specified user's email.
     *
     * @param user the user to send the code to
     * @param code the generated code
     */
    public boolean sendCodeByEmail(User user, String code) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("recipient", user.getEmail());
        body.put("username", user.getName());
        body.put("message", code);

        return send(notificationsUrl+"/send_email", body);
    }
    /**
     * Sends the generated code to the specified user's email.
     * @param user
     * @param code
     * @return
     */
    public boolean sendResetLink( User user, String code){
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("recipient", user.getEmail());
        body.put("message", SecurityUrl+"/reset_password/"+user.get_id()+"/"+code);

        return send(notificationsUrl+"/send_reset_link", body);


    }

    // /**
    //  * Sends the generated code to the specified user's email.
    //  *
    //  * @param user the user to send the code to
    //  * @param newPassword the generated code
    //  */
    // public boolean sendPasswordResetEmail(User user, String newPassword) {
    //   var body = new HashMap<String, String>();
    //   body.put("email", user.getEmail());
    //   body.put("username", user.getName());
    //   body.put("newPassword", newPassword);
    //   body.put("resetUrl", passwordResetUrl);

    //   return send(passwordUrl, body);
    // }
}
