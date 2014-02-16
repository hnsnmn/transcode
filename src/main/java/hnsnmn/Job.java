package hnsnmn;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 6:09
 * To change this template use File | Settings | File Templates.
 */
public class Job {

	private State state;

	private Exception occurredException;

	public Exception getOccuredException() {
		return occurredException;
	}

	public boolean isWaiting() {
		return state == null;
	}

	public boolean isFinish() {
		return isSuccess() || isExceptionOccurred();
	}

	private boolean isExceptionOccurred() {
		return occurredException != null;
	}

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	public void changeState(State state) {
		this.state = state;
	}

	public void exceptionOccured(RuntimeException ex) {
		occurredException = ex;
	}

	public static enum State {
		COMPLETED,
		MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING;

	}
}
