package hnsnmn.infra.persistence;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 20.
* Time: 오전 11:06
* To change this template use File | Settings | File Templates.
*/
public abstract class ExceptionMessageUtil {
	public static String getMessage(RuntimeException ex) {
		return ex.getMessage() == null ? ex.getClass().getName() : ex.getMessage();
	}
}
