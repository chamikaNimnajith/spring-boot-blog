package com.chamika.blog.security;

import com.chamika.blog.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor  //Lombok annotation that generates a constructor for final fields
@Slf4j  // will allow log messages
//for every request from the frontend this filter will run
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try{
        String token = extractToken(request);
        if(token != null){
            UserDetails userDetails = authenticationService.validateToken(token);
            //Creates Spring Security authentication object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // credentials (null since we use token)
                    userDetails.getAuthorities()
            );

            //Stores the authentication in Spring Security's context
            //Makes the user "logged in" for this request
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //Useful for controller methods to access user ID
            if(userDetails instanceof BlogUserDetails){
                request.setAttribute("userId",((BlogUserDetails) userDetails).getId());
            }
        }
        }catch(Exception ex){
            // Do not throw exception , just don't authenticate the user
            log.warn("Received invalid auth Token");
        }

        filterChain.doFilter(request,response);
    }

    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);  //1234567
        }
        return null;
    }
}
