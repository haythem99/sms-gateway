package com.grvnc.integrationlib.conf;

import org.springframework.context.annotation.Bean;

import feign.codec.ErrorDecoder;

public class FeignClientConfiguration {

	public FeignClientConfiguration() {
	}

    @Bean
    public FeignClientInterceptor barRequestInterceptor() {
        return new FeignClientInterceptor();
    }
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientCustomErrorDecoder();
    }
}
