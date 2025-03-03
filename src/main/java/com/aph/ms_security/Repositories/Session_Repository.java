package com.aph.ms_security.Repositories;
import com.aph.ms_security.Models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Session_Repository extends MongoRepository<Session,String>{

}
