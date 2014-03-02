package hnsnmn.springconfig;

import hnsnmn.domain.job.Job;
import hnsnmn.domain.job.JobRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 28.
 * Time: 오후 4:50
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfig.class })
public class JpaJobRepositoryIntTest {

	@Autowired
	private JobRepository jobRepository;

	@Test
	public void findById() {
		Job job = jobRepository.findById(1L);
		assertNotNull(job);
		assertTrue(job.isWaiting());
		assertEquals(2, job.getOutputFormats().size());
	}
}
