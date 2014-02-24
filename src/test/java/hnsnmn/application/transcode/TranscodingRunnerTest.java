package hnsnmn.application.transcode;

import hnsnmn.domain.job.Job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 2:36
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class TranscodingRunnerTest {

	@Mock private TranscodingService transcodingService;
	@Mock private Job job;
	@Mock private JobQueue jobQueue;

	private TranscodingRunner runner;

	@Before
	public void setUp() throws Exception {
		runner = new TranscodingRunner(transcodingService, jobQueue);
	}

	@Test
	public void runTranscodingWhenJobIsExists() {
		when(jobQueue.nextJobId()).thenReturn(1L).thenThrow(
				new JobQueue.CloseException());

		runner.run();

		verify(transcodingService, only()).transcode(1L);
	}
}
