package hnsnmn.application.transcode;

import hnsnmn.domain.job.DestinationStorage;
import hnsnmn.domain.job.FileDestinationStorage;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 20.
 * Time: 오후 2:24
 * To change this template use File | Settings | File Templates.
 */
public interface DestinationStorageFactory {
	DestinationStorage create(String storage);

	DestinationStorageFactory DEFAULT = new DestinationStorageFactory() {
		@Override
		public DestinationStorage create(String storage) {
			if (storage.startsWith("file://")) {
				return new FileDestinationStorage(storage.substring("file://".length()));
			}
			throw new IllegalArgumentException(
					"not supported destination storage : " + storage
			);
		}
	};
}
