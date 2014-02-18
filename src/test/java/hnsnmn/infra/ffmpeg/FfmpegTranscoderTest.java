package hnsnmn.infra.ffmpeg;

import hnsnmn.domain.job.*;
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
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final int BITRATE = 300;
	private Transcoder transcoder;
	private File multimediaFile;
	private List<OutputFormat> outputFormats;

	private OutputFormat mp4Format;
	private OutputFormat mp4Format2;
	private OutputFormat aviFormat;

	@Before
	public void setUp() {
		transcoder = new FfmpegTranscoder(NamingRule.DEFAULT);
		multimediaFile = new File("src/test/resources/sample.avi");
		outputFormats = new ArrayList<OutputFormat>();
		mp4Format = new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.MP4,
				VideoCodec.H264, AudioCodec.AAC);
		mp4Format2 = new OutputFormat(80, 60, 80, Container.MP4,
				VideoCodec.H264, AudioCodec.AAC);
		aviFormat = new OutputFormat(80, 60, 80, Container.AVI,
				VideoCodec.MPEG4, AudioCodec.MP3);
	}

	@Test
	public void transcodeWithOneOuputFormat() {
		outputFormats.add(mp4Format);
		excuteTranscodeAndAssert();
	}

	@Test
	public void transcodeWithOneAviOutputFormat() {
		outputFormats.add(aviFormat);
		excuteTranscodeAndAssert();
	}

	@Test
	public void transcodeWithTwoMp4OutputFormats() {
		outputFormats.add(mp4Format);
		outputFormats.add(mp4Format2);
		excuteTranscodeAndAssert();
	}

	private void excuteTranscodeAndAssert() {
		List<File> transcodedFiles = transcoder.transcode(multimediaFile, outputFormats);
		assertEquals(outputFormats.size(), transcodedFiles.size());
		for (int i = 0; i < outputFormats.size(); i++) {
			assertTrue(transcodedFiles.get(i).exists());
			VideoFormatVerifier.verifyVideoFormat(outputFormats.get(i), transcodedFiles.get(i));
		}
	}
}
