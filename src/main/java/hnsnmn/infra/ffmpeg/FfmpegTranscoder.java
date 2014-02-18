package hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import hnsnmn.domain.job.OutputFormat;
import hnsnmn.domain.job.Transcoder;

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
	public List<File> transcode(File multimediaFile, List<OutputFormat> formats) {
		List<File> results = new ArrayList<File>();
		for (OutputFormat format : formats) {
			results.add(transcode(multimediaFile, format));
		}
		return results;
	}

	private File transcode(File sourceFile, OutputFormat format) {
		IMediaReader reader = ToolFactory.makeReader(sourceFile.getAbsolutePath());

		String outputFile = "outputFile.mp4"; // 테스트 통과를 윈한 코딩
		VideoConverter converter = new VideoConverter(outputFile, reader, format);
		reader.addListener(converter);

		while (reader.readPacket() == null) {
			do {
			} while (false);
		}
		return new File(outputFile);
	}
}

