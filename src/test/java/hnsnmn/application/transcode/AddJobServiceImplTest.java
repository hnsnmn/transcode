package hnsnmn.application.transcode;


import hnsnmn.domain.job.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 20.
 * Time: 오후 2:06
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddJobServiceImplTest {

	@Mock private MediaSourceFile mockMediaSourceFile;
	@Mock private MediaSourceFileFactory mediaSourceFileFactory;
	@Mock private DestinationStorage mockDestinationStorage;
	@Mock private DestinationStorageFactory destinationStorageFactory;
	@Mock private JobRepository jobRepository;

	@Test
	public void addJob() {
		AddJobRequest request = new AddJobRequest();
		when(mediaSourceFileFactory.create(request.getMediaSource())).thenReturn(mockMediaSourceFile);
		when(destinationStorageFactory.create(request.getDestinationStorage())).thenReturn(mockDestinationStorage);

		final Long mockJobId = new Long(1);
		Job mockSavedJob = mock(Job.class);
		when(mockSavedJob.getId()).thenReturn(mockJobId);
		when(jobRepository.save(any(Job.class))).thenReturn(mockSavedJob);

		AddJobService addJobService = new AddJobServiceImpl(
				mediaSourceFileFactory, destinationStorageFactory,
				jobRepository);
		Long jobId = addJobService.addJob(request);
		assertNotNull(jobId);
	}

	private class AddJobRequest {
		private String mediaSourceFile;
		private String destinationStorage;
		private List<OutputFormat> outputFormats;

		public String getMediaSource() {
			return mediaSourceFile;
		}

		public String getDestinationStorage() {
			return destinationStorage;
		}

		public List<OutputFormat> getOutputFormats() {
			return outputFormats;
		}

		private void setMediaSourceFile(String mediaSourceFile) {
			this.mediaSourceFile = mediaSourceFile;
		}

		private void setDestinationStorage(String destinationStorage) {
			this.destinationStorage = destinationStorage;
		}
		public void setOutputFormats(List<OutputFormat> outputFormats) {
			this.outputFormats = outputFormats;
		}
	}

	private class AddJobServiceImpl implements AddJobService {
		private final MediaSourceFileFactory mediaSourceFileFactory;
		private final DestinationStorageFactory destinationStorageFactory;
		private final JobRepository jobRepository;

		@Override
		public Long addJob(AddJobRequest request) {
			Job job = createJob(request);
			Job savedJob = saveJob(job);
			return savedJob.getId();
		}

		private Job saveJob(Job job) {
			return jobRepository.save(job);
		}

		private Job createJob(AddJobRequest request) {
			MediaSourceFile mediaSourceFile = mediaSourceFileFactory.create(request.getMediaSource());
			DestinationStorage destinationStorage = destinationStorageFactory.create(request.getDestinationStorage());
			return new Job(mediaSourceFile, destinationStorage, request.getOutputFormats());
		}

		public AddJobServiceImpl(MediaSourceFileFactory mediaSourceFileFactory, DestinationStorageFactory destinationStorageFactory, JobRepository jobRepository) {
			this.mediaSourceFileFactory = mediaSourceFileFactory;
			this.destinationStorageFactory = destinationStorageFactory;
			this.jobRepository = jobRepository;
		}
	}

	private interface AddJobService {
		Long addJob(AddJobRequest request);
	}
}
