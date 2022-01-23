package com.example.spring.restclient.resttemplate.config;

import com.example.spring.restclient.resttemplate.customizer.ProxyCustomizer;
import com.example.spring.restclient.resttemplate.interceptor.LoggingInterceptor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class RestClientProxyConfig
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    @Value("${proxy.scheme}")
    private String proxyScheme;

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private Integer proxyPort;

    @Value("${proxy.username}")
    private String proxyUsername;

    @Value("${proxy.password}")
    private String proxyPassword;

    @Autowired
    private ProxyCustomizer proxyCustomizer;

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Beans] ==============================

    // -------------------- [Private Beans] --------------------

    // -------------------- [Public Beans] --------------------

    @Bean
    public RestTemplate restTemplateSimpleProxy()
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = localhost
        //   proxy.port = 9999

        RestTemplate restTemplate = null;

        if ((this.proxyHost != null) && (this.proxyPort != null))
        {
            Proxy proxy = new Proxy(Proxy.Type.valueOf(this.proxyScheme.toUpperCase(Locale.ROOT)),
                    new InetSocketAddress(this.proxyHost, this.proxyPort));
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setProxy(proxy);

            restTemplate = new RestTemplate(requestFactory);
        }
        else
            restTemplate = new RestTemplate();

        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateCustomizerProxyAuthn()
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = localhost
        //   proxy.port = 9999
        //   proxy.username = user
        //   proxy.password = password

        RestTemplate restTemplate = null;

        if ((this.proxyHost != null) && (this.proxyPort != null))
            restTemplate = new RestTemplateBuilder(proxyCustomizer).build();
        else
            restTemplate = new RestTemplate();

        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateApacheHttpClientProxyAuthn() throws Exception
    {
        // Requires:
        //   proxy.scheme = http
        //   proxy.host = localhost
        //   proxy.port = 9999
        //   proxy.username = user
        //   proxy.password = password

        RestTemplate restTemplate = null;

        if ((this.proxyHost != null) && (this.proxyPort != null))
        {
            HttpHost httpHost = new HttpHost(this.proxyHost, this.proxyPort, this.proxyScheme);
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

            httpClientBuilder.setProxy(httpHost);

            if ((this.proxyUsername != null) && (this.proxyPassword != null))
            {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(this.proxyHost, this.proxyPort),
                        new UsernamePasswordCredentials(this.proxyUsername, this.proxyPassword));

                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).disableCookieManagement();
            }

            HttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            restTemplate = new RestTemplate(requestFactory);
        }
        else
            restTemplate = new RestTemplate();

        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateApacheHttpClientUseSystemProperties() throws Exception
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

        //        System.setProperty("http.proxyHost", "localhost");
        //        System.setProperty("http.proxyPort", "9999");
        //        System.setProperty("http.nonProxyHosts", "");

        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();

        return new RestTemplateBuilder().requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient)).build();
    }

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

}
