package hnsnmn.application.transcode;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 3:16
 * To change this template use File | Settings | File Templates.
 */
public interface JobQueue {
	void add(Long jobId);
	Long nextJobId();

	@SuppressWarnings("serial")
	public class CloseException extends RuntimeException {
	}
}
