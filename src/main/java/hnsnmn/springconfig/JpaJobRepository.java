package hnsnmn.springconfig;

import hnsnmn.domain.job.*;
import hnsnmn.infra.persistence.JobData;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 8:59
 * To change this template use File | Settings | File Templates.
 */
public class JpaJobRepository implements JobRepository {
	private final MediaSourceFileFactory mediaSourceFileFactory;
	private final DestinationStorageFactory destinationStorageFactory;
	private final ResultCallbackFactory resultCallbackFactory;

	@PersistenceContext
	private EntityManager entityManager;

	public JpaJobRepository(MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory,
							ResultCallbackFactory resultCallbackFactory) {
		this.mediaSourceFileFactory = mediaSourceFileFactory;
		this.destinationStorageFactory = destinationStorageFactory;
		this.resultCallbackFactory = resultCallbackFactory;
	}

	@Transactional
	@Override
	public Job findById(Long jobId) {
		JobData jobData = entityManager.find(JobData.class, jobId);
		if (jobData == null) {
			return null;
		}

		return createJobFromJobData(jobData);
	}

	@Transactional
	@Override
	public Job save(Job job) {
		JobData.ExporterToJobData exporter = new JobData.ExporterToJobData();
		JobData jobData = job.exporter(exporter);
		entityManager.persist(jobData);
		return createJobFromJobData(jobData);
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
