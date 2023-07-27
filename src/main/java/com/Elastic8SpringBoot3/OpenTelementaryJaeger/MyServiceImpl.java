package com.Elastic8SpringBoot3.OpenTelementaryJaeger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.Elastic8SpringBoot3.service.UserService;

import io.opentelemetry.api.trace.Tracer;

@RestController
@Service
public class MyServiceImpl{

	private final Tracer tracer;
	
	@Autowired
	public UserService userService;

    public MyServiceImpl(Tracer tracer) {
        this.tracer = tracer;
    }    
}
