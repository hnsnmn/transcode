package hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import hnsnmn.OutputFormat;
import hnsnmn.domain.job.AudioCodec;
import hnsnmn.domain.job.VideoCodec;
import org.junit.Test;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 18.
 * Time: 오전 11:01
 * To change this template use File | Settings | File Templates.
 */
public class VedioConverterTest {

	public static final String SOURCE_FILE = "src/test/resources/sample.avi";
	public static final int WIDTH = 160;
	public static final int HEIGHT = 120;
	public static final int BITRATE = 150;
	public static final String TRANSCODED_FILE = "target/sample.mp4";

	@Test
	public void transcode() {
		IMediaReader reader = ToolFactory.makeReader(SOURCE_FILE);
		OutputFormat outputFormmat = new OutputFormat(WIDTH, HEIGHT, BITRATE, VideoCodec.H264, AudioCodec.AAC);
		VideoConverter writer = new VideoConverter(TRANSCODED_FILE, reader, outputFormmat);
		reader.addListener(writer);
		while(reader.readPacket() == null) {
			do {
			} while(false);
		}

		VideoFormatVerifier.verifyVideoFormat(outputFormmat, new File(TRANSCODED_FILE));
	}
}
