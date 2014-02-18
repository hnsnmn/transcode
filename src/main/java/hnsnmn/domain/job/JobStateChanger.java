package hnsnmn.domain.job;


import hnsnmn.domain.job.Job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 13.
 * Time: 오전 9:44
 * To change this template use File | Settings | File Templates.
 */
public interface JobStateChanger {
	void changeJobState(Long jobId, Job.State jobState);
}
