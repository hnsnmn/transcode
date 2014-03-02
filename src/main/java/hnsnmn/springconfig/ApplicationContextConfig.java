package hnsnmn.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
* Created with IntelliJ IDEA.
* User: hongseongmin
* Date: 2014. 2. 26.
* Time: 오후 6:18
* To change this template use File | Settings | File Templates.
*/
@Configuration
@Import({ RepositoryConfig.class, JpaConfig.class})
@ImportResource("classpath:spring/datasource.xml")
public class ApplicationContextConfig {
}
