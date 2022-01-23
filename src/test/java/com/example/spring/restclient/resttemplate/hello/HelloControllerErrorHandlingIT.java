package com.example.spring.restclient.resttemplate.hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerErrorHandlingIT
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplateErrorHandling;

    @Autowired
    private RestTemplate restTemplateApacheHttpClientErrorHandling;

    // ============================== [Unit Tests] ==============================

    // -------------------- [Test Helper Classes] --------------------

    // -------------------- [Test Helper Methods] --------------------

    // -------------------- [Test Initialization] --------------------

    // -------------------- [Tests] --------------------

    @Test
    void getErrorHandling() throws JsonProcessingException
    {
        ResponseEntity<Hello> response = this.restTemplateErrorHandling.getForEntity("http://localhost:{port}/hello/error/400", Hello.class, port);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = this.restTemplateErrorHandling.getForEntity("http://localhost:{port}/hello/error/500", Hello.class, port);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getErrorApacheHttpClientHandling() throws JsonProcessingException
    {
        ResponseEntity<Hello> response = this.restTemplateApacheHttpClientErrorHandling.getForEntity("http://localhost:{port}/hello/error/400", Hello.class,
                port);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = this.restTemplateApacheHttpClientErrorHandling.getForEntity("http://localhost:{port}/hello/error/500", Hello.class, port);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
