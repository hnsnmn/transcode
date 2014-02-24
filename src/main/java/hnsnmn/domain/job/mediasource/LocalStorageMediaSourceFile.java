package hnsnmn.domain.job.mediasource;

import hnsnmn.domain.job.MediaSourceFile;

import java.io.File;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 22.
* Time: 오후 4:26
* To change this template use File | Settings | File Templates.
*/
public class LocalStorageMediaSourceFile implements MediaSourceFile {
	private String filePath;

	public LocalStorageMediaSourceFile(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public File getSourceFile() {
		return new File(filePath);
	}
}
