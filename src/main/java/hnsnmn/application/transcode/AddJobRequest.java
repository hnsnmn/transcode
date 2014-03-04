package hnsnmn.application.transcode;

import hnsnmn.domain.job.OutputFormat;
import hnsnmn.domain.job.ThumbnailPolicy;

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
	private ThumbnailPolicy thumbnailPolicy;
	private String resultCallback;


	private void setMediaSourceFile(String mediaSourceFile) {
		this.mediaSourceFile = mediaSourceFile;
	}

	public String getMediaSource() {
		return mediaSourceFile;
	}

	private void setDestinationStorage(String destinationStorage) {
		this.destinationStorage = destinationStorage;
	}

	public String getDestinationStorage() {
		return destinationStorage;
	}

	public void setOutputFormats(List<OutputFormat> outputFormats) {
		this.outputFormats = outputFormats;
	}

	public List<OutputFormat> getOutputFormats() {
		return outputFormats;
	}

	public void setThumbnailPolicy(ThumbnailPolicy thumbnailPolicy) {
		this.thumbnailPolicy = thumbnailPolicy;
	}
	public ThumbnailPolicy getThumbnailPolicy() {
		return thumbnailPolicy;
	}

	public void setResultCallback(String resultCallback) {
		this.resultCallback = resultCallback;
	}

	public String getResultCallback() {
		return resultCallback;  //To change body of created methods use File | Settings | File Templates.
	}

	@Override
	public String toString() {
		return "AddJobRequest [mediaSource=" + mediaSourceFile
				+ ", destinationStorage=" + destinationStorage
				+ ", outputFormats=" + outputFormats + ", thumbnailPolicy="
				+ thumbnailPolicy + ", resultCallback=" + resultCallback + "]";
	}
}
