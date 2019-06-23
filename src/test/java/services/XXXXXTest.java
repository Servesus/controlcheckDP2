
package services;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;

import utilities.AbstractTest;
import domain.Application;
import domain.XXXXX;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class XXXXXTest extends AbstractTest {

	@Autowired
	private XXXXXService		service;

	@Autowired
	private ApplicationService	applicationService;


	@Test
	public void createXXXXXDriver() {
		final Object testingData[][] = {
			{
				"prueba", "http://picture.com", "rookie1", "application1", null
			}, {
				"", "http://picture.com", "rookie1", "application1", ValidationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.createXXXXXTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], super.getEntityId((String) testingData[i][3]), (Class<?>) testingData[i][4]);
	}

	private void createXXXXXTemplate(final String body, final String picture, final String rookie, final int applicationId, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(rookie);
			final Application a = this.applicationService.findOne(applicationId);
			XXXXX result = this.service.create();
			result.setBody(body);
			result.setPicture(picture);
			result.setApplication(a);
			final DataBinder binding = new DataBinder(new XXXXX());
			result = this.service.reconstruct(result, binding.getBindingResult());
			this.service.save(result);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
