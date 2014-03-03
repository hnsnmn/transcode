package hnsnmn.domain.job;

import hnsnmn.infra.persistence.ExceptionMessageUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 6:09
 * To change this template use File | Settings | File Templates.
 */
public class Job {

	public static enum State {
		COMPLETED, MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING, WAITING
	}

	private Long id;
	private State state;
	private MediaSourceFile mediaSourceFile;
	private DestinationStorage destinationStorage;
	private List<OutputFormat> outputFormats;
	private ResultCallback callback;

	private String exceptionMessage;

	public Job(MediaSourceFile mediaSourceFile, DestinationStorage destinationStorage, List<OutputFormat> outputFormats, ResultCallback callback) {
		this(null, State.WAITING, mediaSourceFile, destinationStorage, outputFormats, callback, null);
	}

	public Job(Long id, State state, MediaSourceFile mediaSourceFile,
			   DestinationStorage destinationStorage, List<OutputFormat> outputFormats, ResultCallback callback, String errorMessage) {
		this.id = id;
		this.mediaSourceFile = mediaSourceFile;
		this.destinationStorage = destinationStorage;
		this.outputFormats = outputFormats;
		this.callback = callback;
		this.state = state;
		this.exceptionMessage = errorMessage;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public boolean isWaiting() {
		return state == State.WAITING;
	}

	public boolean isFinish() {
		return isSuccess() || isExceptionOccurred();
	}

	private boolean isExceptionOccurred() {
		return exceptionMessage != null;
	}

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	private void changeState(State state) {
		this.state = state;
	}

	private void exceptionOccured(RuntimeException ex) {
		exceptionMessage = ExceptionMessageUtil.getMessage(ex);
		callback.notifyFailedResult(id, state, exceptionMessage);
	}

	public boolean isExceptionOccured() {
		return exceptionMessage != null;
	}

	public Long getId() {
		return id;
	}

	public void transcode(Transcoder transcoder, ThumbnailExtractor thumbnailExtractor) {
		try {
			// 미디어 원본으로부터 파일을 로컬에 복사한다.
			File multimediaFile = copyMultimediaSourceToLocal();


			// 로컬에 복사된 파일을 변환처리한다.
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);

			// 로컬에 복사된 파일로부터 이미지를 추출한다.
			List<File> thumbnails = extractThumbnail(multimediaFile, thumbnailExtractor);


			// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장한다.
			storeCreatedFilesToDestination(multimediaFiles, thumbnails);

			// 결과를 통지한다.
			notifyJobResultToRequester();

			completed();
		} catch (RuntimeException ex) {
			exceptionOccured(ex);
			throw ex;
		}

	}

	private File copyMultimediaSourceToLocal() {
		changeState(State.MEDIASOURCECOPYING);
		return mediaSourceFile.getSourceFile();
	}

	private List<File> transcode(File mediaFile, Transcoder transcoder) {
		changeState(Job.State.TRANSCODING);
		return transcoder.transcode(mediaFile, outputFormats);
	}

	private List<File> extractThumbnail(File multimediaFile, ThumbnailExtractor thumbnailExtractor) {
		changeState(Job.State.EXTRACTINGTHUMBNAIL);
		return thumbnailExtractor.extract(multimediaFile, id);
	}

	private void storeCreatedFilesToDestination(List<File> multimediaFiles, List<File> thumbnails) {
		changeState(Job.State.STORING);
		destinationStorage.save(multimediaFiles, thumbnails);
	}

	private void notifyJobResultToRequester() {
		changeState(Job.State.NOTIFYING);
		callback.notifySuccessResult(id);
	}

	private void completed() {
		changeState(Job.State.COMPLETED);
	}

	public List<OutputFormat> getOutputFormats() {
		return Collections.unmodifiableList(outputFormats);
	}

	public <T> T exporter(Exporter<T> exporter) {
		exporter.addId(id);
		exporter.addState(state);
		exporter.addMediaSource(mediaSourceFile.getUrl());
		exporter.addDestinationStorage(destinationStorage.getUrl());
		exporter.addResultCallback(callback.getUrl());
		exporter.addOutputFormat(getOutputFormats());
		exporter.addExceptionMessage(exceptionMessage);
		return exporter.build();
	}

	public static interface Exporter<T> {
		public void addId(Long id);

		public void addState(Job.State state);

		public void addMediaSource(String url);

		public void addDestinationStorage(String url);

		public void addResultCallback(String url);

		public void addExceptionMessage(String exceptionMessage);

		public void addOutputFormat(List<OutputFormat> outputFormats);

		public T build();
	}
}
