package hnsnmn.application.transcode;

import hnsnmn.domain.job.Job;
import hnsnmn.domain.job.JobRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyLong;
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
	@Mock private JobRepository jobRepository;
	@Mock private Job job;

	private TranscodingRunner runner;

	@Before
	public void setUp() throws Exception {
		runner = new TranscodingRunner(transcodingService, jobRepository);
	}

	@Test
	public void runTranscodingWhenJobIsExists() {
		when(jobRepository.findEldestJobOfCreatedState()).thenReturn(job);
		when(job.getId()).thenReturn(1L);
		runner.run();

		verify(transcodingService, only()).transcode(1L);
	}

	@Test
	public void dontRunTranscodingWhenJobIsNotExists() {
		when(jobRepository.findEldestJobOfCreatedState()).thenReturn(null);

		runner.run();

		verify(transcodingService, never()).transcode(anyLong());
	}
}
