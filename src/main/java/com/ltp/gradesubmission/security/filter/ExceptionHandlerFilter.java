package com.ltp.gradesubmission.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ltp.gradesubmission.exception.EntityNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        }catch (EntityNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            errorMessage(response, "User not found");
        }catch(JWTVerificationException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorMessage(response, "JWT Not Valid");
        }catch (RuntimeException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            errorMessage(response, "Bad Request");
        }
    }

    private void  errorMessage(HttpServletResponse response, String message) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print("{\"success\":false, \"message\": \""+ message +"\"}");
        out.flush();
    }
}
