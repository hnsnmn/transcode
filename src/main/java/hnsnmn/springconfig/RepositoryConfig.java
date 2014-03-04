package hnsnmn.springconfig;

import hnsnmn.domain.job.DestinationStorageFactory;
import hnsnmn.domain.job.JobRepository;
import hnsnmn.domain.job.MediaSourceFileFactory;
import hnsnmn.domain.job.ResultCallbackFactory;
import hnsnmn.infra.persistence.DbJobRepository;
import hnsnmn.infra.persistence.JobDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 8:57
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableJpaRepositories(basePackages = "hnsnmn.infra.persistence")
public class RepositoryConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackFactory;
	@Autowired
	private JobDataDao jobDataDao;

	@Bean
	public JobRepository jobRepository() {
		return new DbJobRepository(jobDataDao, mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackFactory);
	}
}
