package com.example.spring.restclient.resttemplate.customizer;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProxyCustomizer implements RestTemplateCustomizer
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

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    @Override
    public void customize(RestTemplate restTemplate)
    {
        HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort, this.proxyScheme);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(proxy)
        {
            @Override
            public HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException
            {
                // Exclude hosts.
                if (target.getHostName().equals("192.168.0.5"))
                    return null;

                return super.determineProxy(target, request, context);
            }
        });

        if ((this.proxyUsername != null) && (this.proxyPassword != null))
        {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(this.proxyHost, this.proxyPort),
                    new UsernamePasswordCredentials(this.proxyUsername, this.proxyPassword));

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }

        HttpClient httpClient = httpClientBuilder.build();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}
