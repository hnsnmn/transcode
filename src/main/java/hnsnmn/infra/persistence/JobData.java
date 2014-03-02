package hnsnmn.infra.persistence;

import hnsnmn.domain.job.Job;
import hnsnmn.domain.job.OutputFormat;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 25.
 * Time: 오전 8:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "JOB")
public class JobData {
	@Id
	@Column(name = "JOB_ID")
	@TableGenerator(name = "JOB_ID_GEN", table = "ID_GENERATOR", pkColumnName = "TABLE_NAME", valueColumnName = "ID_VALUE")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "JOB_ID_GEN")
	private Long id;

	@Column(name = "STATE")
	@Enumerated(EnumType.STRING)
	private Job.State state;

	@Column(name = "SOURCE_URL")
	private String sourceUrl;

	@Column(name = "DESTINATION_URL")
	private String destinationUrl;

	@Column(name = "CALLBACK_URL")
	private String callbackUrl;

	@Column(name = "EXCEPTION_MESSAGE")
	private String exceptionMessage;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "JOB_OUTPUTFORMAT", joinColumns = { @JoinColumn(name = "JOB_ID") })
	@OrderColumn(name = "LIST_IDX")
	private List<OutputFormat> outputFormats;

	public Long getId() {
		return id;
	}

	public Job.State getState() {
		return state;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public List<OutputFormat> getOutputFormats() {
		return outputFormats;
	}

	public static class ExporterToJobData implements Job.Exporter {
		private JobData jobData = new JobData();

		@Override
		public void addId(Long id) {
			jobData.id = id;
		}

		@Override
		public void addState(Job.State state) {
			jobData.state = state;
		}

		@Override
		public void addMediaSource(String url) {
			jobData.sourceUrl = url;
		}

		@Override
		public void addDestinationStorage(String url) {
			jobData.destinationUrl = url;
		}

		@Override
		public void addResultCallback(String url) {
			jobData.callbackUrl = url;
		}

		@Override
		public void addExceptionMessage(String exceptionMessage) {
			jobData.exceptionMessage = exceptionMessage;
		}

		@Override
		public void addOutputFormat(List<OutputFormat> outputFormats) {
			jobData.outputFormats = outputFormats;
		}

		public JobData getJobData() {
			return jobData;
		}
	}
}