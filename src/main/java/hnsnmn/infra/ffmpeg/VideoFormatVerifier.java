package hnsnmn.infra.ffmpeg;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import hnsnmn.domain.job.OutputFormat;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 18.
 * Time: 오전 11:09
 * To change this template use File | Settings | File Templates.
 */
public class VideoFormatVerifier {
	private final OutputFormat expectedFormat;
	private final File videoFile;
	private IContainer container;
	private ICodec.ID audioCodec;
	private ICodec.ID videoCodec;
	private int width;
	private int height;

	public static void verifyVideoFormat(OutputFormat expectedFormat,
										 File videoFile) {
		new VideoFormatVerifier(expectedFormat, videoFile).verify();
	}

	public VideoFormatVerifier(OutputFormat expectedFormat, File videoFile) {
		this.expectedFormat = expectedFormat;
		this.videoFile = videoFile;
	}

	private void verify() {
		try {
			assertExtension();
			makeContainer();
			extractMetaInfoOfVideo();
			assertVideoFile();
		} finally {
			closeContainer();
		}
	}

	private void assertExtension() {
		assertEquals(expectedFormat.getFileExtension(), fileExtensiton());
	}

	private String fileExtensiton() {
		String filePath = videoFile.getAbsolutePath();
		int lastDotIdx = filePath.lastIndexOf(".");
		String extension = filePath.substring(lastDotIdx + 1);
		return extension;
	}

	private void makeContainer() {
		container = IContainer.make();
		int openResult = container.open(videoFile.getAbsolutePath(),
				IContainer.Type.READ, null);
		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed:" + openResult);
		}
	}

	private void extractMetaInfoOfVideo() {
		int numStreams = container.getNumStreams();
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				audioCodec = coder.getCodecID();
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoCodec = coder.getCodecID();
				width = coder.getWidth();
				height = coder.getHeight();
			}
		}
	}

	private void assertVideoFile() {
		assertEquals(expectedFormat.getWidth(), width);
		assertEquals(expectedFormat.getHeight(), height);
		assertEquals(expectedFormat.getVideoCodec(),
				CodecValueConverter.toDomainVideoCodec(videoCodec));
		assertEquals(expectedFormat.getAudioCodec(),
				CodecValueConverter.toDomainAudioCodec(audioCodec));
	}

	private void closeContainer() {
		if (container != null)
			container.close();
	}
}
