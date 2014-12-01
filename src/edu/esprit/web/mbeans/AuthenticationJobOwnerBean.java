package edu.esprit.web.mbeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.fdz.domain.JobOwner;
import com.fdz.service.local.AuthenticationJobOwnerServiceLocal;

@ManagedBean(name = "authJOBean")
@SessionScoped
public class AuthenticationJobOwnerBean implements Serializable {

	private static final long serialVersionUID = -6916676537171647179L;

	@EJB
	private AuthenticationJobOwnerServiceLocal authenticationJobOwnerServiceLocal;


	// model
	private JobOwner jobowner;
	
	private boolean loggedIn;

	//

	public AuthenticationJobOwnerBean() {
	}

	// model initialization
	@PostConstruct
	public void initModel() {
		jobowner = new JobOwner();
		loggedIn = false;
	}

	public String doLogin() {
		String navigateTo = null;
		// login application logic
		JobOwner jobownerFound = authenticationJobOwnerServiceLocal
				.authenticate(jobowner.getLogin(), jobowner.getPassword());

		if (jobownerFound != null) {
			jobowner = jobownerFound;
			loggedIn = true;
			navigateTo = "/pages/jobowner/home?faces-redirect=true";
		}
		else {
			FacesContext.getCurrentInstance().addMessage(
					"login_form:login_submit",
					new FacesMessage("Bad credentials!"));
		}
		return navigateTo;
	}

	public String doLogout() {
		String navigateTo = null;
		initModel();
		navigateTo = "/pages/home?faces-redirect=true";
		return navigateTo;
	}



	public JobOwner getUser() {
		return jobowner;
	}

	public void setUser(JobOwner jobowner) {
		this.jobowner = jobowner;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public void doSave() {
		authenticationJobOwnerServiceLocal.saveOrUpdateJobOwner(jobowner);
	}

	public void doCancel() {
		jobowner = new JobOwner();
	}

}
