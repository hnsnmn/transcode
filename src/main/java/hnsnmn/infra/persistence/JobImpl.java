package hnsnmn.infra.persistence;

import hnsnmn.domain.job.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 3. 3.
 * Time: 오후 2:04
 * To change this template use File | Settings | File Templates.
 */
public class JobImpl extends Job {
	private JobDataDao jobDataDao;

	public JobImpl(JobDataDao jobDataDao, Long id, State state,
				   MediaSourceFile mediaSourceFile,
				   DestinationStorage destinationStorage,
				   List<OutputFormat> outputFormats,
				   ResultCallback callback,
				   ThumbnailPolicy thumbnailPolicy,
				   String errorMessage) {
		super(id, state, mediaSourceFile, destinationStorage, outputFormats,
				callback, thumbnailPolicy, errorMessage);
		this.jobDataDao = jobDataDao;
	}

	@Override
	protected void changeState(State newState) {
		super.changeState(newState);
		jobDataDao.updateState(getId(), newState);
	}

	@Override
	protected void exceptionOccured(RuntimeException ex) {
		String exceptionMessage = ExceptionMessageUtil.getMessage(ex);
		jobDataDao.updateExceptionMessage(getId(), exceptionMessage);

		super.exceptionOccured(ex);
	}
}
