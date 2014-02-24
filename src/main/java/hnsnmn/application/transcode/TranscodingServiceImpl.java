package hnsnmn.application.transcode;

import hnsnmn.domain.job.Job;
import hnsnmn.domain.job.JobRepository;
import hnsnmn.domain.job.ThumbnailExtractor;
import hnsnmn.domain.job.Transcoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 4:55
 * To change this template use File | Settings | File Templates.
 */
public class TranscodingServiceImpl implements TranscodingService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private ThumbnailExtractor thumbnailExtractor;
	private Transcoder transcoder;
	private JobRepository jobRepository;

	public TranscodingServiceImpl( ThumbnailExtractor thumbnailExtractor,
								  Transcoder transcoder,
								  JobRepository jobRepository) {
		this.thumbnailExtractor = thumbnailExtractor;
		this.transcoder = transcoder;
		this.jobRepository = jobRepository;
	}

	@Override
	public void transcode(Long jobId) {
		Job job = jobRepository.findById(jobId);
		checkJobExists(jobId, job);
		transcode(job);
	}

	private void transcode(Job job) {
		try {
			job.transcode(transcoder, thumbnailExtractor);
		} catch (RuntimeException ex) {
			logger.error("fail to do transcoding job {}", job.getId(), ex);
		}
	}

	private void checkJobExists(Long jobId, Job job) {
		if (job == null) {
			throw new JobNotFoundException(jobId);
		}
	}
}
