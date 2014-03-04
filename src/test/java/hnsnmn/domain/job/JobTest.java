package hnsnmn.domain.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 20.
 * Time: 오전 10:00
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class JobTest {

	@Mock private MediaSourceFile mediaSource;
	@Mock private File sourceFile;
	@Mock private Transcoder transcoder;
	@Mock private List<File> multimediaFiles;
	@Mock private List<OutputFormat> outputFormats;
	@Mock private ThumbnailExtractor thumbnailExtractor;
	@Mock private List<File> thumbnails;
	@Mock private ResultCallback callback;
	@Mock private DestinationStorage destination;

	private ThumbnailPolicy thumbnailPolicy;

	@Test
	public void jobShouldBeCreatedStateWhenCreated() {
		thumbnailPolicy = new ThumbnailPolicy();
		Job job = new Job(mediaSource, destination, outputFormats, callback, thumbnailPolicy);

		assertEquals(Job.State.WAITING, job.getLastState());
		assertTrue(job.isWaiting());
		assertFalse(job.isFinish());
		assertFalse(job.isSuccess());
		assertFalse(job.isExceptionOccured());
	}

	@Test
	public void transcodeSuccessfully() {
		long jobId = 1L;

		when(mediaSource.getSourceFile()).thenReturn(sourceFile);
		when(transcoder.transcode(sourceFile, outputFormats)).thenReturn(multimediaFiles);
		when(thumbnailExtractor.extract(sourceFile, thumbnailPolicy)).thenReturn(thumbnails);


		Job job = createWaitingJobWithID(jobId);
		job.transcode(transcoder, thumbnailExtractor);

		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertTrue(job.isSuccess());
		assertTrue(job.isFinish());

		verify(mediaSource, only()).getSourceFile();
		verify(destination, only()).save(multimediaFiles, thumbnails);
		verify(callback, only()).notifySuccessResult(jobId);
	}

	private Job createWaitingJobWithID(long jobId) {
		return new Job(jobId, Job.State.WAITING, mediaSource, destination,
				outputFormats, callback, thumbnailPolicy, null);
	}

	@Test
	public void jobShouldThrowExceptionWhenFailgetSourceFile() {
		long jobId = 1L;
		RuntimeException exception = new RuntimeException("exception");
		when(mediaSource.getSourceFile()).thenThrow(exception);

		Job job = createWaitingJobWithID(jobId);

		try {
			job.transcode(transcoder, thumbnailExtractor);
			fail("발생해야됨.");
		} catch (Exception e) {
		}
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());
		assertTrue(job.isExceptionOccured());
		assertFalse(job.isSuccess());
		assertTrue(job.isFinish());

		verify(mediaSource, only()).getSourceFile();
		verify(destination, never()).save(multimediaFiles, thumbnails);
		verify(callback, only()).notifyFailedResult(jobId, Job.State.MEDIASOURCECOPYING, "exception");
	}
}
