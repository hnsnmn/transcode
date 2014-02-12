package hnsnmn;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 3:39
 * To change this template use File | Settings | File Templates.
 */
public interface JobResultNotifier {
	void notifyToRequest(Long jobId);
}
