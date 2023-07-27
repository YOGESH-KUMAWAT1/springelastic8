package com.Elastic8SpringBoot3;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.Elastic8SpringBoot3.OpenTelementaryJaeger.MyServiceImpl;

import io.jaegertracing.Configuration;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.opentracing.Tracer;

@EnableElasticsearchRepositories
@SpringBootApplication
public class ElasticwithSpringBoot3Application {

	public static void main(String[] args) throws IOException {
		
//		SpringApplication.run(ElasticwithSpringBoot3Application.class, args);
		
		ConfigurableApplicationContext context = SpringApplication.run(ElasticwithSpringBoot3Application.class, args);

//       MyServiceImpl tracer = context.getBean(MyServiceImpl.class);
	}
	@Bean
    public MeterRegistry meterRegistry() {
        PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        return meterRegistry;
    }
}