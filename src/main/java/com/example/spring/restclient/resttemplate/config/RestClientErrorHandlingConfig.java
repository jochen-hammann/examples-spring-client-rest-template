package com.example.spring.restclient.resttemplate.config;

import com.example.spring.restclient.resttemplate.errorhandler.CustomResponseErrorHandler;
import com.example.spring.restclient.resttemplate.interceptor.LoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientErrorHandlingConfig
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    static Logger LOGGER = LoggerFactory.getLogger(RestClientErrorHandlingConfig.class);

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Beans] ==============================

    // -------------------- [Private Beans] --------------------

    // -------------------- [Public Beans] --------------------

    @Bean
    public RestTemplate restTemplateErrorHandling()
    {
        // Caution: If the RestTemplateBuilder does not specify a ClientHttpRequestFactory, it tries to detect one automatically by inspecting the
        //          classpath. For example, if the Apache HttpComponents HttpClient is specified in the Maven POM, it will use the Apache HttpClient.
        //          Thus, we specify the SimpleClientHttpRequestFactory explicitly.
        //          The default constructor of the RestTemplate uses the SimpleClientHttpRequestFactory.

        return new RestTemplateBuilder().requestFactory(SimpleClientHttpRequestFactory.class).errorHandler(new CustomResponseErrorHandler()).build();
    }

    @Bean
    public RestTemplate restTemplateApacheHttpClientErrorHandling()
    {
        return new RestTemplateBuilder().requestFactory(HttpComponentsClientHttpRequestFactory.class)
                                        .errorHandler(new CustomResponseErrorHandler())
                                        .build();
    }

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

}
