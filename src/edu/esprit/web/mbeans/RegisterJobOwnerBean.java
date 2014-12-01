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

import com.fdz.service.local.AuthenticationJobOwnerServiceLocal;
import com.fdz.service.local.JobOwnerServiceLocal;

import com.fdz.domain.JobOwner;

@ManagedBean(name="registerJOBean")
@RequestScoped
public class RegisterJobOwnerBean {

	@EJB
	private AuthenticationJobOwnerServiceLocal authService;

	@EJB
	private JobOwnerServiceLocal jobownerService;

	@ManagedProperty("#{authJOBean}")
	private AuthenticationJobOwnerBean authJOBean;

	private JobOwner jobowner;

	public RegisterJobOwnerBean() {
	}

	@PostConstruct
	public void init() {
		jobowner = new JobOwner();
	}

	public String doSignUp() {
		String navigateTo = null;
		jobownerService.saveOrUpdate(jobowner);
		authJOBean.setUser(jobowner);
		navigateTo = authJOBean.doLogin();
		return navigateTo;
	}

	public JobOwner getCustomer() {
		return jobowner;
	}

	public void setCustomer(JobOwner jobowner) {
		this.jobowner = jobowner;
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

	public AuthenticationJobOwnerBean getAuthJOBean() {
		return authJOBean;
	}

	public void setAuthJOBean(AuthenticationJobOwnerBean authJOBean) {
		this.authJOBean = authJOBean;
	}



}
