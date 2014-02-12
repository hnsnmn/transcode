package hnsnmn.service;

import hnsnmn.*;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 4:55
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingService {
	private MediaSourceCopier mediaSourceCopier;
	private JobResultNotifier jobResultNotifier;
	private CreatedFileSender createdFileSender;
	private ThumbnailExtractor thumbnailExtractor;
	private Transcoder transcoder;

	public TranscodingService(MediaSourceCopier mediaSourceCopier, JobResultNotifier jobResultNotifier, CreatedFileSender createdFileSender, ThumbnailExtractor thumbnailExtractor,  Transcoder transcoder) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.jobResultNotifier = jobResultNotifier;
		this.createdFileSender = createdFileSender;
		this.thumbnailExtractor = thumbnailExtractor;
		this.transcoder = transcoder;
	}

	public void transcode(Long jobId) {
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
