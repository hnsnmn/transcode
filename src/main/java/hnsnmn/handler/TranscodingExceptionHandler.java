package hnsnmn.handler;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 13.
 * Time: 오후 1:42
 * To change this template use File | Settings | File Templates.
 */
public interface TranscodingExceptionHandler {
	void notifyToJob(Long jobId, RuntimeException ex);
}
