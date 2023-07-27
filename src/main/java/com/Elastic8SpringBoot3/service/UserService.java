package com.Elastic8SpringBoot3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Elastic8SpringBoot3.Model.User;
import com.Elastic8SpringBoot3.Repository.UserRepository;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
    private Tracer tracer;
	
	public Iterable<User> listAll() {

		Span span = tracer.spanBuilder("listAll").startSpan();
		try {
			return (List<User>) userRepository.findAll(PageRequest.of(0, 100)).toList();	
		}
		finally {
            span.end();
        }
		
		
	}

	public User save(User user) {
		Span span = tracer.spanBuilder("save").startSpan();
		try {
			return userRepository.save(user);
		}
		finally {
			span.end();
		}
	}

}
