package hnsnmn.application.transcode;

import hnsnmn.domain.job.*;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오전 9:46
* To change this template use File | Settings | File Templates.
*/
public class AddJobServiceImpl implements AddJobService {
	private MediaSourceFileFactory mediaSourceFileFactory;
	private DestinationStorageFactory destinationStorageFactory;
	private JobRepository jobRepository;
	private ResultCallbackFactory resultCallbackFactory;


	public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory, ResultCallbackFactory resultCallbackFactory, JobRepository jobRepository) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
		this.jobRepository = jobRepository;
	}

	@Override
	public Long addJob(AddJobRequest request) {
		Job job = createJob(request);
		Job savedJob = saveJob(job);
		return savedJob.getId();
	}

	private Job createJob(AddJobRequest request) {
		MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
		DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
		ResultCallback resultCallback = resultCallbackFactory.create(request.getResultCallback());

		return new Job(mediaSourceFile, destinationStorage, request.getOutputFormats(), resultCallback);
	}

	private Job saveJob(Job job) {
		return jobRepository.save(job);
	}
}
