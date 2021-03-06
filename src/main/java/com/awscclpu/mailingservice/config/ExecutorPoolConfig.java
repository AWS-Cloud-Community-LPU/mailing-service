package com.awscclpu.mailingservice.config;

import com.awscclpu.mailingservice.constants.PropertyConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableScheduling
@EnableAsync
public class ExecutorPoolConfig {

	@Value("${" + PropertyConstants.THREAD_POOL_TASK_EXECUTOR_SIZE + "}")
	private int threadPoolSize;

	@Bean(name = "deactivateOTP_background_pool")
	public ThreadPoolTaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(threadPoolSize);
		executor.setMaxPoolSize(threadPoolSize);
		executor.setThreadNamePrefix("deactivateOTP_background_pool");
		executor.initialize();
		return executor;
	}
}
