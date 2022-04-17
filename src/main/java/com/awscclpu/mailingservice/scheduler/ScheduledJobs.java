package com.awscclpu.mailingservice.scheduler;

import com.awscclpu.mailingservice.constants.PropertyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJobs {

	final AsyncTask asyncTask;

	@Autowired
	public ScheduledJobs(AsyncTask asyncTask) {
		this.asyncTask = asyncTask;
	}

	@Scheduled(fixedRateString = "${" + PropertyConstants.DEACTIVATE_OTP_JOB_RATE_IN_MILLISECONDS + "}")
	public void deactivateOlderOTP() {
		asyncTask.deactivateOTP();
	}
}
