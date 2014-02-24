package hnsnmn.domain.job;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 17.
 * Time: 오후 4:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class MediaSourceFile {
	private String url;

	public MediaSourceFile(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public abstract File getSourceFile();
}
