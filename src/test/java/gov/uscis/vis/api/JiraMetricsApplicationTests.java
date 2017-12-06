package gov.uscis.vis.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {
		JiraMetricsMain.class
})
public class JiraMetricsApplicationTests {

	@Test
	public void contextLoads() {
	}

}
