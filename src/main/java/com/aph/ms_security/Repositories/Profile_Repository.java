package com.aph.ms_security.Repositories;


import com.aph.ms_security.Models.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Profile_Repository extends MongoRepository<Profile,String>{

}