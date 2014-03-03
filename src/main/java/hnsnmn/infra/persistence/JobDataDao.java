package hnsnmn.infra.persistence;

import hnsnmn.domain.job.Job;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 3.
 * Time: 오후 2:14
 * To change this template use File | Settings | File Templates.
 */
public interface JobDataDao extends Repository<JobData, Long> {
	public JobData save(JobData jobData);

	public JobData findById(Long id);

	@Transactional
	@Modifying
	@Query("update JobData j set j.state = ?2 where j.id = ?1")
	public int updateState(Long id, Job.State newState);

	@Transactional
	@Modifying
	@Query("update JobData j set j.exceptionMessage = ?2 where j.id = ?1")
	public void updateExceptionMessage(Long id, String exceptionMessage);
}
