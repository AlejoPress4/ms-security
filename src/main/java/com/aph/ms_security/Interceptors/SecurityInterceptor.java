package com.aph.ms_security.Interceptors;

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
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        //boolean success=this.validatorService.validationRolePermission(request,request.getRequestURI(),request.getMethod());
        // Validar la nueva ruta
        if (requestURI.equals("/role-permission/most-used") && method.equals("GET")) {
            return true; // Permitir acceso a esta ruta
        }

        // Otras validaciones...
        return this.validatorService.validationRolePermission(request, requestURI, method);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Lógica a ejecutar después de que se haya manejado la solicitud por el controlador
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de la vista
    }


}
