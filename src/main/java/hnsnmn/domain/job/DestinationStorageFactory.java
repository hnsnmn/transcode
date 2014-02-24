package hnsnmn.domain.job;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 20.
 * Time: 오후 2:24
 * To change this template use File | Settings | File Templates.
 */
public interface DestinationStorageFactory {
	DestinationStorage create(String storage);
}
