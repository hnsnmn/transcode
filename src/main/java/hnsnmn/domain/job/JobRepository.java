package hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 6:08
 * To change this template use File | Settings | File Templates.
 */
public interface JobRepository {
	Job findById(Long jobId);
	Job save(Job job);
}
