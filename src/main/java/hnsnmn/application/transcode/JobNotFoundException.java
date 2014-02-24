package hnsnmn.application.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 2:03
 * To change this template use File | Settings | File Templates.
 */
public class JobNotFoundException extends RuntimeException{
	public JobNotFoundException(Long jobId) {
		super("Not found Job[" + jobId + "]");
	}
}
