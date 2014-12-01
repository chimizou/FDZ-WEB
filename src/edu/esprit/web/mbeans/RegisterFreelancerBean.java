package edu.esprit.web.mbeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.fdz.service.local.AuthenticationFreelancerServiceLocal;
import com.fdz.service.local.FreelancerServiceLocal;

import com.fdz.domain.Freelancer;

@ManagedBean(name="registerBean")
@RequestScoped
public class RegisterFreelancerBean {

	@EJB
	private AuthenticationFreelancerServiceLocal authService;

	@EJB
	private FreelancerServiceLocal freelancerService;

	@ManagedProperty("#{authBean}")
	private AuthenticationFreelancerBean authBean;

	private Freelancer freelancer;

	public RegisterFreelancerBean() {
	}

	@PostConstruct
	public void init() {
		freelancer = new Freelancer();
	}

	public String doSignUp() {
		String navigateTo = null;
		freelancerService.saveOrUpdate(freelancer);
		authBean.setUser(freelancer);
		navigateTo = authBean.doLogin();
		return navigateTo;
	}

	public Freelancer getCustomer() {
		return freelancer;
	}

	public void setCustomer(Freelancer freelancer) {
		this.freelancer = freelancer;
	}

	public void validateLoginUnicity(FacesContext context,
			UIComponent component, Object value) throws ValidatorException {
		String loginToValidate = (String) value;
		if (loginToValidate == null || loginToValidate.trim().isEmpty()) {
			return;
		}
		boolean loginInUse = authService.loginExists(loginToValidate);
		if (loginInUse) {
			throw new ValidatorException(new FacesMessage(
					"login already in use!"));
		}
	}

	public AuthenticationFreelancerBean getAuthBean() {
		return authBean;
	}

	public void setAuthBean(AuthenticationFreelancerBean authBean) {
		this.authBean = authBean;
	}

}
