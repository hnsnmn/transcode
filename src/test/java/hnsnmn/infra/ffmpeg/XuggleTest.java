package hnsnmn.infra.ffmpeg;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.*;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 18.
 * Time: 오전 9:39
 * To change this template use File | Settings | File Templates.
 */
public class XuggleTest {
	@Test
	public void getMetadataOfExistingAVIFile() {
		IContainer container = IContainer.make();
		int openResult = container.open("src/test/resources/sample.avi", IContainer.Type.READ, null);

		if (openResult < 0) {
			throw new RuntimeException("Xuggler file open failed:" + openResult);
		}

		int numStreams = container.getNumStreams();
		System.out.printf("file \"%s\": %d stream%s;", "src/test/resources/sample.avi", numStreams, numStreams == 1 ? "":"s");
		System.out.printf("bit rate: %d;", container.getBitRate());
		System.out.printf("\n");

		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			System.out.printf("stream %d:", i);
			System.out.printf("type:%s;", coder.getCodecType());

			System.out.printf("codec:%s;", coder.getCodecID());
			System.out.printf("duration:%s;", stream.getDuration() == Global.NO_PTS ? "unknown" : "" + stream.getDuration());
			System.out.printf("start time:%s;", container.getStartTime() == Global.NO_PTS ? "unknown" : "" + stream.getStartTime());
			System.out.printf("timebase:%d/%d;", stream.getTimeBase().getNumerator(), stream.getTimeBase().getDenominator());
			System.out.printf("coder tb:%d/%d;", coder.getTimeBase().getNumerator(), coder.getTimeBase().getDenominator());

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				System.out.printf("sample rate:%d", coder.getSampleRate());
				System.out.printf("channels:%d", coder.getChannels());
				System.out.printf("format:%s", coder.getSampleFormat());
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				System.out.printf("width:%d", coder.getWidth());
				System.out.printf("height:%d", coder.getHeight());
				System.out.printf("format:%s", coder.getPixelType());
				System.out.printf("frame-rate:%5.2f;", coder.getFrameRate().getDouble());
			}
			System.out.printf("\n");
		}
		container.close();
	}

	@Test
	public void transcode() {
		IMediaReader reader = ToolFactory.makeReader("src/test/resources/sample.avi");

		IMediaWriter writer = ToolFactory.makeWriter("target/sameple.mp4", reader);
		reader.addListener(writer);
		while (reader.readPacket() == null) {
			do {
			} while (false);
		}
	}

}
