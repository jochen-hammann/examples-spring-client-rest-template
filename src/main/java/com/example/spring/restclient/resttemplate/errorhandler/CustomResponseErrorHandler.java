package com.example.spring.restclient.resttemplate.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class CustomResponseErrorHandler implements ResponseErrorHandler
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException
    {
        return (clientHttpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || clientHttpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException
    {
        if (clientHttpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
        {
            log.error("The server error '{}' occurred.", clientHttpResponse.getStatusCode().value());
        }
        else if (clientHttpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR)
        {
            log.error("The client error '{}' occurred.", clientHttpResponse.getStatusCode().value());
        }
    }
}
