package com.javainuse.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class ConsumerControllerClient {
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	/*@Autowired
	private DiscoveryClient discoveryClient;*/
	
	public void getEmployee() throws RestClientException, IOException {

		/*List<ServiceInstance> instances=discoveryClient.getInstances("employee-producer-app");
		ServiceInstance serviceInstance1=instances.get(0);
		
		String baseUrl1=serviceInstance1.getUri().toString();
		System.out.println("=====baseUrl1=============="+baseUrl1);*/
		
		ServiceInstance serviceInstance=loadBalancer.choose("employee-producer-app");
		
		
		String baseUrl=serviceInstance.getUri().toString();
		System.out.println("=====baseUrl=============="+baseUrl);
		
		baseUrl=baseUrl+"/employee";
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		try{
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);
		}catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println(response.getBody());
	}

	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}