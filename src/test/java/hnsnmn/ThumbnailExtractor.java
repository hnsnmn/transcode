package hnsnmn;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 3:10
 * To change this template use File | Settings | File Templates.
 */
public interface ThumbnailExtractor {
	List<File> extract(File mockMultimediaFile, Long jobId);
}
