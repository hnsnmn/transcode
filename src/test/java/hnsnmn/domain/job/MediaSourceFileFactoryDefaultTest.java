package hnsnmn.domain.job;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 22.
 * Time: 오전 10:07
 * To change this template use File | Settings | File Templates.
 */
public class MediaSourceFileFactoryDefaultTest {
	@Test
	public void createLocalStorageMediaSourceFile() {
		MediaSourceFileFactory factory = MediaSourceFileFactory.DEFAULT;
		MediaSourceFile sourceFile = factory.create("file://./src/test/resources/sample.avi");
		assertTrue(sourceFile instanceof LocalStorageMediaSourceFile);
		assertTrue(sourceFile.getSourceFile().exists());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSupportedSource() {
		MediaSourceFileFactory factory = MediaSourceFileFactory.DEFAULT;
		factory.create("xxx://www.naver.com");
		fail("must throw exception");
	}

}
