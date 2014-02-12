package hnsnmn;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 11.
 * Time: 오후 6:16
 * To change this template use File | Settings | File Templates.
 */
public interface MediaSourceCopier {
	File copy(Long jobId);
}
