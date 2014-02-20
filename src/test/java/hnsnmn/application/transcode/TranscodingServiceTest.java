package hnsnmn.application.transcode;

import hnsnmn.domain.job.*;
import hnsnmn.domain.job.JobStateChanger;
import hnsnmn.handler.TranscodingExceptionHandler;
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
	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumbnails = new ArrayList<File>();
	private List<OutputFormat> outputFormmats = new ArrayList<OutputFormat>();

	private final RuntimeException mockException = new RuntimeException();
	Long jobId = new Long(1);
	@Mock private MediaSourceCopier mediaSourceCopier;
	@Mock private Transcoder transcoder;
	@Mock private ThumbnailExtractor thumbnailExtractor;
	@Mock private CreatedFileSender createdFileSender;
	@Mock private JobResultNotifier jobResultNotifier;
	@Mock private JobRepository jobRepository;
	@Mock private JobStateChanger jobStateChanger;
	@Mock private TranscodingExceptionHandler transcodingExceptionHandler;
	@Mock private MediaSourceFile mediaSourceFile;
	@Mock private ResultCallback callback;

	@Mock private DestinationStorage destinationStorage;
	private TranscodingService transcodingService;
	private Job mockJob;

	@Before
	public void setUp() {
//		mediaSourceCopier = mock(MediaSourceCopier.class);
		MockitoAnnotations.initMocks(this);

		mockJob = new Job(jobId, mediaSourceFile, destinationStorage, outputFormmats, callback);
		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, outputFormmats)).thenReturn(mockMultimediaFiles);
		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);

		transcodingService = new TranscodingServiceImpl( thumbnailExtractor,
				transcoder,
				jobRepository);
	}

	@Test
	public void transcodeSuccessfully() {
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
		when(mediaSourceFile.getSourceFile()).thenThrow(mockException);

		excuteFailingTranscodeAndAssertFail(Job.State.MEDIASOURCECOPYING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.transcoderNever = true;
		collaborationVerifier.thumbnailExtractorNever = true;
		collaborationVerifier.destinationStorageNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecuaseExceptionOccuredAtTranscode() {
		when(transcoder.transcode(mockMultimediaFile, outputFormmats)).thenThrow(mockException);

		excuteFailingTranscodeAndAssertFail(Job.State.TRANSCODING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.thumbnailExtractorNever = true;
		collaborationVerifier.destinationStorageNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtThumbnailExtractor() {
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenThrow(mockException);

		excuteFailingTranscodeAndAssertFail(Job.State.EXTRACTINGTHUMBNAIL);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.destinationStorageNever = true;
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtDestinationStorage() {
		doThrow(mockException).when(destinationStorage).save(mockMultimediaFiles, mockThumbnails);

		excuteFailingTranscodeAndAssertFail(Job.State.STORING);

		CollaborationVerifier collaborationVerifier = new CollaborationVerifier();
		collaborationVerifier.verifyCollaboration();
	}

	@Test
	public void transcodeFailBecauseExceptionOccuredAtJobResultNotifier() {
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
		public boolean destinationStorageNever;

		public void verifyCollaboration() {
//		verify(mediaSourceCopier, only()).copy(jobId);

			if (this.transcoderNever)
				verify(transcoder, never()).transcode(any(File.class), anyListOf(OutputFormat.class));
			else
				verify(transcoder, only()).transcode(mockMultimediaFile, outputFormmats);

			if (this.thumbnailExtractorNever)
				verify(thumbnailExtractor, never()).extract(any(File.class), anyLong());
			else
				verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);

			if (this.destinationStorageNever)
				verify(destinationStorage, never()).save(mockMultimediaFiles, mockThumbnails);
			else
				verify(destinationStorage, only()).save(mockMultimediaFiles, mockThumbnails);

		}
	}
}
