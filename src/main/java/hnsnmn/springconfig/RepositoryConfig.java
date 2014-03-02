package hnsnmn.springconfig;

import hnsnmn.domain.job.JobRepository;
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

	@Bean
	public JobRepository jobRepository() {
		return new JpaJobRepository();
	}
}
