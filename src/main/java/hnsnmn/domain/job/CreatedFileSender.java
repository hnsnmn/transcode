package hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 3:38
 * To change this template use File | Settings | File Templates.
 */
public interface CreatedFileSender {
	void store(List<File> multimediaFiles, List<File> thumbnails, Long jobId);
}
