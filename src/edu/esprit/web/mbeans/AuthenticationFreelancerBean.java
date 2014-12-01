package edu.esprit.web.mbeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import com.fdz.domain.Freelancer;
import com.fdz.service.local.AuthenticationFreelancerServiceLocal;

@ManagedBean(name = "authBean")
@SessionScoped
public class AuthenticationFreelancerBean implements Serializable {

	private static final long serialVersionUID = -6916676537171647179L;

	@EJB
	private AuthenticationFreelancerServiceLocal authenticationFreelancerServiceLocal;


	// model
	private Freelancer freelancer;
	
	private boolean loggedIn;

	//

	public AuthenticationFreelancerBean() {
	}

	// model initialization
	@PostConstruct
	public void initModel() {
		freelancer = new Freelancer();
		loggedIn = false;
	}

	public String doLogin() {
		String navigateTo = null;
		// login application logic
		Freelancer freelancerFound = authenticationFreelancerServiceLocal
				.authenticate(freelancer.getLogin(), freelancer.getPassword());

		if (freelancerFound != null) {
			freelancer = freelancerFound;
			loggedIn = true;
			navigateTo = "/pages/freelancer/home?faces-redirect=true";
			// if (freelancer instanceof Admin) {
			// navigateTo = "/pages/admin/home?faces-redirect=true";
			// }
			// if (freelancer instanceof Customer) {
			// navigateTo = "/pages/customer/home?faces-redirect=true";
			// }
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

	// public boolean hasRole(String role) {
	// boolean authorized = false;
	// if (role.equals("Admin")) {
	// authorized = (freelancer instanceof Admin);
	// }
	// if (role.equals("Customer")) {
	// authorized = (freelancer instanceof Customer);
	// }
	//
	// return authorized;
	// }

	public Freelancer getUser() {
		return freelancer;
	}

	public void setUser(Freelancer freelancer) {
		this.freelancer = freelancer;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public void doSave() {
		authenticationFreelancerServiceLocal.saveOrUpdateFreelancer(freelancer);
		FacesContext.getCurrentInstance().addMessage(
				"setting_form:setting_submit",
				new FacesMessage("informations changed!"));
	}
	
	public void doCancel() {
		freelancer = new Freelancer();
	}
	
	public void doEnable() {
		authenticationFreelancerServiceLocal.disableAccountFreelancer(freelancer.getIdFreelancer());
		FacesContext.getCurrentInstance().addMessage(
				"setting_form:disable_submit",
				new FacesMessage("your account has been enabled!"));
	}
	
	public void handleFileUpload(FileUploadEvent event){
		System.out.println("elligi");
	}

}
