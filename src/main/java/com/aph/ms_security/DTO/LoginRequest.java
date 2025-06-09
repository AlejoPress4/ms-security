// src/main/java/com/aph/ms_security/DTO/LoginRequest.java
package com.aph.ms_security.DTO;

public class LoginRequest {
    private String email;
    private String password;
    private String captchaToken;

    // Getters y setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCaptchaToken() { return captchaToken; }
    public void setCaptchaToken(String captchaToken) { this.captchaToken = captchaToken; }
}