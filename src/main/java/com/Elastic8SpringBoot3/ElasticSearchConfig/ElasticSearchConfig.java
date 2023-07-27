package com.Elastic8SpringBoot3.ElasticSearchConfig;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;



@Configuration
public class ElasticSearchConfig {

	@Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private String port;
    @Value("${server.ssl.key-store}")
    private Resource keyStore;
    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${spring.elasticsearch.rest.password}")
	private String password;
    @Value("${spring.elasticsearch.rest.username}")
    private String username;
    
    @Bean 
    public RestClient elasticsearchRestClient(Tracer tracer)
    {
    	try {
    	    final SSLContext sslContext = SSLContexts.custom()
    	            .loadTrustMaterial(keyStore.getURL(), keyStorePassword.toCharArray())
    	            .build();
    	    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    	       credentialsProvider.setCredentials(AuthScope.ANY,
    	                  new UsernamePasswordCredentials(username, password));
    	       RestClientBuilder builder = RestClient.builder(new HttpHost(host, Integer.parseInt(port), "https"))
    	         .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
    	           @Override
    	           public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
    	             return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setSSLContext(sslContext);
    	               }
    	             });
    	       Span span = tracer.spanBuilder("ElasticsearchSetup").startSpan();
    	       try {
    	       return builder.build();	
    	   }finally {
               span.end();
           }
    	}      catch(Exception e) {
    	    e.printStackTrace();
    	    return null;
    	   }
    	    }
    }
