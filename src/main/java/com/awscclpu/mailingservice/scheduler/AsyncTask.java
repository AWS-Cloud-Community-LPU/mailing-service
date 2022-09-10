package com.awscclpu.mailingservice.scheduler;

import com.awscclpu.mailingservice.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableAsync
public class AsyncTask {
	private final CacheService cacheService;

	public AsyncTask(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	@Async("executorBackgroundPool")
	public void populateCache() {
		long initPopulateCacheTime = System.currentTimeMillis();
		log.info("Starting Scheduled job for Populating Cache");
		cacheService.evictTemplatesCache();
		cacheService.templatesList();
		log.info("Scheduled job for Populating Cache Job took: " + (System.currentTimeMillis() - initPopulateCacheTime) + "ms");
	}
}
