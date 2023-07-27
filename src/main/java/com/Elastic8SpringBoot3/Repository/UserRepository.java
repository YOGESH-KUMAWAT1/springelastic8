package com.Elastic8SpringBoot3.Repository;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.Elastic8SpringBoot3.Model.User;



@Repository
 public interface UserRepository extends ElasticsearchRepository <User, Integer> {
	
	
	
}
