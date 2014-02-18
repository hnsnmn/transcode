package hnsnmn.infra.ffmpeg;

import hnsnmn.OutputFormmat;
import hnsnmn.Transcoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 17.
* Time: 오후 10:53
* To change this template use File | Settings | File Templates.
*/
public class FfmpegTranscoder implements Transcoder {
	@Override
	public List<File> transcode(File mockMultimediaFile, List<OutputFormmat> outputFormmats) {
		List<File> results = new ArrayList<File>();
		for (OutputFormmat formmat : outputFormmats) {
			results.add(new File("."));
		}
		return results;
	}
}

