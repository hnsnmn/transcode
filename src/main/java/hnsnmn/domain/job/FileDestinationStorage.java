package hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 22.
* Time: 오후 4:55
* To change this template use File | Settings | File Templates.
*/
public class FileDestinationStorage implements DestinationStorage {
	private String folder;

	public FileDestinationStorage(String folder) {
		this.folder = folder;
	}

	@Override
	public void save(List<File> multimediaFiles, List<File> thumbnails) {

	}
}
