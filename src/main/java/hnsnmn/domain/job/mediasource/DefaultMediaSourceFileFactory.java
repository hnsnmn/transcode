package hnsnmn.domain.job.mediasource;

import hnsnmn.domain.job.MediaSourceFile;
import hnsnmn.domain.job.MediaSourceFileFactory;
import hnsnmn.domain.job.mediasource.LocalStorageMediaSourceFile;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오전 10:26
* To change this template use File | Settings | File Templates.
*/
public class DefaultMediaSourceFileFactory implements MediaSourceFileFactory {
	@Override
	public MediaSourceFile create(String mediaSource) {
		if (mediaSource.startsWith("file://")) {
			String filePath = mediaSource.substring("file://".length());
			return new LocalStorageMediaSourceFile(filePath);
		}
		throw new IllegalArgumentException("not supported media source : " + mediaSource);
	}
}
