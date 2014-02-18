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

	@Before
	public void setUp() {
		transcoder = new FfmpegTranscoder();
	}

	@Test
	public void transcodeWithOneOuputFormmat() {
		File multimediaFile = new File("src/test/resources/sample.avi");
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.MP4,
				VideoCodec.H264, AudioCodec.AAC));

		List<File> transcodedFiles = transcoder.transcode(multimediaFile, outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0), transcodedFiles.get(0));
//		verifyTranscodedFile(outputFormats.get(0), transcodedFiles.get(0));
	}

	@Test
	public void transcodeWithOneAviOutputFormat() {
		File multimediaFile = new File("src/test/resources/sample.avi");
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(WIDTH, HEIGHT, BITRATE, Container.AVI,
				VideoCodec.MPEG4, AudioCodec.MP3));

		List<File> transcodedFiles = transcoder.transcode(multimediaFile, outputFormats);
		assertEquals(1, transcodedFiles.size());
		assertTrue(transcodedFiles.get(0).exists());
		VideoFormatVerifier.verifyVideoFormat(outputFormats.get(0), transcodedFiles.get(0));
	}
//	private void verifyTranscodedFile(OutputFormat outputFormmat, File file) {
//		IContainer container = IContainer.make();
//		int openResult = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
//
//		if (openResult < 0) {
//			throw new RuntimeException("Xubbler file open failed:" + openResult);
//		}
//
//		int numStreams = container.getNumStreams();
//
//		int width = 0;
//		int height = 0;
//		ICodec.ID videoCodec = null;
//		ICodec.ID audioCodec = null;
//
//		for (int i = 0; i < numStreams; i++) {
//			IStream stream = container.getStream(i);
//			IStreamCoder coder = stream.getStreamCoder();
//
//			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
//				audioCodec = coder.getCodecID();
//			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
//				videoCodec = coder.getCodecID();
//				width = coder.getWidth();
//				height = coder.getHeight();
//			}
//		}
//		container.close();
//
//		assertEquals(outputFormmat.getWidth(), width);
//		assertEquals(outputFormmat.getHeight(), height);
//		assertEquals(outputFormmat.getVideoCodec(), videoCodec.toString());
//		assertEquals(outputFormmat.getAudioCodec(), audioCodec.toString());
//	}

}
