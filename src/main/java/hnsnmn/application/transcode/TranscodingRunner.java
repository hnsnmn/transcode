package hnsnmn.application.transcode;

import hnsnmn.domain.job.Job;
import hnsnmn.domain.job.JobRepository;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오후 2:39
* To change this template use File | Settings | File Templates.
*/
public class TranscodingRunner {
	private final TranscodingService transcodingService;
	private final JobRepository jobRepository;

	public TranscodingRunner(TranscodingService transcodingService, JobRepository jobRepository) {
		this.transcodingService = transcodingService;
		this.jobRepository = jobRepository;
	}

	public void run() {
		Job job = getNextJob();
		runTranscoding(job);
	}

	private Job getNextJob() {
		return jobRepository.findEldestJobOfCreatedState();
	}

	private void runTranscoding(Job job) {
		if (job == null) {
			return;
		}
		transcodingService.transcode(job.getId());
	}
}
