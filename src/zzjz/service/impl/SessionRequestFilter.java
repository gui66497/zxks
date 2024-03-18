package zzjz.service.impl;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

/**
 * @author guitang.fang
 * @ClassName: SessionRequestFilter
 * @Description:
 * @version  2016/11/2 16:19
 */
@Provider
public class SessionRequestFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest httpServletRequest;

    /**
     * ContainerRequest过滤器.
     * @param requestContext requestContext
     * @return ContainerRequest
     */
    public ContainerRequest filter(ContainerRequest requestContext) {
        //session
        final HttpSession session = httpServletRequest.getSession();
        requestContext.setSecurityContext(new SecurityContext() {
            public Principal getUserPrincipal() {
                return null;
            }

            public boolean isUserInRole(String s) {
                return false;
            }

            public boolean isSecure() {
                return false;
            }

            public String getAuthenticationScheme() {
                return (String) session.getAttribute("userId");
            }
        });
        return requestContext;
    }
}
