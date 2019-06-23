
package controllers.application;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ApplicationService;
import services.CompanyService;
import services.CurriculaService;
import services.PositionService;
import services.RookieService;
import services.XXXXXService;
import controllers.AbstractController;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Rookie;
import domain.XXXXX;

@Controller
@RequestMapping("application")
public class ApplicationController extends AbstractController {

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private RookieService		rookieService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private XXXXXService		xxxxxService;


	@RequestMapping(value = "/rookie/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Application> applications;

		final Actor user = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
		final Rookie rookie = this.rookieService.findOne(user.getId());
		applications = this.applicationService.getApplicationsByRookie(rookie);

		final HashMap<Integer, Position> appPosition = new HashMap<Integer, Position>();
		for (final Application a : applications)
			appPosition.put(a.getId(), this.applicationService.getPositionByApplication(a.getId()));

		result = new ModelAndView("application/rookie/list");
		result.addObject("applications", applications);
		result.addObject("appPosition", appPosition);
		result.addObject("requestURI", "application/rookie/list.do");
		return result;
	}

	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listCompany(@RequestParam final int positionId) {
		ModelAndView result;
		Collection<Application> applications;

		final Actor user = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
		final Company company = this.companyService.findOne(user.getId());

		try {
			final Position position = this.positionService.findOne(positionId);
			Assert.isTrue(position.getCompany().equals(company));
			applications = this.applicationService.getApplicationsByPosition(positionId);
			result = new ModelAndView("application/company/list");
			result.addObject("applications", applications);
			result.addObject("requestURI", "application/company/list.do?positionId=" + positionId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/position/company/list.do");
		}
		return result;
	}

	@RequestMapping(value = "/rookie/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;
		Actor actor;
		Rookie rookie;

		try {
			Assert.notNull(applicationId);
			actor = this.actorService.getActorLogged();
			rookie = this.rookieService.findOne(actor.getId());
			application = this.applicationService.findOne(applicationId);
			Assert.isTrue(application.getRookie().equals(rookie));
			final Position position = this.applicationService.getPositionByApplication(applicationId);
			result = new ModelAndView("application/rookie/show");
			result.addObject("application", application);
			result.addObject("position", position);
			final Collection<XXXXX> x = this.xxxxxService.getXXXXXsC(application.getId());
			final Date haceUnMes = this.restarMesesFecha(new Date(), 1);
			final Date haceDosMeses = this.restarMesesFecha(new Date(), 2);
			result.addObject("xxxxx", x);
			result.addObject("haceUnMes", haceUnMes);
			result.addObject("haceDosMeses", haceDosMeses);
			final String language = LocaleContextHolder.getLocale().getLanguage();
			result.addObject("lang", language);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/application/rookie/list.do");
		}
		return result;
	}

	@RequestMapping(value = "/company/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		try {
			Assert.notNull(applicationId);
			application = this.applicationService.findOne(applicationId);
			this.applicationService.acceptApplication(application);
			this.applicationService.saveCompany(application);
			final int positionId = this.applicationService.getPositionByApplication(applicationId).getId();
			result = new ModelAndView("redirect:/application/company/list.do?positionId=" + positionId);
			return result;
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			return result;
		}
	}

	@RequestMapping(value = "/company/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		try {
			Assert.notNull(applicationId);
			final Position position = this.applicationService.getPositionByApplication(applicationId);
			Assert.isTrue(!position.getIsCancelled());
			application = this.applicationService.findOne(applicationId);
			result = this.rejectModelAndView(application);
			return result;
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
			return result;
		}
	}

	@RequestMapping(value = "/company/reject", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(Application application, final BindingResult binding) {
		ModelAndView result;
		application = this.applicationService.reconstructReject(application, binding);

		if (application.getRejectComment().equals("")) {
			binding.rejectValue("rejectComment", "error.rejectComment");
			result = this.rejectModelAndView(application);
			return result;
		} else if (binding.hasErrors()) {
			result = this.rejectModelAndView(application);
			return result;
		} else {
			final int positionId = this.applicationService.getPositionByApplication(application.getId()).getId();
			this.applicationService.rejectApplication(application);
			this.applicationService.saveCompany(application);
			result = new ModelAndView("redirect:/application/company/list.do?positionId=" + positionId);
			return result;
		}
	}

	protected ModelAndView rejectModelAndView(final Application application) {
		ModelAndView result;

		result = this.rejectModelAndView(application, null);

		return result;
	}

	protected ModelAndView rejectModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("application/company/reject");
		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}

	//PARTE DEL ROOKIE--------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/rookie/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		ModelAndView result;
		try {
			final Position position = this.positionService.findOne(positionId);
			final Rookie rookie = this.rookieService.findOne(this.actorService.getActorLogged().getId());
			final Collection<Application> applications = this.applicationService.getApplicationsByRookie(rookie);
			final Collection<Application> applicationsPosition = this.applicationService.getAllApplicationsByPosition(positionId);
			Assert.isTrue(!applicationsPosition.contains(applications));
			Assert.isTrue(!position.getIsCancelled());
			final Actor a = this.actorService.getActorLogged();
			final Rookie h = this.rookieService.findOne(a.getId());
			final Application application = this.applicationService.create();
			result = new ModelAndView("application/rookie/create");
			result.addObject("application", application);
			result.addObject("positionId", positionId);
			result.addObject("curricula", h.getCurricula());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/rookie/create", method = RequestMethod.POST)
	public ModelAndView saveCreate(@ModelAttribute("application") final Application application, @RequestParam final int positionId, final BindingResult binding) {
		ModelAndView result;
		try {
			this.applicationService.saveRookie(application, positionId);
			result = new ModelAndView("redirect:/application/rookie/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/rookie/update", method = RequestMethod.GET)
	public ModelAndView update(@RequestParam final int applicationId) {
		ModelAndView result;
		try {
			final Position position = this.applicationService.getPositionByApplication(applicationId);
			Assert.isTrue(!position.getIsCancelled());
			final Application a = this.applicationService.findOne(applicationId);
			final Actor ac = this.actorService.getActorLogged();
			final Rookie h = this.rookieService.findOne(ac.getId());
			Assert.isTrue(a.getRookie().equals(h));

			result = new ModelAndView("application/rookie/update");
			result.addObject("application", a);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/rookie/update", method = RequestMethod.POST)
	public ModelAndView saveUpdate(@ModelAttribute("application") Application application, final BindingResult binding) {
		ModelAndView result;
		if (StringUtils.isEmpty(application.getExplanation()) || StringUtils.isEmpty(application.getLink())) {
			binding.rejectValue("explanation", "error.explanation");
			binding.rejectValue("link", "error.link");
			result = new ModelAndView("application/rookie/update");
			result.addObject("application", application);
		} else
			try {
				application = this.applicationService.reconstruct(application, binding);
				this.applicationService.saveRookieUpdate(application);
				result = new ModelAndView("redirect:/application/rookie/list.do");
			} catch (final ValidationException v) {
				result = new ModelAndView("application/rookie/update");
				result.addObject(application);
			} catch (final Throwable oops) {
				result = new ModelAndView("application/rookie/update");
				result.addObject("application", application);
				result.addObject("message", "application.commit.error");
			}
		return result;
	}

	private Date restarMesesFecha(final Date date, final Integer meses) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -meses);
		return calendar.getTime();
	}
}
