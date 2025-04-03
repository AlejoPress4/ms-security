package com.aph.ms_security.Services;

import com.aph.ms_security.Controllers.UserRole_Controller;
import com.aph.ms_security.Repositories.Permission_Repository;
import com.aph.ms_security.Repositories.RolePermission_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Models.*;
import com.aph.ms_security.Repositories.Permission_Repository;
import com.aph.ms_security.Repositories.RolePermission_Repository;
import com.aph.ms_security.Repositories.User_Repository;
import com.aph.ms_security.Repositories.UserRole_Repository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Service
public class ValidatorsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private Permission_Repository thePermissionRepository;
    @Autowired
    private User_Repository theUserRepository;
    @Autowired
    private RolePermission_Repository theRolePermissionRepository;

    @Autowired
    private UserRole_Repository theUserRoleRepository;

    @Autowired
    private RolePermission_Repository rolePermissionRepository;

    private static final String BEARER_PREFIX = "Bearer ";

    public boolean validationRolePermission(HttpServletRequest request,
            String url,
            String method) {
        boolean success = false;
        User theUser = this.getUser(request);
        if (theUser != null) {
            System.out.println("Antes URL " + url + " metodo " + method);
            url = url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
            System.out.println("URL " + url + " metodo " + method);
            Permission thePermission = this.thePermissionRepository.getPermission(url, method);

            List<UserRole> roles = this.theUserRoleRepository.getRolesByUser(theUser.get_id());
            int i = 0;
            while (i < roles.size() && success == false) {
                UserRole actual = roles.get(i);
                Role theRole = actual.getRole();
                if (theRole != null && thePermission != null) {
                    System.out.println("Rol " + theRole.get_id() + " Permission " + thePermission.get_id());
                    RolePermission theRolePermission = this.theRolePermissionRepository
                            .getRolePermission(theRole.get_id(), thePermission.get_id());
                    if (theRolePermission != null) {
                        success = true;
                    }
                } else {
                    success = false;
                }
                i += 1;
            }

        }
        return success;
    }

    public User getUser(final HttpServletRequest request) {
        User theUser = null;
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Header " + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            System.out.println("Bearer Token: " + token);
            User theUserFromToken = jwtService.getUserFromToken(token);
            if (theUserFromToken != null) {
                theUser = this.theUserRepository.findById(theUserFromToken.get_id())
                        .orElse(null);

            }
        }
        return theUser;
    }

    private final Map<String, Map<String, Integer>> permissionUsage = new HashMap<>();

    public void registerPermissionUsage(String roleId, String permissionId) {
        RolePermission rolePermission = rolePermissionRepository.getPermissionsByRole(roleId).stream()
                .filter(rp -> rp.getPermission().get_id().equals(permissionId))
                .findFirst()
                .orElse(null);

        if (rolePermission != null) {
            rolePermission.incrementUsageCount();
            rolePermissionRepository.save(rolePermission);
        }
    }

    public String getMostUsedPermission(String roleId) {
        Map<String, Integer> roleUsage = permissionUsage.get(roleId);
        if (roleUsage == null || roleUsage.isEmpty()) {
            return null;
        }
        return roleUsage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Permission getPermission(String url, String method) {
        return thePermissionRepository.getPermission(url, method);
    }

}
