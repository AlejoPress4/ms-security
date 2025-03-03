package com.aph.ms_security.Repositories;


import com.aph.ms_security.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface User_Repository extends MongoRepository<User,String> {

}

