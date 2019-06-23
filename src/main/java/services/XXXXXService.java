
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.XXXXXRepository;
import domain.XXXXX;

@Service
@Transactional
public class XXXXXService {

	@Autowired
	private XXXXXRepository	repository;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private RookieService	rookieService;
	@Autowired
	private Validator		validator;


	public XXXXX reconstruct(final XXXXX object, final BindingResult binding) {
		XXXXX result;
		if (object.getId() == 0) {
			result = this.create();
			result.setTicker(XXXXXService.generadorDeTickers());
			result.setIsFinal(false);
			result.setApplication(object.getApplication());
		} else
			result = this.repository.findOne(object.getId());
		result.setBody(object.getBody());
		result.setPicture(object.getPicture());

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}

	public XXXXX create() {
		XXXXX result;
		result = new XXXXX();
		result.setIsFinal(false);
		result.setTicker(XXXXXService.generadorDeTickers());
		return result;
	}

	public Collection<XXXXX> findAll() {
		return this.repository.findAll();
	}

	public XXXXX findOne(final Integer arg0) {
		return this.repository.findOne(arg0);
	}

	public XXXXX save(final XXXXX object) {
		Assert.notNull(object);
		Assert.isTrue(object.getApplication().getRookie().equals(this.rookieService.findOne(this.actorService.getActorLogged().getId())));
		Assert.isTrue(object.getApplication().getStatus().equals("SUBMITTED"));
		if (object.getIsFinal() == true)
			object.setMoment(new Date());

		XXXXX result;

		result = this.repository.save(object);
		return result;
	}

	public void delete(final XXXXX object) {
		Assert.notNull(object);
		Assert.isTrue(object.getIsFinal() == false);
		Assert.isTrue(this.rookieService.findOne(this.actorService.getActorLogged().getId()).equals(object.getApplication().getRookie()));
		this.repository.delete(object);
	}

	public void delete2(final XXXXX x) {
		this.repository.delete(x);
	}

	public static String generadorDeTickers() {
		String dateRes = "";
		String numericRes = "";
		final String alphanumeric = "0123456789";
		dateRes = new SimpleDateFormat("yy-MMdd").format(Calendar.getInstance().getTime());
		final Random in = new Random();
		final Integer a = 2;
		final Integer b = 5;
		final Integer contador = in.nextInt(b - a) + a;

		for (int i = 0; i < contador; i++) {
			final Random random = new Random();
			numericRes = numericRes + alphanumeric.charAt(random.nextInt(alphanumeric.length() - 1));
		}

		return dateRes + "-" + numericRes;
	}

	public Collection<XXXXX> getXXXXXs(final int id) {
		final Collection<XXXXX> result = new ArrayList<XXXXX>();
		result.addAll(this.repository.getXXXXXs(id));
		return result;
	}

	public Collection<XXXXX> getXXXXXsC(final int id) {
		final Collection<XXXXX> result = new ArrayList<XXXXX>();
		result.addAll(this.repository.getXXXXXsC(id));
		return result;
	}

}
