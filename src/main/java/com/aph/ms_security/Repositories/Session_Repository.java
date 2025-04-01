package com.aph.ms_security.Repositories;
import com.aph.ms_security.Models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface Session_Repository extends MongoRepository<Session,String>{
    @Query("{'user.$id':ObjectId(?0), 'code2FA':?1 }")
    Session getSession(String userid, String code2FA);

}
