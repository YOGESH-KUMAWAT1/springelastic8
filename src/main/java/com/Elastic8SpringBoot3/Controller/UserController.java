package com.Elastic8SpringBoot3.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.Elastic8SpringBoot3.Model.User;
import com.Elastic8SpringBoot3.service.UserService;

import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;

@RestController
@RequestMapping("/apidata")
public class UserController {

	private final Tracer tracer;
	
	private final UserService userService;
	private final LongCounter requestCounter;
	private final LongCounter errorCounter;
	
	@Autowired
	public UserController(UserService userService, Tracer tracer, LongCounter requestCounter, LongCounter errorCounter) {
		this.userService = userService;
        this.tracer = tracer;
        this.requestCounter=requestCounter;
        this.errorCounter=errorCounter;
    }
	
	 
//	@Traced(operationName = "myApiMethod")
    @GetMapping("/my-api")
    public String myApiMethod() {
        // Your API logic here
        return "Response from my API";
    }
	

	@GetMapping("/getAllData")
	public Iterable<User> getAllData() {
		Span span = tracer.spanBuilder("getAllData").setSpanKind(SpanKind.SERVER).startSpan();
        try {
        	requestCounter.add(1);
            return userService.listAll();
        }
        catch(Exception e)
        {
        	errorCounter.add(1);
        	throw e;
        }
        finally {
            span.end();
        }
		
	}
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public User createUser(@RequestBody User user) 
	{
	Span span = tracer.spanBuilder("createUser").setSpanKind(SpanKind.SERVER).startSpan();
    try {
    	requestCounter.add(1);
        return userService.save(user);
    }
    catch(Exception e)
    {
    	errorCounter.add(1);
    	throw e;
    }
    finally {
        span.end();
    }
	}
}
