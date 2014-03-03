package hnsnmn.springconfig;

import hnsnmn.domain.job.DestinationStorageFactory;
import hnsnmn.domain.job.JobRepository;
import hnsnmn.domain.job.MediaSourceFileFactory;
import hnsnmn.domain.job.ResultCallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 8:57
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class RepositoryConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;

	@Bean
	public JobRepository jobRepository() {
		return new JpaJobRepository(mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackFactory);
	}
}
