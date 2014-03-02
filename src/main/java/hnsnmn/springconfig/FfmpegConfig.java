package hnsnmn.springconfig;

import hnsnmn.domain.job.Transcoder;
import hnsnmn.infra.ffmpeg.FfmpegTranscoder;
import hnsnmn.infra.ffmpeg.NamingRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 6:15
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class FfmpegConfig {

	@Bean
	public Transcoder transcoder() {
		return new FfmpegTranscoder(NamingRule.DEFAULT);
	}
}
