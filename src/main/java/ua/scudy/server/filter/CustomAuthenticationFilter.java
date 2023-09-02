package ua.scudy.server.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.scudy.server.constants.RoleType;
import ua.scudy.server.entity.user.ScudyRole;
import ua.scudy.server.jwt.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        log.info("Authentication try: email = {}, password = {}", email, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String tokenIssuer = request.getRequestURI().toString();
        String access_token = jwtUtils.generateUserAccessToken(user.getUsername(), tokenIssuer, user.getAuthorities());
        String refresh_token = jwtUtils.generateUserRefreshToken(user.getUsername(), tokenIssuer);
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        for(var role : user.getAuthorities()) {
            if(role.getAuthority().equals(RoleType.STUDENT.name())) {
                tokens.put("user_role", RoleType.STUDENT.name());
            }
            if(role.getAuthority().equals(RoleType.TEACHER.name())) {
                tokens.put("user_role", RoleType.TEACHER.name());
            }
        }
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
