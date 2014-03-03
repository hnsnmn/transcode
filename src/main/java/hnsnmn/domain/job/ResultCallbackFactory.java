package hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오전 9:39
 * To change this template use File | Settings | File Templates.
 */
public interface ResultCallbackFactory {
	ResultCallback create(String resultCallback);
}
