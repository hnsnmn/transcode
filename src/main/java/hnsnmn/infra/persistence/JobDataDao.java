package hnsnmn.infra.persistence;

import org.springframework.data.repository.Repository;

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
}
