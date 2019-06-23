
package controllers.xxxxx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ApplicationService;
import services.RookieService;
import services.XXXXXService;
import controllers.AbstractController;
import domain.Application;
import domain.XXXXX;

@Controller
@RequestMapping("/xxxxx")
public class XXXXXController extends AbstractController {

	@Autowired
	private XXXXXService		service;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private RookieService		rookieService;


	@RequestMapping(value = "/rookie,company/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int applicationId) {
		ModelAndView result;
		Collection<XXXXX> x;
		final String language = LocaleContextHolder.getLocale().getLanguage();

		x = this.service.getXXXXXs(applicationId);

		final Date haceUnMes = this.restarMesesFecha(new Date(), 1);
		final Date haceDosMeses = this.restarMesesFecha(new Date(), 2);
		result = new ModelAndView("xxxxx/rookie,company/list");
		result.addObject("xxxxx", x);
		result.addObject("requestURI", "xxxxx/rookie,company/list.do");
		result.addObject("lang", language);
		result.addObject("applicationId", applicationId);
		result.addObject("haceUnMes", haceUnMes);
		result.addObject("haceDosMeses", haceDosMeses);

		return result;

	}
	@RequestMapping(value = "/rookie/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int applicationId) {
		ModelAndView result;
		final XXXXX x = this.service.create();
		result = this.createEditModelAndView(x);
		result.addObject("applicationId", applicationId);
		return result;
	}

	@RequestMapping(value = "/rookie/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int xxxxxId) {
		ModelAndView result;
		XXXXX x;

		x = this.service.findOne(xxxxxId);

		Assert.isTrue(x.getApplication().getRookie().equals(this.rookieService.findOne(this.actorService.getActorLogged().getId())));
		Assert.isTrue(x.getIsFinal() == false);
		result = this.createEditModelAndView(x);
		result.addObject("applicationId", x.getApplication().getId());
		return result;
	}

	@RequestMapping(value = "/rookie/edit", method = RequestMethod.POST, params = "draft")
	public ModelAndView saveDraft(@ModelAttribute("xxxxx") XXXXX x, final BindingResult binding, @RequestParam final int applicationId) {
		ModelAndView result;
		try {
			if (x.getId() == 0) {
				final Application a = this.applicationService.findOne(applicationId);
				Assert.notNull(a);
				x.setApplication(a);
			}
			x = this.service.reconstruct(x, binding);
			Assert.isTrue(x.getIsFinal() == false);
			x.setIsFinal(false);
			this.service.save(x);
			result = new ModelAndView("redirect:/xxxxx/rookie,company/list.do?applicationId=" + x.getApplication().getId());
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(x);
			result.addObject("applicationId", applicationId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(x, "xxxxx.commit.error");
			result.addObject("applicationId", applicationId);
		}
		return result;
	}

	@RequestMapping(value = "/rookie/edit", method = RequestMethod.POST, params = "final")
	public ModelAndView saveFinal(@ModelAttribute("xxxxx") XXXXX x, final BindingResult binding, @RequestParam final int applicationId) {
		ModelAndView result;
		try {
			if (x.getId() == 0) {
				final Application a = this.applicationService.findOne(applicationId);
				Assert.notNull(a);
				x.setApplication(a);
			}
			x = this.service.reconstruct(x, binding);
			Assert.isTrue(x.getIsFinal() == false);
			x.setIsFinal(true);
			this.service.save(x);
			result = new ModelAndView("redirect:/xxxxx/rookie,company/list.do?applicationId=" + x.getApplication().getId());
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(x);
			result.addObject("applicationId", applicationId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(x, "xxxxx.commit.error");
			result.addObject("applicationId", applicationId);
		}
		return result;
	}

	@RequestMapping(value = "/rookie/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("xxxxx") XXXXX x, final BindingResult binding) {
		ModelAndView result;
		try {
			x = this.service.reconstruct(x, binding);
			this.service.delete(x);
			result = new ModelAndView("redirect:/xxxxx/rookie,company/list.do?applicationId=" + x.getApplication().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(x, "xxxxx.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final XXXXX x) {
		ModelAndView result;
		result = this.createEditModelAndView(x, null);
		return result;

	}

	protected ModelAndView createEditModelAndView(final XXXXX x, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("xxxxx/rookie/edit");
		result.addObject("xxxxx", x);
		result.addObject("message", messageCode);
		return result;
	}

	@RequestMapping(value = "/rookie,company/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int xxxxxId) {
		ModelAndView result;
		final XXXXX x = this.service.findOne(xxxxxId);
		if (x == null)
			result = new ModelAndView("redirect:/");
		final String language = LocaleContextHolder.getLocale().getLanguage();
		final SimpleDateFormat formatterEs = new SimpleDateFormat("dd-MM-yy HH:mm");
		final SimpleDateFormat formatterEn = new SimpleDateFormat("yy-MM-dd HH:mm");
		String moment;
		if (language == "es")
			moment = formatterEs.format(x.getMoment());
		else
			moment = formatterEn.format(x.getMoment());

		result = new ModelAndView("xxxxx/rookie,company/show");
		result.addObject("body", x.getBody());
		result.addObject("picture", x.getPicture());
		result.addObject("moment", moment);
		return result;

	}

	private Date restarMesesFecha(final Date date, final Integer meses) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -meses);
		return calendar.getTime();
	}

}
