package hnsnmn;

import hnsnmn.service.JobStateChanger;
import hnsnmn.service.TranscodingExceptionHandler;
import hnsnmn.service.TranscodingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
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
	private final File mockMultimediaFile = mock(File.class);
	private final List<File> mockMultimediaFiles = new ArrayList<File>();
	private final List<File> mockThumbnails = new ArrayList<File>();
	private final RuntimeException mockException = new RuntimeException();
	Long jobId = new Long(1);
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
	private JobRepository jobRepository;
	@Mock
	private JobStateChanger jobStateChanger;
	@Mock
	private TranscodingExceptionHandler transcodingExceptionHandler;
	@Mock
	private MediaSourceFile mediaSourceFile;
	@Mock
	private DestinationStorage destinationStorage;

	private TranscodingService transcodingService;
	private Job mockJob;

	@Before
	public void setUp() {
//		mediaSourceCopier = mock(MediaSourceCopier.class);
		MockitoAnnotations.initMocks(this);

		mockJob = new Job(jobId, mediaSourceFile, destinationStorage);
		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);

		transcodingService = new TranscodingServiceImpl(jobResultNotifier,
				thumbnailExtractor,
				transcoder,
				jobRepository);
	}

	@Test
	public void transcodeSuccessfully() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);


		Job job = jobRepository.findById(jobId);
		assertTrue(job.isWaiting());
		transcodingService.transcode(jobId);

		assertTrue(job.isFinish());
		assertTrue(job.isSuccess());
		assertEquals(Job.State.COMPLETED, job.getLastState());
		assertNull(job.getOccuredException());

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtMediaSourceFile() {
		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);

		excuteFailingTranscodeAndAssertFail(Job.State.MEDIASOURCECOPYING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = true;
		collaborationVerifier.thumbnailExtractorNever = true;
		collaborationVerifier.destinationStorageNever = true;
		collaborationVerifier.jobResultNotifierNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecuaseExceptionOccuredAtTranscode() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenThrow(mockException);
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		excuteFailingTranscodeAndAssertFail(Job.State.TRANSCODING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.thumbnailExtractorNever = true;
		collaborationVerifier.destinationStorageNever = true;
		collaborationVerifier.jobResultNotifierNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumbnailExtractor() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenThrow(mockException);
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		excuteFailingTranscodeAndAssertFail(Job.State.EXTRACTINGTHUMBNAIL);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.destinationStorageNever = true;
		collaborationVerifier.jobResultNotifierNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtDestinationStorage() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		doThrow(mockException).when(destinationStorage).save(mockMultimediaFiles, mockThumbnails);

		excuteFailingTranscodeAndAssertFail(Job.State.STORING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.jobResultNotifierNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtJobResultNotifier() {
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);
		when(jobRepository.findById(jobId)).thenReturn(mockJob);

		doThrow(mockException).when(jobResultNotifier).notifyToRequest(jobId);


		excuteFailingTranscodeAndAssertFail(Job.State.NOTIFYING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.verifyCollaboration();
	}

	private void excuteFailingTranscodeAndAssertFail(Job.State expected) {
		Exception throwEx = null;
		try {
			transcodingService.transcode(jobId);
		} catch (Exception e) {
			throwEx = e;
		}
		assertThat(throwEx, instanceOf(Exception.class));

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(expected, job.getLastState());
		assertNotNull(job.getOccuredException());
	}


	private class CollaborationVerifier {
		public boolean transcoderNever;
		public boolean thumbnailExtractorNever;
		public boolean jobResultNotifierNever;
		public boolean destinationStorageNever;

		public void verifyCollaboration() {
//		verify(mediaSourceCopier, only()).copy(jobId);

			if (this.transcoderNever)
				verify(transcoder, never()).transcode(any(File.class), anyLong());
			else
				verify(transcoder, only()).transcode(mockMultimediaFile, jobId);

			if (this.thumbnailExtractorNever)
				verify(thumbnailExtractor, never()).extract(any(File.class), anyLong());
			else
				verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);

			if (this.destinationStorageNever)
				verify(destinationStorage, never()).save(mockMultimediaFiles, mockThumbnails);
			else
				verify(destinationStorage, only()).save(mockMultimediaFiles, mockThumbnails);

			if (this.jobResultNotifierNever)
				verify(jobResultNotifier, never()).notifyToRequest(jobId);
			else
				verify(jobResultNotifier, only()).notifyToRequest(jobId);

		}
	}
}
