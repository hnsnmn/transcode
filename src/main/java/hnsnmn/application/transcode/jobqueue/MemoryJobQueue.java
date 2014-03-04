package hnsnmn.application.transcode.jobqueue;

import hnsnmn.application.transcode.JobQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 27.
* Time: 오후 9:49
* To change this template use File | Settings | File Templates.
*/
public class MemoryJobQueue implements JobQueue {
	private BlockingQueue<Long> queue = new LinkedBlockingDeque<Long>();

	@Override
	public Long nextJobId() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("Blocking Queue interrupted");
		}
	}

	@Override
	public void add(Long jobId) {
		queue.add(jobId);
	}
}
