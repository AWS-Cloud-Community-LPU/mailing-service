package com.awscclpu.mailingservice.scheduler;

import com.awscclpu.mailingservice.constant.PropertyConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobs {

	final AsyncTask asyncTask;

	public ScheduledJobs(AsyncTask asyncTask) {
		this.asyncTask = asyncTask;
	}

	@Scheduled(fixedRateString = "${" + PropertyConstants.POPULATE_CACHE_IN_MILLISECONDS + "}")
	public void populateCache() {
		asyncTask.populateCache();
	}
}
