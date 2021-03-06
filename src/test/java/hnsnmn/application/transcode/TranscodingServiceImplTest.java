package hnsnmn.application.transcode;

import hnsnmn.domain.job.*;
import hnsnmn.handler.TranscodingExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 11.
 * Time: 오후 5:52
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImplTest {
	Long jobId = new Long(1);

	private File mockMultimediaFile = mock(File.class);
	private List<File> mockMultimediaFiles = new ArrayList<File>();
	private List<File> mockThumbnails = new ArrayList<File>();

	private List<OutputFormat> outputFormmats = new ArrayList<OutputFormat>();
	private final RuntimeException mockException = new RuntimeException();

	@Mock private MediaSourceCopier mediaSourceCopier;
	@Mock private Transcoder transcoder;
	@Mock private ThumbnailExtractor thumbnailExtractor;
	@Mock private CreatedFileSender createdFileSender;
	@Mock private JobRepository jobRepository;
	@Mock private JobStateChanger jobStateChanger;
	@Mock private TranscodingExceptionHandler transcodingExceptionHandler;
	@Mock private MediaSourceFile mediaSourceFile;
	@Mock private ResultCallback callback;

	@Mock private DestinationStorage destinationStorage;
	private TranscodingService transcodingService;
	private Job mockJob;
	private ThumbnailPolicy thumbnailPolicy;

	@Before
	public void setUp() {
//		mediaSourceCopier = mock(MediaSourceCopier.class);
		MockitoAnnotations.initMocks(this);
		thumbnailPolicy = new ThumbnailPolicy();

		mockJob = new Job(jobId, Job.State.WAITING, mediaSourceFile, destinationStorage,
				outputFormmats, callback, thumbnailPolicy, null);
		when(mediaSourceFile.getSourceFile()).thenReturn(mockMultimediaFile);
		when(transcoder.transcode(mockMultimediaFile, outputFormmats)).thenReturn(mockMultimediaFiles);
		when(jobRepository.findById(jobId)).thenReturn(mockJob);
		when(thumbnailExtractor.extract(mockMultimediaFile, thumbnailPolicy)).thenReturn(mockThumbnails);

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
		assertFalse(job.isExceptionOccured());
		assertNull(job.getExceptionMessage());

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
		when(thumbnailExtractor.extract(mockMultimediaFile, thumbnailPolicy)).thenThrow(mockException);

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


	private void excuteFailingTranscodeAndAssertFail(Job.State expected) {
		try {
			transcodingService.transcode(jobId);
		} catch (Exception ex) {
			assertSame(mockException, ex);
		}

		Job job = jobRepository.findById(jobId);
		assertTrue(job.isFinish());
		assertFalse(job.isSuccess());
		assertEquals(expected, job.getLastState());
		assertTrue(job.isExceptionOccured());
		assertNotNull(job.getExceptionMessage());
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
				verify(thumbnailExtractor, never()).extract(any(File.class), any(ThumbnailPolicy.class));
			else
				verify(thumbnailExtractor, only()).extract(mockMultimediaFile, thumbnailPolicy);

			if (this.destinationStorageNever)
				verify(destinationStorage, never()).save(mockMultimediaFiles, mockThumbnails);
			else
				verify(destinationStorage, only()).save(mockMultimediaFiles, mockThumbnails);

		}
	}
}
