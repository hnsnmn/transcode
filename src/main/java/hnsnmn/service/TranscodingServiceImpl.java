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
public class TranscodingServiceImpl implements TranscodingService {
	private MediaSourceCopier mediaSourceCopier;
	private JobResultNotifier jobResultNotifier;
	private CreatedFileSender createdFileSender;
	private ThumbnailExtractor thumbnailExtractor;
	private Transcoder transcoder;
	private JobStateChanger jobStateChanger;
	private TranscodingExceptionHandler transcodingExceptionHandler;

	public TranscodingServiceImpl(MediaSourceCopier mediaSourceCopier,
								  JobResultNotifier jobResultNotifier,
								  CreatedFileSender createdFileSender,
								  ThumbnailExtractor thumbnailExtractor,
								  Transcoder transcoder,
								  JobStateChanger jobStateChnager,
								  TranscodingExceptionHandler transcodingExceptionHandler) {
		this.mediaSourceCopier = mediaSourceCopier;
		this.jobResultNotifier = jobResultNotifier;
		this.createdFileSender = createdFileSender;
		this.thumbnailExtractor = thumbnailExtractor;
		this.transcoder = transcoder;
		this.jobStateChanger = jobStateChnager;
		this.transcodingExceptionHandler = transcodingExceptionHandler;
	}

	public void transcode(Long jobId) {
		changeJobState(jobId, Job.State.MEDIASOURCECOPYING);

		// 미디어 원본으로부터 파일을 로컬에 복사한다.
		File multimediaFile = copyMultimediaSourceToLocal(jobId);
		// 로컬에 복사된 파일을 변환처리한다.
		List<File> multimediaFiles = transcode(multimediaFile, jobId);

		// 로컬에 복사된 파일로부터 이미지를 추출한다.
		List<File> thumbnails = extractThumbnail(multimediaFile, jobId);


		// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장한다.
		sendCreatedFilesToDestination(multimediaFiles, thumbnails, jobId);

		// 결과를 통지한다.
		notifyJobResultToRequester(jobId);
		changeJobState(jobId, Job.State.COMPLETED);
	}

	private void changeJobState(Long jobId, Job.State newJobState) {
		jobStateChanger.changeJobState(jobId, newJobState);
	}

	private File copyMultimediaSourceToLocal(Long jobId) {
		try {
			return mediaSourceCopier.copy(jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private List<File> transcode(File mediaFile, Long jobId) {
		try {
			return transcoder.transcode(mediaFile, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private void notifyJobResultToRequester(Long jobId) {
		try {
			jobResultNotifier.notifyToRequest(jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private void sendCreatedFilesToDestination(List<File> multimediaFiles, List<File> thumbnails, Long jobId) {
		try {
			createdFileSender.send(multimediaFiles, thumbnails, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}

	private List<File> extractThumbnail(File multimediaFile, Long jobId) {
		try {
			return thumbnailExtractor.extract(multimediaFile, jobId);
		} catch (RuntimeException ex) {
			transcodingExceptionHandler.notifyToJob(jobId, ex);
			throw ex;
		}
	}
}
