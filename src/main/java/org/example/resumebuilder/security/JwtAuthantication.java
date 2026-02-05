package org.example.resumebuilder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.resumebuilder.document.User;
import org.example.resumebuilder.repositery.UserRepositry;
import org.example.resumebuilder.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthantication extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepositry userRepositry;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

        if (authHeader!= null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {
                userId= jwtUtil.getUserIdFromToken(token);

            } catch (Exception e) {
                log.error("Token is not valid/available");
            }
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token)){
                 User user =   userRepositry.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));
                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,null, new ArrayList<>());
                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            } catch (Exception e) {
                log.error("JWT validation failed: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request,response);


    }
}
