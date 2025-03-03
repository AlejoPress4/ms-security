package com.aph.ms_security.Repositories;
import com.aph.ms_security.Models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Permission_Repository extends MongoRepository<Permission,String> {

}
