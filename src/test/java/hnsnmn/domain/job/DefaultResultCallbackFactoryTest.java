package hnsnmn.domain.job;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 10:19
 * To change this template use File | Settings | File Templates.
 */
public class DefaultResultCallbackFactoryTest {
	private DefaultResultCallbackFactory callbackFactory = new DefaultResultCallbackFactory();

	@Test
	public void shouldCreateHttpResultCallbackWhenUrlIsHttp() {
		ResultCallback callback = callbackFactory.create("http://localhost:9999/transcode/callback");
		assertTrue(callback instanceof HttpResultCallback);

	}

}
