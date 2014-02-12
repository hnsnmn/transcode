package hnsnmn;

import hnsnmn.service.MediaSourceCopier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 11.
 * Time: 오후 5:52
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceTest {
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

	@Before
	public void setUp() {
//		mediaSourceCopier = mock(MediaSourceCopier.class);
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void transcodeSuccessfully() {
		// 미디어 원본으로부터 파일을 로컬에 복사한다.
		Long jobId = new Long(1);
		File mockMultimediaFile = mock(File.class);
		when(mediaSourceCopier.copy(jobId)).thenReturn(mockMultimediaFile);

		List<File> mockMultimediaFiles = new ArrayList<File>();
		when(transcoder.transcode(mockMultimediaFile, jobId)).thenReturn(mockMultimediaFiles);

		List<File> mockThumbnails = new ArrayList<File>();
		when(thumbnailExtractor.extract(mockMultimediaFile, jobId)).thenReturn(mockThumbnails);

		transcode(jobId);

		verify(mediaSourceCopier, only()).copy(jobId);
		verify(transcoder, only()).transcode(mockMultimediaFile, jobId);
		verify(thumbnailExtractor, only()).extract(mockMultimediaFile, jobId);
		verify(createdFileSender, only()).send(mockMultimediaFiles, mockThumbnails, jobId);
		verify(jobResultNotifier, only()).notifyToRequest(jobId);

	}

	private void transcode(Long jobId) {
		File multimediaFile = copyMultimediaSourceToLocal(jobId);
		// 로컬에 복사된 파일을 변환처리한다.
		List<File> multimediaFiles = transcode(multimediaFile, jobId);

		// 로컬에 복사된 파일로부터 이미지를 추출한다.
		List<File> thumbnails = extractThumbnail(multimediaFile, jobId);


		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장한다.
		sendCreatedFilesToDestination(multimediaFiles, thumbnails, jobId);

		// 결과를 통지한다.
		notifyJobResultToRequester(jobId);
	}

	private void notifyJobResultToRequester(Long jobId) {
		jobResultNotifier.notifyToRequest(jobId);
	}

	private void sendCreatedFilesToDestination(List<File> multimediaFiles, List<File> thumbnails, Long jobId) {
		createdFileSender.send(multimediaFiles, thumbnails, jobId);
	}

	private List<File> extractThumbnail(File multimediaFile, Long jobId) {
		return thumbnailExtractor.extract(multimediaFile, jobId);
	}

	private List<File> transcode(File mediaFile, Long jobId) {
		return transcoder.transcode(mediaFile, jobId);
	}

	private File copyMultimediaSourceToLocal(Long jobId) {
		return mediaSourceCopier.copy(jobId);
	}

}
