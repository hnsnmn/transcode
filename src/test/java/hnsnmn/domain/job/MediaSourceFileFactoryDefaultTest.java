package hnsnmn.domain.job;

import hnsnmn.application.transcode.MediaSourceFileFactory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
		MediaSourceFile sourceFile = factory.create("file://./src/test/resource/sample.avi");
		assertTrue(sourceFile instanceof LocalStorageMediaSourceFile);
		assertTrue(sourceFile.getSourceFile().exists());
	}

	private class LocalStorageMediaSourceFile {
	}
}
