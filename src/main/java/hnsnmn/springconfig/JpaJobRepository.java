package hnsnmn.springconfig;

import hnsnmn.domain.job.Job;
import hnsnmn.domain.job.JobRepository;
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
	@PersistenceContext
	private EntityManager entityManager;


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
		job.exporter(exporter);
		JobData jobData = exporter.getJobData();
		entityManager.persist(jobData);
		return createJobFromJobData(jobData);
	}

	private Job createJobFromJobData(JobData jobData) {
		return null;  //To change body of created methods use File | Settings | File Templates.
	}
}
