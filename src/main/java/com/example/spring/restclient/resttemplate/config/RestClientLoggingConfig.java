package com.example.spring.restclient.resttemplate.config;

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
public class RestClientLoggingConfig
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    static Logger LOGGER = LoggerFactory.getLogger(RestClientLoggingConfig.class);

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Beans] ==============================

    // -------------------- [Private Beans] --------------------

    // -------------------- [Public Beans] --------------------

    @Bean
    public RestTemplate restTemplateBasicLogging()
    {
        // Requires:
        //   logging.level.org.springframework.web.client.RestTemplate=DEBUG

        // Caution: If the RestTemplateBuilder does not specify a ClientHttpRequestFactory, it tries to detect one automatically by inspecting the
        //          classpath. For example, if the Apache HttpComponents HttpClient is specified in the Maven POM, it will use the Apache HttpClient.
        //          Thus, we specify the SimpleClientHttpRequestFactory explicitly.
        //          The default constructor of the RestTemplate uses the SimpleClientHttpRequestFactory.

        return new RestTemplateBuilder().requestFactory(SimpleClientHttpRequestFactory.class).build();
    }

    @Bean
    public RestTemplate restTemplateApacheHttpClientLogging()
    {
        // Requires:
        //   logging.level.org.apache.http=DEBUG
        //   logging.level.httpclient.wire=DEBUG

        return new RestTemplateBuilder().requestFactory(HttpComponentsClientHttpRequestFactory.class).build();
    }

    @Bean
    public RestTemplate restTemplateInterceptorLogging()
    {
        // Requires:
        //   logging.level.com.example.spring.restclient.resttemplate.interceptor.LoggingInterceptor=DEBUG
        //   logging.level.com.example.spring.restclient.resttemplate.config.RestClientConfig=DEBUG

        RestTemplate restTemplate = null;

        if (LOGGER.isDebugEnabled())
        {
            ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
            restTemplate = new RestTemplate(factory);

            List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
            if (CollectionUtils.isEmpty(interceptors))
                interceptors = new ArrayList<>();

            interceptors.add(new LoggingInterceptor());

            restTemplate.setInterceptors(interceptors);
        }
        else
            restTemplate = new RestTemplate();

        return restTemplate;
    }

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

}
