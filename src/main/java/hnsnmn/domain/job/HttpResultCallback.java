package hnsnmn.domain.job;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오후 10:42
* To change this template use File | Settings | File Templates.
*/
public class HttpResultCallback extends ResultCallback {

	public HttpResultCallback(String url) {
		super(url);
	}

	@Override
	public void notifySuccessResult(Long id) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void notifyFailedResult(Long jobId, Job.State lastState, String errorCause) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
