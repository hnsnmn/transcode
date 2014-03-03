package hnsnmn.springconfig;

import hnsnmn.domain.job.*;
import hnsnmn.infra.persistence.JobData;
import hnsnmn.infra.persistence.JobDataDao;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 8:59
 * To change this template use File | Settings | File Templates.
 */
public class DbJobRepository implements JobRepository {
	private final MediaSourceFileFactory mediaSourceFileFactory;
	private final DestinationStorageFactory destinationStorageFactory;
	private final ResultCallbackFactory resultCallbackFactory;
	private final JobDataDao jobDataDao;

	public DbJobRepository(JobDataDao jobDataDao, MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory,
						   ResultCallbackFactory resultCallbackFactory) {
		this.jobDataDao = jobDataDao;
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
	}

	@Override
	public Job findById(Long jobId) {
		JobData jobData = jobDataDao.findById(jobId);
		if (jobData == null) {
			return null;
		}

		return createJobFromJobData(jobData);
	}

	@Override
	public Job save(Job job) {
		JobData.ExporterToJobData exporter = new JobData.ExporterToJobData();
		JobData jobData = job.exporter(exporter);
		JobData savedJobData = jobDataDao.save(jobData);
		return createJobFromJobData(savedJobData);
	}

	private Job createJobFromJobData(JobData jobData) {
		return new Job(jobData.getId(), jobData.getState(),
				mediaSourceFileFactory.create(jobData.getSourceUrl()),
				destinationStorageFactory.create(jobData.getDestinationUrl()),
				jobData.getOutputFormats(),
				resultCallbackFactory.create(jobData.getCallbackUrl()),
				jobData.getExceptionMessage());
	}
}
