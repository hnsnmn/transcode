package hnsnmn.infra.ffmpeg;

import hnsnmn.FfmpegTranscoder;
import hnsnmn.OutputFormmat;
import hnsnmn.Transcoder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 17.
 * Time: 오후 10:36
 * To change this template use File | Settings | File Templates.
 */
public class FfmpegTranscoderTest {
	private Transcoder transcoder;

	@Before
	public void setUp() {
		transcoder = new FfmpegTranscoder();
	}

	@Test
	public void transcodeWithOneOuputFormmat() {
		File multimediaFile = new File(".");
		List<OutputFormmat> outputFormmats = new ArrayList<OutputFormmat>();
		outputFormmats.add(new OutputFormmat(640, 480, 300, "h264", "aac"));
		List<File> transcodedFiles = transcoder.transcode(multimediaFile, outputFormmats);

		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());

	}

}
