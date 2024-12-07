package se.ifmo.rest;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class CORSFilter implements Filter {
    private static final String ALLOWED_ORIGINS = "http://localhost:3000";
    private static final String ALLOWED_METHODS = "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD";
    private static final String ALLOWED_HEADERS = "Content-Type, Authorization";
    private static final String ALLOWED_CREDENTIALS = "true";

    private Logger logger = LoggerFactory.getLogger(CORSFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Обработка preflight запроса (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }

        logger.debug("Request method: " + httpRequest.getMethod());
        logger.debug("Request URL: " + httpRequest.getRequestURI());

        httpResponse.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGINS);
        httpResponse.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
        httpResponse.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        httpResponse.setHeader("Access-Control-Allow-Credentials", ALLOWED_CREDENTIALS);

        chain.doFilter(request, httpResponse);
    }

    @Override
    public void destroy() { }
}
