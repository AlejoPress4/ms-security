package com.aph.ms_security.DTO;

public class PasswordRecoveryRequest {
    private String email;
    private String captchaToken;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCaptchaToken() { return captchaToken; }
    public void setCaptchaToken(String captchaToken) { this.captchaToken = captchaToken; }
}