package com.aph.ms_security.Repositories;
import com.aph.ms_security.Models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Role_Repository extends MongoRepository<Role,String> {
}
