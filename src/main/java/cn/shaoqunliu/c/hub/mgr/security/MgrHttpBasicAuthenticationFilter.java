package cn.shaoqunliu.c.hub.mgr.security;

import cn.shaoqunliu.c.hub.mgr.security.details.MgrAccessDetails;
import cn.shaoqunliu.c.hub.mgr.security.token.MgrAuthenticationToken;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class MgrHttpBasicAuthenticationFilter extends BasicAuthenticationFilter {

    MgrHttpBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String auth = request.getHeader("Authorization");
            String uri = request.getRequestURI();
            HttpMethod method = HttpMethod.resolve(request.getMethod());
            Claims claims;
            Object accessMap;
            if (auth == null || !uri.startsWith("/v1/") ||
                    (claims = JwtsUtils.getClaimsFromToken(auth)) == null ||
                    !((accessMap = claims.get("access")) instanceof Map)) {
                chain.doFilter(request, response);
                return;
            }
            @SuppressWarnings("unchecked")
            MgrAccessDetails accessDetails = new JSONObject((Map) accessMap)
                    .toJavaObject(MgrAccessDetails.class);
            // check if the user has enough permissions to access the required resources
            MgrAuthenticationToken authenticationToken =
                    new MgrAuthenticationToken(uri, method, accessDetails, request.getParameterMap());
            Authentication authResult = getAuthenticationManager()
                    .authenticate(authenticationToken);
            Objects.requireNonNull(authResult);
            // has the required permission
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            // fail commence
            return;
        }
        chain.doFilter(request, response);
    }
}
