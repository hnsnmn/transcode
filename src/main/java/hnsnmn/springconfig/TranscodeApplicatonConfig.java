package hnsnmn.springconfig;

import hnsnmn.application.transcode.jobqueue.MemoryJobQueue;
import hnsnmn.application.transcode.*;
import hnsnmn.domain.job.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 27.
 * Time: 오후 9:27
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class TranscodeApplicatonConfig {

	@Autowired
	private MediaSourceFileFactory mediaSourceFileFactory;
	@Autowired
	private DestinationStorageFactory destinationStorageFactory;
	@Autowired
	private ResultCallbackFactory resultCallbackfactory;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private Transcoder transcoder;
	@Autowired
	private ThumbnailExtractor thumbnailExtractor;

	@Bean
	public TranscodingRunner transcodingRunner() {
		return new TranscodingRunner(transcodingService(), jobQueue());
	}

	@Bean
	public AddJobService addJobService() {
		return new AddJobServiceImpl(mediaSourceFileFactory,
				destinationStorageFactory, resultCallbackfactory,
				jobRepository, jobQueue());
	}

	@Bean
	public TranscodingService transcodingService() {
		return new TranscodingServiceImpl(transcoder, thumbnailExtractor, jobRepository);
	}

	private JobQueue jobQueue() {
		return new MemoryJobQueue();
	}
}
