package hnsnmn.application.jobqueue;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 9:12
 * To change this template use File | Settings | File Templates.
 */
public class MemoryJobQueueTest {
	@Test
	public void shouldBeFifo() {
		MemoryJobQueue queue = new MemoryJobQueue();
		Long jobId1 = 1L;
		Long jobId2 = 2L;
		queue.add(jobId1);
		queue.add(jobId2);
		Assert.assertEquals(jobId1, queue.nextJobId());
		Assert.assertEquals(jobId2, queue.nextJobId());
	}

}
