package hnsnmn.service;

import hnsnmn.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 4:55
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImpl implements TranscodingService {
	private JobResultNotifier jobResultNotifier;
	private CreatedFileSender createdFileSender;
	private ThumbnailExtractor thumbnailExtractor;
	private Transcoder transcoder;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(JobResultNotifier jobResultNotifier,
								  CreatedFileSender createdFileSender,
								  ThumbnailExtractor thumbnailExtractor,
								  Transcoder transcoder,
								  JobRepository jobRepository) {
		this.jobResultNotifier = jobResultNotifier;
		this.createdFileSender = createdFileSender;
		this.thumbnailExtractor = thumbnailExtractor;
		this.transcoder = transcoder;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(transcoder, thumbnailExtractor, createdFileSender, jobResultNotifier);
	}

//	private void changeJobState(Long jobId, Job.State newJobState) {
//		jobStateChanger.changeJobState(jobId, newJobState);
//	}
//
//	private File copyMultimediaSourceToLocal(Long jobId) {
//		try {
//			return mediaSourceCopier.copy(jobId);
//		} catch (RuntimeException ex) {
//			transcodingExceptionHandler.notifyToJob(jobId, ex);
//			throw ex;
//		}
//	}
//
//	private List<File> transcode(File mediaFile, Long jobId) {
//		try {
//			return transcoder.transcode(mediaFile, jobId);
//		} catch (RuntimeException ex) {
//			transcodingExceptionHandler.notifyToJob(jobId, ex);
//			throw ex;
//		}
//	}
//
//	private void notifyJobResultToRequester(Long jobId) {
//		try {
//			jobResultNotifier.notifyToRequest(jobId);
//		} catch (RuntimeException ex) {
//			transcodingExceptionHandler.notifyToJob(jobId, ex);
//			throw ex;
//		}
//	}
//
//	private void storeCreatedFilesToDestination(List<File> multimediaFiles, List<File> thumbnails, Long jobId) {
//		try {
//			createdFileSender.store(multimediaFiles, thumbnails, jobId);
//		} catch (RuntimeException ex) {
//			transcodingExceptionHandler.notifyToJob(jobId, ex);
//			throw ex;
//		}
//	}
//
//	private List<File> extractThumbnail(File multimediaFile, Long jobId) {
//		try {
//			return thumbnailExtractor.extract(multimediaFile, jobId);
//		} catch (RuntimeException ex) {
//			transcodingExceptionHandler.notifyToJob(jobId, ex);
//			throw ex;
//		}
//	}
}
