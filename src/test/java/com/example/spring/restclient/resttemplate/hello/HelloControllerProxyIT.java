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
class HelloControllerProxyIT
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplateSimpleProxy;

    @Autowired
    private RestTemplate restTemplateCustomizerProxyAuthn;

    @Autowired
    private RestTemplate restTemplateApacheHttpClientProxyAuthn;

    @Autowired
    private RestTemplate restTemplateApacheHttpClientUseSystemProperties;

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
    void getSimpleProxy() throws JsonProcessingException
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = localhost
        //   proxy.port = 9999

        ResponseEntity<Hello> response = this.restTemplateSimpleProxy.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }

    @Test
    void getCustomizerProxyAuthn() throws JsonProcessingException
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = localhost
        //   proxy.port = 9999
        //   proxy.username = user
        //   proxy.password = password

        ResponseEntity<Hello> response = this.restTemplateCustomizerProxyAuthn.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }

    @Test
    void getApacheHttpClientProxyAuthn() throws JsonProcessingException
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = localhost
        //   proxy.port = 9999
        //   proxy.username = user
        //   proxy.password = password

        ResponseEntity<Hello> response = this.restTemplateApacheHttpClientProxyAuthn.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }

    @Test
    void getApacheHttpClientUseSystemProperties() throws JsonProcessingException
    {
        // Requires system properties:
        //   http.proxyHost
        //   http.proxyPort
        //   http.nonProxyHosts
        //   https.proxyHost
        //   https.proxyPort

        // The following system properties are not supported by HttpClient 4.5.x, which is used by Spring Boot.
        //   http.proxyUser
        //   http.proxyPassword
        //   https.proxyUser
        //   https.proxyPassword

        ResponseEntity<Hello> response = this.restTemplateApacheHttpClientUseSystemProperties.getForEntity("http://localhost:{port}/hello", Hello.class, port);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        this.dump(response.getBody());
    }

    @Test
    void getSimpleProxy_Squid() throws JsonProcessingException
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = stc-dev-squid
        //   proxy.port = 3128

        ResponseEntity<String> response = this.restTemplateSimpleProxy.getForEntity("http://www.google.de", String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    void getCustomizerProxyAuthn_Squid() throws JsonProcessingException
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = stc-dev-squid
        //   proxy.port = 3128
        //   proxy.username = squiduser
        //   proxy.password = 21admin

        ResponseEntity<String> response = this.restTemplateCustomizerProxyAuthn.getForEntity("http://www.google.de", String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    void getApacheHttpClientProxyAuthn_Squid() throws JsonProcessingException
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = stc-dev-squid
        //   proxy.port = 3128
        //   proxy.username = squiduser
        //   proxy.password = 21admin

        ResponseEntity<String> response = this.restTemplateApacheHttpClientProxyAuthn.getForEntity("http://www.google.de", String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }
}
