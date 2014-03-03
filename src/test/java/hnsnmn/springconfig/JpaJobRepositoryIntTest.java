package hnsnmn.springconfig;

import hnsnmn.domain.job.*;
import hnsnmn.domain.job.destination.FileDestinationStorage;
import hnsnmn.domain.job.mediasource.LocalStorageMediaSourceFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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

	@Test
	public void save() {
		List<OutputFormat> outputFormats = new ArrayList<OutputFormat>();
		outputFormats.add(new OutputFormat(60, 40, 150, Container.MP4));

		Job job = new Job(
				new LocalStorageMediaSourceFile("file://./video.avi"),
				new FileDestinationStorage("file://./target"), outputFormats,
				new HttpResultCallback("http://"));
		Job savedJob = jobRepository.save(job);
		assertNotNull(savedJob);
		assertNotNull(savedJob.getId());
		assertJobsEquals(job, savedJob);
	}

	private void assertJobsEquals(Job job, Job savedJob) {
		assertEquals(job.getOutputFormats().size(), savedJob.getOutputFormats().size());
	}
}
