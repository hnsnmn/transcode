package hnsnmn.application.transcode;

import hnsnmn.domain.job.OutputFormat;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 24.
* Time: 오전 9:46
* To change this template use File | Settings | File Templates.
*/
class AddJobRequest {
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

	public String getResultCallback() {
		return null;  //To change body of created methods use File | Settings | File Templates.
	}
}
