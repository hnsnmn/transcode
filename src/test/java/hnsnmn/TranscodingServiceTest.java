package hnsnmn;

import hnsnmn.service.JobStateChanger;
import hnsnmn.service.TranscodingExceptionHandler;
import hnsnmn.service.TranscodingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 11.
 * Time: 오후 5:52
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceTest {
	Long jobId;
	@Mock
	private MediaSourceCopier mediaSourceCopier;
	@Mock
	private Transcoder transcoder;
	@Mock
	private ThumbnailExtractor thumbnailExtractor;
	@Mock
	private CreatedFileSender createdFileSender;
	@Mock
	private JobResultNotifier jobResultNotifier;
	@Mock
	private JobRepository jobRepositor;
	@Mock
	private JobStateChanger jobStateChanger;
	@Mock
	private TranscodingExceptionHandler transcodingExceptionHandler;

	private TranscodingService transcodingService;
	private Job mockJob = new Job();
	private final File mockMultimediaFile = mock(File.class);
	private final List<File> mockMultimediaFiles = new ArrayList<File>();
	private final List<File> mockThumbnails = new ArrayList<File>();
	private final RuntimeException mockException = new RuntimeException();

	@Before
	public void setUp() {
//		mediaSourceCopier = mock(MediaSourceCopier.class);
		MockitoAnnotations.initMocks(this);
		transcodingService = new TranscodingServiceImpl(mediaSourceCopier,
				jobResultNotifier,
				createdFileSender,
				thumbnailExtractor,
				transcoder,
				jobStateChanger,
				transcodingExceptionHandler);

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				Job.State newState = (Job.State) invocationOnMock.getArguments()[1];
				mockJob.changeState(newState);
				return null;
			}
		}).when(jobStateChanger).changeJobState(anyLong(), any(Job.State.class));

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				RuntimeException ex = (RuntimeException) invocationOnMock.getArguments()[1];
				mockJob.exceptionOccured(ex);
				return null;
			}
		}).when(transcodingExceptionHandler).notifyToJob(anyLong(), any(RuntimeException.class));
		jobId = new Long(1);
	}

	@Test
	public void transcodeSuccessfully() {
		when(jobRepositor.findById(jobId)).thenReturn(mockJob);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);


		Job job = jobRepositor.findById(jobId);
		assertTrue(job.isWaiting());
		transcodingService.transcode(jobId);

		assertTrue(job.isFinish());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertNull(job.getOccuredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);
		verify(createdFileSender, only()).send(mockMultimediaFiles, mockThumbnails, jobId);
		verify(jobResultNotifier, only()).notifyToRequest(jobId);

	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceCopier() {
		when(jobRepositor.findById(jobId)).thenReturn(mockJob);
		when(mediaSourceCopier.copy(jobId)).thenThrow(mockException);

		Exception throwEx = null;
		try {
			transcodingService.transcode(jobId);
		} catch (Exception e) {
			throwEx = e;
		}
		assertThat(throwEx, instanceOf(Exception.class));

		Job job = jobRepositor.findById(jobId);
		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(Job.State.MEDIASOURCECOPYING, job.getLastState());
		assertNotNull(job.getOccuredException());

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, never()).transcode(any(File.class), anyLong());
		verify(thumbnailExtractor, never()).extract(any(File.class), anyLong());
		verify(createdFileSender, never()).send(anyListOf(File.class), anyListOf(File.class), anyLong());
		verify(jobResultNotifier, never()).notifyToRequest(anyLong());

	}
}
