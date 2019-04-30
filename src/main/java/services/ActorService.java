
package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ActorService {

	//Managed Repositories
	@Autowired
	private ActorRepository			actorRepository;

	//Supporting services
	@Autowired
	private UserAccountService		userAccountService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private AdministratorService 	administratorService;
	@Autowired
	private RookieService 			rookieService;
	@Autowired
	private CurriculaService		curriculaService;
	@Autowired
	private ApplicationService		applicationService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private CompanyService			companyService;
	@Autowired
	private ProblemService			problemService;
	@Autowired
	private PositionService			positionService;
	@Autowired
	private ProviderService			providerService;
	@Autowired
	private AuditorService			auditorService;
	@Autowired
	private ItemService			itemService;
	@Autowired
	private SponsorshipService		sponsorshipService;
	@Autowired
	private AuditService		auditService;


	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);
	}

	public UserAccount findUserAccount(final Actor actor) {
		Assert.notNull(actor);

		UserAccount result;

		result = this.userAccountService.findByActor(actor);

		return result;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Actor result;

		result = this.actorRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public Actor getActorLogged() {
		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		actor = this.findByUserAccount(userAccount);
		Assert.notNull(actor);

		return actor;
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		result = this.actorRepository.save(actor);

		return result;
	}

	public Actor findByUsername(final String username) {
		Assert.notNull(username);

		Actor result;
		result = this.actorRepository.findByUsername(username);
		return result;
	}

	public Collection<Actor> findSuspiciousActors() {
		Collection<Actor> result;
		result = this.actorRepository.findSuspiciousActors();
		return result;
	}

	public Collection<Actor> findBannedActors() {
		Collection<Actor> result;
		result = this.actorRepository.findBannedActors();
		return result;
	}

	public Boolean existUsername(final String username) {
		Boolean res = false;
		final List<String> lista = new ArrayList<String>();
		for (final Actor a : this.actorRepository.findAll())
			lista.add(a.getUserAccount().getUsername());
		if (!(lista.contains(username)))
			res = true;
		return res;
	}

	public Boolean existIdSocialProfile(final Integer id) {
		Boolean res = false;
		final List<Integer> lista = new ArrayList<Integer>();
		for (final SocialProfile s : this.socialProfileService.findAll())
			lista.add(s.getId());
		if (lista.contains(id))
			res = true;
		return res;
	}

	public void deleteInformation() {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor user = this.findByUserAccount(userAccount);

		//Borrado de los socialProfiles de los actores
		if (!(user.getSocialProfiles().isEmpty())) {
			final List<SocialProfile> a = new ArrayList<>();
			final Collection<SocialProfile> ad = user.getSocialProfiles();
			a.addAll(ad);
			for (final SocialProfile i : a)
				this.socialProfileService.delete(i);
		}

		//Borrado de los mensajes que recibes y envias
		final Collection<Message> msgs = this.messageService.findAllSentByActor(user.getId());
		msgs.addAll(this.messageService.findAllReceivedByActor(user.getId()));

		if (msgs.size() > 0)
			for (final Message m : msgs)
				this.messageService.deleteForced(m);

		//Borrado si admin
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {

			final Administrator admin = this.administratorService.findOne(user.getId());

			//Borrado de la informacion del administrador
			this.administratorService.delete(admin);
		}

		//Borrado si rookie
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ROOKIE")) {

			//Faltan las relaciones de rookie
			final Rookie h = this.rookieService.findOne(user.getId());

			//Borrado de las applications del rookie
			final Collection<Application> applications = this.applicationService.getApplicationsByRookie(h);
			final Collection<Curricula> allCurriculas = this.curriculaService.findAll();
			final Collection<Position> allPositions = this.positionService.findAll();

			this.deleteApplications(applications, allPositions, allCurriculas);

			//Borrado de todos las curriculas de rookie
			Collection<Curricula> curriculas = h.getCurricula();
			h.setCurricula(new ArrayList<Curricula>());
			for(final Curricula c : curriculas) {
				this.curriculaService.deleteCopy(c);
			}

			//Borrado del rookie
			this.rookieService.delete(h);

			//Borrado del finder
			Finder finder = this.finderService.findOne(h.getFinder().getId());
			finder.setPositions(new ArrayList<Position>());
			this.finderService.delete(finder);

		}

		//Borrado si company
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY")) {

			final Company company = this.companyService.findOne(user.getId());

			//Borrado de los Applications de company
			final Collection<Application> applications = this.applicationService.getApplicationsByCompany(company);
			final Collection<Curricula> allCurriculas = this.curriculaService.findAll();
			final Collection<Position> allPositions = this.positionService.findAll();
			this.deleteApplications(applications, allPositions, allCurriculas);

			//Borrado de los problems de company
			final Collection<Problem> problems = this.problemService.findAllByCompany(company.getId());
			final Collection<Application> allApplications = this.applicationService.findAll();
			for (Problem p : problems) {
				for (Application a : allApplications) {
					if (a.getProblem().equals(p)) this.applicationService.delete(a);
				}
				this.problemService.deleteForced(p);
			}


			//Borrado de las positions
			final Collection<Position> positions = this.positionService.getPositionsByCompanyAll(company);
			final Collection<Finder> allFinders = this.finderService.findAll();
			for (Position p : positions) {
				for (Finder f : allFinders) {
					if (f.getPositions().contains(p)) {
						Collection<Position> pos = f.getPositions();
						pos.remove(p);
						f.setPositions(pos);
					}

				}
				this.positionService.deleteForced(p);
			}

			this.companyService.delete(company);
		}

		//Borrado del provider
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("PROVIDER")) {
			final Provider p = this.providerService.findOne(user.getId());

			//Borrado items de un provider
			Collection<Item> items = new ArrayList<>();
			items = this.itemService.findAllByProvider(p.getId());
			if(!(items.isEmpty())) {
				for (Item i : items) {
					this.itemService.delete(i);
				}
			}

			//Borrado sponsorship de un provider
			Collection<Sponsorship> sponsorships=new ArrayList<>();
			sponsorships=this.sponsorshipService.findAllByProvider(p.getId());
			if(!(sponsorships.isEmpty())) {
				for (Sponsorship s : sponsorships) {
					this.sponsorshipService.delete(s);
				}
			}
			this.providerService.delete(p);
		}

		//Borrado si auditor
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("AUDITOR")) {

			final Auditor a = this.auditorService.findOne(user.getId());

			//Borrado audit de auditor
//			Collection<Audit> audits = new ArrayList<>();
//			audits = this.auditService.getAuditsByAuditor();
//			if(!(audits.isEmpty())) {
//				for (Audit au : audits) {
//					Collection<Position> positions = new ArrayList<>();
//					positions = this.positionService.getPositionByAudit(au.getId());
//					for (Position p : positions) {
//						p.setAuditor(null);
//					}
//					this.auditService.delete(au);
//				}
//			}
			this.auditorService.delete(a);
		}
	}

	private void deleteApplications(Collection<Application> applications, Collection<Position> positions){
		for(Application a : applications) {
			for (Position p : positions) {
				if (p.getApplications().contains(a)) p.getApplications().remove(a);
			}
			this.applicationService.delete(a);
		}
	}

	private void deleteApplications(Collection<Application> applications, Collection<Position> positions,
		Collection<Curricula> curriculas){
		for(Application a : applications) {
			Curricula c = a.getCurricula();
			for (Position p : positions) {
				if (p.getApplications().contains(a)) p.getApplications().remove(a);
			}
			this.applicationService.delete(a);
			if (curriculas.contains(c)) this.curriculaService.deleteCopy(c);
		}
	}
}
