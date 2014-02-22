package hnsnmn.domain.job;

import hnsnmn.application.transcode.DestinationStorageFactory;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 22.
 * Time: 오후 4:48
 * To change this template use File | Settings | File Templates.
 */
public class DestinationStorageFactoryDefaultTest {
	@Test
	public void createFileDestinationFactory() {
		DestinationStorageFactory factory = DestinationStorageFactory.DEFAULT;
		DestinationStorage destinationStorage = factory.create("file://usr/local");
		assertTrue(destinationStorage instanceof FileDestinationStorage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotSurpportedDestination() {
		DestinationStorageFactory factory = DestinationStorageFactory.DEFAULT;
		factory.create("xxx://www.naver.com");
		fail("must thorw Exception");
	}

}
