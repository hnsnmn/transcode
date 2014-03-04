package hnsnmn.springconfig;

import hnsnmn.domain.job.ResultCallbackFactory;
import hnsnmn.domain.job.callback.DefaultResultCallbackFactory;
import hnsnmn.domain.job.DestinationStorageFactory;
import hnsnmn.domain.job.MediaSourceFileFactory;
import hnsnmn.domain.job.destination.DefaultDestinationStorageFactory;
import hnsnmn.domain.job.mediasource.DefaultMediaSourceFileFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 6:10
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class DomainConfig {
	@Bean
	public ResultCallbackFactory resultCallbackFactory() {
		return new DefaultResultCallbackFactory();
	}

	@Bean
	public DestinationStorageFactory destinationStorageFactory() {
		return new DefaultDestinationStorageFactory();
	}

	@Bean
	public MediaSourceFileFactory mediaSourceFileFactory() {
		return new DefaultMediaSourceFileFactory();
	}
}
