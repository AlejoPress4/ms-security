package com.aph.ms_security.Interceptors;

import com.aph.ms_security.Models.User;
import com.aph.ms_security.Models.Permission;
import com.aph.ms_security.Services.ValidatorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private ValidatorsService validatorService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
            throws Exception {
        boolean success = this.validatorService.validationRolePermission(request, request.getRequestURI(),
                request.getMethod());
        if (success) {
            // Obtener el usuario y su rol
            User user = validatorService.getUser(request);
            if (user != null && user.getRole() != null) {
                String roleId = user.getRole().get_id();
                String url = request.getRequestURI().replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
                String method = request.getMethod();
                Permission permission = this.validatorService.getPermission(url, method);
                if (permission != null) {
                    // Registrar el uso del permiso
                    validatorService.registerPermissionUsage(roleId, permission.get_id());
                }
            }
        }
        return success;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Lógica a ejecutar después de que se haya manejado la solicitud por el
        // controlador
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // Lógica a ejecutar después de completar la solicitud, incluso después de la
        // renderización de la vista
    }
}
