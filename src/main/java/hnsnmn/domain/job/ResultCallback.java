package hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 20.
 * Time: 오전 10:10
 * To change this template use File | Settings | File Templates.
 */
public interface ResultCallback {
	void notifySuccessResult(Long id);
	void notifyFailedResult(long jobId, Job.State mediasourcecopying, String exception);
}
