package pt.uc.dei.aor.paj;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class MessageBean implements Serializable {

	private static final long serialVersionUID = -7545699186003072974L;
	private UIComponent component;

	public UIComponent getComponent() {
		return component;
	}

	public void setComponent(UIComponent component) {
		this.component = component;
	}

	public String doAction() {

		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(component.getClientId(),
				new FacesMessage("Test msg"));

		return "";
	}
}
