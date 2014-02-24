package hnsnmn.domain.job.destination;

import hnsnmn.application.transcode.DestinationStorageFactory;
import hnsnmn.domain.job.DestinationStorage;
import hnsnmn.domain.job.FileDestinationStorage;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 24.
 * Time: 오후 1:20
 * To change this template use File | Settings | File Templates.
 */
public class DefaultDestinationStorageFactory implements DestinationStorageFactory {
	@Override
	public DestinationStorage create(String destinationStorage) {
		if (destinationStorage.startsWith("file://")) {
			return new FileDestinationStorage(
					destinationStorage.substring("file://".length())
			);
		}
		throw new IllegalArgumentException(
				"not supported destination storage : " + destinationStorage);
	}
}
