package com.example.spring.restclient.resttemplate.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LoggingInterceptor implements ClientHttpRequestInterceptor
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    static Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution)
            throws IOException
    {
        LOGGER.debug("Request body: {}", new String(bytes, StandardCharsets.UTF_8));

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
        String body = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));

        LOGGER.debug("Response body: {}", body);

        return response;
    }
}
