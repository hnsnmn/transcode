package hnsnmn.infra.ffmpeg;

import it.sauronsoftware.jave.*;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 4.
 * Time: 오후 1:55
 * To change this template use File | Settings | File Templates.
 */
public class JavaUsageTest {

	@Test
	public void getInfo() throws EncoderException {
		Encoder encoder = new Encoder();
		MultimediaInfo info = encoder.getInfo(new File("src/test/resources/sample.mp4"));
		System.out.println(info.getFormat());
		System.out.println(info.getAudio().getDecoder());
		System.out.println(info.getVideo().getDecoder());
	}

	@Test
	public void encodeAviToMp4() throws EncoderException {
		File source = new File("src/test/resources/sample.avi");
		File target = new File("target/sample.mp4");

//		AudioAttributes audio = new AudioAttributes();
//		audio.setCodec("libfaac");
//		audio.setBitRate(new Integer(64000));
//		audio.setChannels(new Integer(1));
//		audio.setSamplingRate(new Integer(22050));


		VideoAttributes video = new VideoAttributes();
//		video.setCodec("libx264");
//		video.setBitRate(new Integer(160000));
//		video.setFrameRate(new Integer(15));
		video.setSize(new VideoSize(400, 300));

		EncodingAttributes attributes = new EncodingAttributes();
		attributes.setFormat("mp4");
		attributes.setVideoAttributes(video);

		Encoder encoder = new Encoder();
		encoder.encode(source, target, attributes);
	}
}
