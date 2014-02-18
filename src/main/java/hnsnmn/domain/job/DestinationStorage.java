package hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 17.
 * Time: 오후 9:53
 * To change this template use File | Settings | File Templates.
 */
public interface DestinationStorage {
	void save(List<File> multimediaFiles, List<File> thumbnails);
}
