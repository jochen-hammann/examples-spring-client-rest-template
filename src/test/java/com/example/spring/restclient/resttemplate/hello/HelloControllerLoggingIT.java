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
class HelloControllerLoggingIT
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplateBasicLogging;

    @Autowired
    private RestTemplate restTemplateApacheHttpClientLogging;

    @Autowired
    private RestTemplate restTemplateInterceptorLogging;

    // ============================== [Unit Tests] ==============================

    // -------------------- [Test Helper Classes] --------------------

    // -------------------- [Test Helper Methods] --------------------

    private void dump(Hello hello) throws JsonProcessingException
    {
        String jsonStr = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(hello);
        System.out.println(jsonStr);
    }

    // -------------------- [Test Initialization] --------------------

    // -------------------- [Tests] --------------------

    @Test
    void getBasicLogging() throws JsonProcessingException
    {
        // Requires:
        //   logging.level.org.springframework.web.client.RestTemplate=DEBUG

        ResponseEntity<Hello> response = this.restTemplateBasicLogging.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }

    @Test
    void getApacheHttpClientLogging() throws JsonProcessingException
    {
        // Requires:
        //   logging.level.org.apache.http=DEBUG
        //   logging.level.httpclient.wire=DEBUG

        ResponseEntity<Hello> response = this.restTemplateApacheHttpClientLogging.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }

    @Test
    void getInterceptorLogging() throws JsonProcessingException
    {
        // Requires:
        //   logging.level.com.example.spring.restclient.resttemplate.interceptor.LoggingInterceptor=DEBUG
        //   logging.level.com.example.spring.restclient.resttemplate.config.RestClientConfig=DEBUG

        ResponseEntity<Hello> response = this.restTemplateInterceptorLogging.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }
}
