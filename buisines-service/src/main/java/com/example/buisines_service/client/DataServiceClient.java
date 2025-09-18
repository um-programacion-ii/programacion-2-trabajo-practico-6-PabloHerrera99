package com.example.buisines_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "data-service", url = "${data.service.url")
public class DataServiceClient {
}
