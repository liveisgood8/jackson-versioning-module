package com.springboot.restexample;

import io.github.liveisgood8.jacksonversioning.Version;
import io.github.liveisgood8.jacksonversioning.holder.InheritableThreadLocalVersionHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VersionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String version = request.getHeader("Version-Info");
        InheritableThreadLocalVersionHolder.initialize(Version.fromString(version));

        filterChain.doFilter(request, response);
    }
}
