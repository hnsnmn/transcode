package hnsnmn;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 12.
 * Time: 오후 6:09
 * To change this template use File | Settings | File Templates.
 */
public class Job {

	private Long id;

	private State state;

	private Exception occurredException;

	public Job(Long id) {
		this.id = id;
	}

	public Exception getOccuredException() {
		return occurredException;
	}

	public boolean isWaiting() {
		return state == null;
	}

	public boolean isFinish() {
		return isSuccess() || isExceptionOccurred();
	}

	private boolean isExceptionOccurred() {
		return occurredException != null;
	}

	public boolean isSuccess() {
		return state == State.COMPLETED;
	}

	public State getLastState() {
		return state;
	}

	public void changeState(State state) {
		this.state = state;
	}

	public void exceptionOccured(RuntimeException ex) {
		occurredException = ex;
	}

	public static enum State {
		COMPLETED,
		MEDIASOURCECOPYING, TRANSCODING, EXTRACTINGTHUMBNAIL, STORING, NOTIFYING;

	}

	public void transcode(MediaSourceCopier mediaSourceCopier,
						  Transcoder transcoder, ThumbnailExtractor thumbnailExtractor,
						  CreatedFileSender createdFileSender, JobResultNotifier jobResultNotifier) {
		try {
			changeState(State.MEDIASOURCECOPYING);
			// 미디어 원본으로부터 파일을 로컬에 복사한다.
			File multimediaFile = copyMultimediaSourceToLocal(mediaSourceCopier);


			changeState(Job.State.TRANSCODING);
			// 로컬에 복사된 파일을 변환처리한다.
			List<File> multimediaFiles = transcode(multimediaFile, transcoder);

			changeState(Job.State.EXTRACTINGTHUMBNAIL);
			// 로컬에 복사된 파일로부터 이미지를 추출한다.
			List<File> thumbnails = extractThumbnail(multimediaFile, thumbnailExtractor);


			changeState(Job.State.STORING);
			// 변환된 결과 파일과 썸네일 이미지를 목적지에 저장한다.
			storeCreatedFilesToDestination(multimediaFiles, thumbnails, createdFileSender);

			changeState(Job.State.NOTIFYING);
			// 결과를 통지한다.
			notifyJobResultToRequester(jobResultNotifier);
			changeState(Job.State.COMPLETED);
		} catch (RuntimeException ex) {
			exceptionOccured(ex);
			throw ex;
		}

	}

	private File copyMultimediaSourceToLocal(MediaSourceCopier mediaSourceCopier) {
		return mediaSourceCopier.copy(id);
	}

	private List<File> transcode(File mediaFile, Transcoder transcoder) {
		return transcoder.transcode(mediaFile, id);
	}

	private void notifyJobResultToRequester(JobResultNotifier jobResultNotifier) {
		jobResultNotifier.notifyToRequest(id);
	}

	private void storeCreatedFilesToDestination(List<File> multimediaFiles, List<File> thumbnails, CreatedFileSender createdFileSender) {
		createdFileSender.store(multimediaFiles, thumbnails, id);
	}

	private List<File> extractThumbnail(File multimediaFile, ThumbnailExtractor thumbnailExtractor) {
		return thumbnailExtractor.extract(multimediaFile, id);
	}
}
