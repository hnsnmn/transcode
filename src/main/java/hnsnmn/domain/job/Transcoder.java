package hnsnmn.domain.job;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 1:48
 * To change this template use File | Settings | File Templates.
 */
public interface Transcoder {
	List<File> transcode(File mockMultimediaFile, List<OutputFormat> formats);
}
