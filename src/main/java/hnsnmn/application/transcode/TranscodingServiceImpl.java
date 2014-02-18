package hnsnmn.application.transcode;

import hnsnmn.domain.job.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 4:55
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImpl implements TranscodingService {
	private JobResultNotifier jobResultNotifier;
	private ThumbnailExtractor thumbnailExtractor;
	private Transcoder transcoder;
	private JobRepository jobRepository;

	public TranscodingServiceImpl(JobResultNotifier jobResultNotifier,
								  ThumbnailExtractor thumbnailExtractor,
								  Transcoder transcoder,
								  JobRepository jobRepository) {
		this.jobResultNotifier = jobResultNotifier;
		this.thumbnailExtractor = thumbnailExtractor;
		this.transcoder = transcoder;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		job.transcode(transcoder, thumbnailExtractor, jobResultNotifier);
	}
}
