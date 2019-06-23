
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Message;
import domain.Position;
import domain.Problem;
import domain.Rookie;
import domain.Sponsorship;
import domain.XXXXX;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository	applicationRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private Validator				validator;

	@Autowired
	private XXXXXService			xxxxxService;


	public Application reconstruct(final Application application, final BindingResult binding) {
		Application result;

		result = this.applicationRepository.findOne(application.getId());
		result.setExplanation(application.getExplanation());
		result.setLink(application.getLink());
		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Application reconstructReject(final Application application, final BindingResult binding) {
		Application result;
		result = this.applicationRepository.findOne(application.getId());
		application.setStatus(result.getStatus());
		application.setSubmitMoment(result.getSubmitMoment());
		application.setVersion(result.getVersion());
		application.setCurricula(result.getCurricula());
		application.setExplanation(result.getExplanation());
		application.setLink(result.getLink());
		application.setProblem(result.getProblem());
		application.setRookie(result.getRookie());
		application.setMoment(result.getMoment());
		application.setRejectComment(application.getRejectComment());
		this.validator.validate(application, binding);
		result = application;
		return result;
	}

	public Application create() {
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ROOKIE"));
		Application application;

		application = new Application();

		return application;
	}
	public Application findOne(final int applicationId) {
		Assert.notNull(applicationId);

		final Application application = this.applicationRepository.findOne(applicationId);
		Assert.notNull(application);

		return application;
	}

	public Collection<Application> findAll() {
		Collection<Application> applications;

		applications = this.applicationRepository.findAll();
		Assert.notNull(applications);

		return applications;
	}
	public Application saveCompany(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));

		Application result;

		result = this.applicationRepository.save(application);

		return result;
	}

	public void acceptApplication(final Application application) {
		Assert.notNull(application);
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
		final Collection<Application> applications = this.getApplicationsByCompany(company);

		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
		Assert.isTrue(application.getStatus().equals("SUBMITTED"));
		Assert.isTrue(applications.contains(application));
		final Position position = this.getPositionByApplication(application.getId());
		Assert.isTrue(!position.getIsCancelled());
		application.setStatus("ACCEPTED");

		final Message msg = this.messageService.create();
		msg.setRecipient(application.getRookie());
		msg.setSubject("An application has changed its status.");
		msg.setBody("The application for the position has changed its status to ACCEPTED");
		msg.getTags().add("NOTIFICATION");
		this.messageService.save(msg);

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllByPosition(position.getId());
		for (final Sponsorship s : sponsorships)
			this.sponsorshipService.deleteForced(s);

	}

	public void rejectApplication(final Application application) {
		Assert.notNull(application);
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
		final Collection<Application> applications = this.getApplicationsByCompany(company);

		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
		Assert.isTrue(application.getStatus().equals("SUBMITTED"));
		Assert.isTrue(applications.contains(application));
		Assert.isTrue(!application.getRejectComment().equals(""));
		final Position position = this.getPositionByApplication(application.getId());
		Assert.isTrue(!position.getIsCancelled());
		application.setStatus("REJECTED");

		final Message msg = this.messageService.create();
		msg.setRecipient(application.getRookie());
		msg.setSubject("An application has changed its status.");
		msg.setBody("The application for the position has changed its status to REJECTED");
		msg.getTags().add("NOTIFICATION");
		this.messageService.save(msg);
	}

	public Collection<Application> getApplicationsByRookie(final Rookie rookie) {
		Assert.notNull(rookie);
		Collection<Application> applications;

		applications = this.applicationRepository.getApplicationsByRookie(rookie.getId());

		return applications;
	}

	public Collection<Application> getApplicationsByCompany(final Company company) {
		Assert.notNull(company);
		Collection<Application> applications;

		applications = this.applicationRepository.getApplicationsByCompany(company.getId());

		return applications;
	}

	public Collection<Application> getApplicationsByPosition(final int positionId) {
		Assert.notNull(positionId);
		Collection<Application> applications;

		applications = this.applicationRepository.getApplicationsByPosition(positionId);

		return applications;
	}

	public Collection<Application> getAllApplicationsByPosition(final int positionId) {

		Collection<Application> applications;

		applications = this.applicationRepository.getAllApplicationsByPosition(positionId);

		return applications;
	}

	public Position getPositionByApplication(final int applicationId) {
		Assert.notNull(applicationId);

		final Position position = this.applicationRepository.getPositionByApplication(applicationId);

		return position;
	}

	public void delete(final Application application) {
		Assert.notNull(application);
		final Collection<XXXXX> c = this.xxxxxService.getXXXXXs(application.getId());
		for (final XXXXX x : c)
			this.xxxxxService.delete2(x);
		this.applicationRepository.delete(application);
	}

	//PARTE DEL ROOKIE--------------------------------------------------------------------------------------------------

	public Application saveRookie(final Application application, final int positionId) {
		Application result;
		Assert.notNull(application);
		Assert.isTrue(application.getId() == 0);
		Assert.notNull(positionId);
		final Position p = this.positionService.findOne(positionId);
		Assert.notNull(p);
		Assert.isTrue(p.getIsFinal() == true && p.getIsCancelled() == false);

		final Actor a = this.actorService.getActorLogged();
		final Rookie h = this.rookieService.findOne(a.getId());
		Assert.notNull(h);

		application.setRookie(h);
		application.setMoment(new Date());
		application.setStatus("PENDING");
		application.setCurricula(this.curriculaService.copy(application.getCurricula()));
		final List<Problem> problems = (List<Problem>) p.getProblems();
		final Random random = new Random();
		final int valorRandom = random.nextInt(problems.size());
		application.setProblem(problems.get(valorRandom));

		result = this.applicationRepository.save(application);

		p.getApplications().add(result);

		return result;

	}

	public Application saveRookieUpdate(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ROOKIE"));
		Assert.isTrue(application.getStatus().equals("PENDING"));
		Assert.isTrue(!application.getExplanation().equals(""));
		Assert.isTrue(!application.getLink().equals(""));

		Application result = application;
		result.setSubmitMoment(new Date());
		result.setStatus("SUBMITTED");

		result = this.applicationRepository.save(application);

		return result;
	}
}
