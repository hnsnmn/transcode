package hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 10:23
 * To change this template use File | Settings | File Templates.
 */
public class DefaultResultCallbackFactory implements ResultCallbackFactory{
	@Override
	public ResultCallback create(String url) {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return new HttpResultCallback(url);
		}
		throw new IllegalArgumentException();
	}
}
