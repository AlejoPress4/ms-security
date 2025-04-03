package com.aph.ms_security.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
//Creacion de paquetes, creacion de clases en el paquete de models
public class User {
    //Atributos
    @Id
    private String _id; //se usa porque en mongo se utilizan los identificadores con rayalpiso
    private String name;
    private String email;
    private String password;
    private String passwordResetToken;


    @DBRef
    private Role role;

    public User() {

    };
    //Constructor
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Getters y Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //Establecer la parte de la recuperacion de contraseñas
    //Token es el codigo de recuperacion
    public String getPasswordResetToken() {
        return this.passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    private int loginCount = 0; // Inicializamos el contador en 0

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public void incrementLoginCount() {
        this.loginCount++;
    }
}

