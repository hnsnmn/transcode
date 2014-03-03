package hnsnmn.springconfig;

import hnsnmn.domain.job.ResultCallbackFactory;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: hongseongmin
 * Date: 2014. 2. 26.
 * Time: 오후 6:14
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationContextConfigTest {
	@Test
	public void getBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationContextConfig.class
		);
		assertNotNull(context.getBean("dataSource", DataSource.class));
		assertNotNull(context.getBean("resultCallbackFactory", ResultCallbackFactory.class));
	}

}
