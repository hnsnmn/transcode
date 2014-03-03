package hnsnmn.infra.persistence;

import hnsnmn.domain.job.*;
import hnsnmn.springconfig.ApplicationContextConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 3.
 * Time: 오후 1:12
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class})
public class JobIntTest {

	@Autowired
	private JobRepository jobRepository;

	private Transcoder transcoder;
	private ThumbnailExtractor thumbnailExtractor;

	@Before
	public void setUp() {
		transcoder = mock(Transcoder.class);
		thumbnailExtractor = mock(ThumbnailExtractor.class);
	}

	@Test
	public void jobShouldChangeStateInDB() {
		RuntimeException transcoderException = new RuntimeException("강제발생");
		when(transcoder.transcode(any(File.class), anyListOf(OutputFormat.class))).thenThrow(transcoderException);

		Long jobId = new Long(1);
		Job job = jobRepository.findById(jobId);

		try {
			job.transcode(transcoder, thumbnailExtractor);
		} catch (RuntimeException e) {
		}

		Job updateJob = jobRepository.findById(jobId);

		assertEquals(Job.State.TRANSCODING, job.getLastState());
		assertEquals(Job.State.TRANSCODING, updateJob.getLastState());
		assertEquals("강제발생", job.getExceptionMessage());
		assertEquals(job.getExceptionMessage(), updateJob.getExceptionMessage());

	}

}
