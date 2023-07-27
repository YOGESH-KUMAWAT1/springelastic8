package com.Elastic8SpringBoot3.OpenTelementaryJaeger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;


@Configuration
public class OpenTelemetryConfig {
	@Bean
	public OpenTelemetry openTelemetry() {
            Resource resource = Resource.getDefault()
  .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "jaegerTraces")));
            
            OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                    .setEndpoint("http://192.168.1.83:4317/")
                    .build();
//            OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
//                    .setEndpoint("http://localhost:9090") // Replace with your Prometheus server endpoint
//                    .build();

SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
  .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
  .setResource(resource)
  .build();

//SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//  .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
//  .setResource(resource)
//  .build();

OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
  .setTracerProvider(sdkTracerProvider)
//  .setMeterProvider(sdkMeterProvider)
  .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
  .buildAndRegisterGlobal();
		return openTelemetry;
            
	}
//	@Bean
//    public MeterRegistry meterRegistry() {
//        PrometheusMeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
//        return meterRegistry;
//    }
	@Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        MeterProvider meterProvider = openTelemetry.getMeterProvider();
        return meterProvider.get("jaeger");
    }
	@Bean
    public LongCounter requestCounter(Meter meter) {
        return meter.counterBuilder("requests")
                .setDescription("Total number of requests")
                .setUnit("1")
                .build();
    }
	@Bean
    public LongCounter errorCounter(Meter meter) {
        return meter.counterBuilder("errors")
                .setDescription("Total number of errors")
                .setUnit("1")
                .build();
    }
//	@Bean
//    public io.jaegertracing.Configuration jaegerConfiguration() {
//        io.jaegertracing.Configuration.SamplerConfiguration samplerConfig =
//                io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
//                        .withType("const")
//                        .withParam(1);
//
//        io.jaegertracing.Configuration.ReporterConfiguration reporterConfig =
//                io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
//                        .withLogSpans(true);
//
//        io.jaegertracing.Configuration config =
//                new io.jaegertracing.Configuration("jaeger1")
//                        .withSampler(samplerConfig)
//                        .withReporter(reporterConfig);
//
//        return config;
//    }
//	@Bean
//    public io.opentracing.Tracer jaegerTracer() {
//        return jaegerConfiguration().getTracer();
//    }
    }
