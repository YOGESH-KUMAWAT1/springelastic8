package com.Elastic8SpringBoot3.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;



@Document(indexName = "users")
public class User {

	@Id
	private int id;
	private String firstName;
	private String lastName;
	private String country;
	
	@Autowired
	private final Tracer tracer;

	public User(Integer id, String firstName, String lastName, String country) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		
		this.tracer = GlobalOpenTelemetry.getTracer("jaegerTraces");
	}
	public void performSomeOperation() {
        Span span = tracer.spanBuilder("UserDataOperation").startSpan();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            span.end();
        }
    }
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", country=" + country + "]";
	}

}
