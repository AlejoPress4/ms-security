package com.aph.ms_security.Repositories;

import com.aph.ms_security.Models.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRole_Repository extends MongoRepository<UserRole,String> {
    @Query("{'user.$id': ObjectId(?0)}")
    public List<UserRole> getRolesByUser(String userId);

    @Query("{'role.$id': ObjectId(?0)}")
    public List<UserRole> getUsersByRole(String roleId);
}
