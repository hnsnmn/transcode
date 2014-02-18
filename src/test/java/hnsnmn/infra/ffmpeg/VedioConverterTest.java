package hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import hnsnmn.domain.job.Container;
import hnsnmn.domain.job.OutputFormat;
import hnsnmn.domain.job.AudioCodec;
import hnsnmn.domain.job.VideoCodec;
import org.junit.Before;
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
	private IMediaReader reader;
	private OutputFormat outputFormat;
	private String outputFile;

	@Before
	public void setUp() {
		reader = ToolFactory.makeReader(SOURCE_FILE);
	}

	@Test
	public void transcode() {
		InitOutput("target/sample.mp4", Container.MP4, VideoCodec.H264, AudioCodec.AAC);
		testVideoConverter();
	}

	@Test
	public void transcodeWithOnlyContainer() {
		InitOutput("target/sample.avi", Container.AVI);
		testVideoConverter();
	}

	private void InitOutput(String transcodedFile, Container container, VideoCodec videoCodec, AudioCodec audioCodec) {
		if (videoCodec == null && audioCodec == null) {
			outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, container);
		} else {
			outputFormat = new OutputFormat(WIDTH, HEIGHT, BITRATE, container, videoCodec, audioCodec);
		}
		outputFile = transcodedFile;
	}

	private void InitOutput(String transcodedFile, Container container) {
		InitOutput(transcodedFile, container, null, null);
	}

	private void testVideoConverter() {
		VideoConverter writer = new VideoConverter(outputFile, reader, outputFormat);
		reader.addListener(writer);
		while(reader.readPacket() == null) {
			do {
			} while(false);
		}

		VideoFormatVerifier.verifyVideoFormat(outputFormat, new File(outputFile));
	}
}
