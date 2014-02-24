package hnsnmn.domain.job;

import static hnsnmn.domain.job.Job.*;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 10:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class ResultCallback {
	private String url;

	public ResultCallback(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract void notifySuccessResult(Long jobId);

	public abstract void notifyFailedResult(Long jobId, State lastState, String errorCause);
}
