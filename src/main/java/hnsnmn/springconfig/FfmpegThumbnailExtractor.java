package hnsnmn.springconfig;

import hnsnmn.domain.job.ThumbnailExtractor;
import hnsnmn.domain.job.ThumbnailPolicy;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 3.
 * Time: 오후 6:12
 * To change this template use File | Settings | File Templates.
 */
public class FfmpegThumbnailExtractor implements ThumbnailExtractor {
	@Override
	public List<File> extract(File mockMultimediaFile, ThumbnailPolicy thumbnailPolicy) {
		return Collections.emptyList();
	}
}
