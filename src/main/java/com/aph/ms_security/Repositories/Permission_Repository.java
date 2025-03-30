package com.aph.ms_security.Repositories;
import com.aph.ms_security.Models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface Permission_Repository extends MongoRepository<Permission,String> {
    @Query("{'url':?0,'method':?1}")
    Permission getPermission(String url,
                            String method);
}
