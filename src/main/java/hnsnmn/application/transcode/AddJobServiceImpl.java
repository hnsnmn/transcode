package hnsnmn.application.transcode;

import hnsnmn.domain.job.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오전 9:46
* To change this template use File | Settings | File Templates.
*/
public class AddJobServiceImpl implements AddJobService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private MediaSourceFileFactory mediaSourceFileFactory;

	private DestinationStorageFactory destinationStorageFactory;
	private JobRepository jobRepository;
	private ResultCallbackFactory resultCallbackFactory;
	private JobQueue jobQueue;


	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory, ResultCallbackFactory resultCallbackFactory, JobRepository jobRepository, JobQueue jobQueue) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
		this.jobRepository = jobRepository;
		this.jobQueue = jobQueue;
	}

	@Override
	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job savedJob = saveJob(job);
		jobQueue.add(savedJob.getId());
		return savedJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		try {
			MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
			DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
			ResultCallback resultCallback = resultCallbackFactory.create(request.getResultCallback());

			return new Job(mediaSourceFile, destinationStorage,
					request.getOutputFormats(), resultCallback,
					request.getThumbnailPolicy());
		} catch (RuntimeException ex) {
			logger.error("fail to create Job from request {}", request, ex);
			throw ex;
		}
	}

	private Job saveJob(Job job) {
		try {
			return jobRepository.save(job);
		} catch (RuntimeException ex) {
			logger.error("fail to save Job to Repository", ex);
			throw ex;
		}
	}
}
