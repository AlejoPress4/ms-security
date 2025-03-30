package com.aph.ms_security.Repositories;


import com.aph.ms_security.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface User_Repository extends MongoRepository<User,String> {
    @Query("{'email': ?0}")
    public User getUserByEmail(String email);
    //Buscar un usario por este metodo de cola en mongo

}

