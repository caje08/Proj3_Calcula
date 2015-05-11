package pt.uc.dei.aor.paj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 97653816888614094L;
	private String username;
	private String password;
	private String repassword;
	private boolean isUsernameValid;
	private boolean isPasswordValid;
	private boolean validationComplete;
	private boolean validRegComplete;
	private boolean seguinte;
	private boolean regValid;
	private HashMap<String, String> user;
//	@Inject
//	private Calculadora activusr;

	public Login() {
		user = new HashMap<>();
		user.put("USER1", "pass1");
		user.put("USER2", "pass2");
		validationComplete = false;
		validRegComplete = false;
		this.isPasswordValid = false;
		this.isUsernameValid = false;
		this.seguinte = false;
		this.password = "";
		this.username = "";
		this.regValid = false;
	}

	public String userLogout(){
		this.username = "";
		this.password = "";
		this.repassword = "";
		this.isPasswordValid = false;
		this.isUsernameValid = false;
		this.regValid = false;
		return "login";
	}
	public boolean getRegValid() {
		return regValid;
	}

	public void setRegValid(boolean isRegValid) {
		this.regValid = isRegValid;
	}

	public HashMap<String, String> getUser() {
		return user;
	}

	public void setUser(HashMap<String, String> user) {
		this.user = user;
	}

	/**
	 * Delete a user from the HashMap
	 * 
	 * @param nome
	 *            username
	 * 
	 */
	public void eliminaUser(String nome) {
		String chave = criaChave(nome);
		String val = user.get(chave);

		if (val != null) {
			user.remove(chave);
		} else {
			isUsernameValid = false;
		}
	}

	/**
	 * Search for a specific user
	 * 
	 * @param nome
	 *            username
	 * 
	 */
	public void procuraUser(String nome, String pw) {
		String chave = criaChave(nome);
		String valor = user.get(chave);

		if (user.containsKey(chave)) {

			isUsernameValid = true;
			if (valor != null && valor.equals(pw)) {
				isPasswordValid = true;
			} else {
				isPasswordValid = false;
			}
		} else {
			isUsernameValid = false;
		}
		if (isUsernameValid && isPasswordValid) {
			seguinte = true;
		} else {
			seguinte = false;
		}
	}

	/**
	 * Check if the key is unique and add the user to the HashMap. Activates the
	 * 'isRegValid' flag to true if the user can be added
	 * 
	 * @param usr
	 *            username
	 * @param pw
	 *            the username password
	 */
	public void addUser() {
		String chave, usr = username, pw = password;
		validationComplete = false;
		if (this.username == null || this.username.equals("")) {
			isUsernameValid = false;
			validRegComplete = true;
		} else {

			isUsernameValid = true;
			validRegComplete = true;
		}
		if (this.password == null || this.password.equals("")) {
			isPasswordValid = false;
			validRegComplete = true;
		} else {
			isPasswordValid = true;
			validRegComplete = true;
		}
		if (isUsernameValid && isPasswordValid) {
			testRegist(usr, pw);
			if (isUsernameValid && isPasswordValid) {
				isUsernameValid = false;
				isPasswordValid = false;
				chave = criaChave(usr);
				if (user.get(chave) == null) {
					isUsernameValid = true;
					if (pw != null) {
						user.put(chave, pw);
//						activusr.setLogusrname(usr);
						isPasswordValid = true;
						regValid = true;
					} else {
						isPasswordValid = false;
					}
				} else {
					isUsernameValid = false;
				}
			}
		}
	}

	/**
	 * Check if both username and password related to the regist of a user are
	 * having less than 10 digits or chars activating the flags
	 * 'isUsernameValid' and 'isPasswordValid' accordingly.
	 * 
	 * @param usr
	 *            username
	 * @param pw
	 *            the username password
	 */
	public void testRegist(String st, String pw) {
		int ns = st.length();
		int np = pw.length();

		if (ns > 0 && ns < 11) {

			for (int i = 0; i < ns; i++) {
				if ((st.charAt(i) >= 'a' && st.charAt(i) <= 'z')
						|| (st.charAt(i) >= 'A' && st.charAt(i) <= 'Z')
						|| (st.charAt(i) >= '0' && st.charAt(i) <= '9')) {
					isUsernameValid = true;
				} else {
					i = ns;
					isUsernameValid = false;
				}
			}
			if (np > 0 && np < 10 && isUsernameValid) {

				for (int i = 0; i < np; i++) {
					char c = pw.charAt(i);
					if ((pw.charAt(i) >= 'a' && pw.charAt(i) <= 'z')
							|| (pw.charAt(i) >= 'A' && pw.charAt(i) <= 'Z')
							|| (pw.charAt(i) >= '0' && pw.charAt(i) <= '9')) {
						isPasswordValid = true;
					} else {
						i = np;
						isPasswordValid = false;
					}
				}
			} else {
				isPasswordValid = false;
			}
		} else {
			isUsernameValid = false;
		}
	}

	/**
	 * @param st
	 *            username
	 * @return the username in Uppercase to be used as key
	 */
	public String criaChave(String st) {
		return st.toUpperCase();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	/**
	 * @return the isUsernameValid
	 */
	public boolean getIsUsernameValid() {
		return isUsernameValid;
	}

	/**
	 * @param isUsernameValid
	 *            the isUsernameValid to set
	 */
	public void setUsernameValid(boolean isUsernameValid) {
		this.isUsernameValid = isUsernameValid;
	}

	/**
	 * @return the isPasswordValid
	 */
	public boolean getIsPasswordValid() {
		return isPasswordValid;
	}

	/**
	 * @paramisPasswordValid the isPasswordValid to set
	 */
	public void setPasswordValid(boolean isPasswordValid) {
		this.isPasswordValid = isPasswordValid;
	}

	/**
	 * @return the validationComplete
	 */
	public boolean getValidationComplete() {
		return validationComplete;
	}

	public boolean getValidRegComplete() {
		return validRegComplete;
	}

	public void setValidRegComplete(boolean validRegComplete) {
		this.validRegComplete = validRegComplete;
	}

	/**
	 * @param validationComplete
	 *            the validationComplete to set
	 */
	public void setValidationComplete(boolean validationComplete) {
		this.validationComplete = validationComplete;
	}

	public String checkValidity() {
		String saida = "";
		regValid = false;
		if (this.username == null || this.username.equals("")) {
			isUsernameValid = false;
			FacesContext.getCurrentInstance().addMessage("loginusermsg", new FacesMessage("Error: Your password is NOT strong enough."));
			validationComplete = true;
		} else {
			// procuraUser(username,password);
			isUsernameValid = true;
			validationComplete = true;
		}
		if (this.password == null || this.password.equals("")) {
			isPasswordValid = false;
			validationComplete = true;
		} else {
			isPasswordValid = true;
			validationComplete = true;
		}
		if (isUsernameValid && isPasswordValid) {
			procuraUser(username, password);
			if (validationComplete && seguinte) {
				saida = "index";
			} else
				saida = "login";
		} else {
			saida = "login";
		}
		return saida;
	}
}
