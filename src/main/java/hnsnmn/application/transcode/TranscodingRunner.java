package hnsnmn.application.transcode;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오후 2:39
* To change this template use File | Settings | File Templates.
*/
public class TranscodingRunner {
	private TranscodingService transcodingService;
	private JobQueue jobQueue;

	public TranscodingRunner(TranscodingService transcodingService, JobQueue jobQueue) {
		this.transcodingService = transcodingService;
		this.jobQueue = jobQueue;
	}

	public void run() {
		while (true) {
			Long jobId = null;
			try {
				jobId = getNextWaitingJob();
			} catch (JobQueue.CloseException ex) {
				break;
			}
			runTranscoding(jobId);
		}
	}

	private Long getNextWaitingJob() {
		return jobQueue.nextJobId();
	}

	private void runTranscoding(Long jobId) {
		try {
			transcodingService.transcode(jobId);
		} catch (RuntimeException ex) {

		}
	}
}
