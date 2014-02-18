package hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 18.
 * Time: 오후 3:05
 * To change this template use File | Settings | File Templates.
 */
public enum Container {
	MP4(VideoCodec.H264, AudioCodec.AAC, "mp4"),
	AVI(VideoCodec.MPEG4, AudioCodec.MP3, "avi");

	private VideoCodec defaultVideoCodec;
	private AudioCodec defaultAudioCodec;
	private String fileExtension;

	private Container(VideoCodec defaultVideoCodec, AudioCodec defaultAudioCodec, String fileExtension) {
		this.defaultVideoCodec = defaultVideoCodec;
		this.defaultAudioCodec = defaultAudioCodec;
		this.fileExtension = fileExtension;
	}

	public VideoCodec getDefaultVideoCodec() {
		return defaultVideoCodec;
	}

	public AudioCodec getDefaultAudioCodec() {
		return defaultAudioCodec;
	}

	public String getFileExtension() {
		return fileExtension;
	}
}
