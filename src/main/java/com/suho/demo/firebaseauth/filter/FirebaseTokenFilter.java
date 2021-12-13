package com.suho.demo.firebaseauth.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.suho.demo.firebaseauth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private final FirebaseAuth firebaseAuth;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, javax.servlet.http.HttpServletResponse response, javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        final FirebaseToken firebaseToken;
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            setUnauthorizedResponse(response, "Invalid Header");
            return;
        }
        final String token = header.substring(7);

        try {
            firebaseToken = firebaseAuth.verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            setUnauthorizedResponse(response, "Invalid Token");
            return;
        }

        try {
            UserDetails user = memberService.loadUserByUsername(firebaseToken.getUid());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (NoSuchElementException | UsernameNotFoundException e) {
            setUnauthorizedResponse(response, "User Not Found");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\""+message+"\"}");
    }

    @Bean
    public FirebaseTokenFilter getFirebaseTokenFilter(FirebaseAuth firebaseAuth, MemberService memberService) {
        return new FirebaseTokenFilter(firebaseAuth, memberService);
    }
}
