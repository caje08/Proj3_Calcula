package pt.uc.dei.aor.paj;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Chatclass implements Serializable {

	private static final long serialVersionUID = 3149507795197543389L;
	private String userID;
	private String usermsg;
	private int compmsg = 200;
	private Date date;
	private Date hora;
	private String dataehora;

	public Chatclass() {
		this.userID = "";
		this.usermsg = "";
		this.compmsg = 0;
		this.date = new Date();
		this.hora = new Date();
		this.dataehora="";
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUsermsg() {
		return usermsg;
	}

	public void setUsermsg(String usermsg) {
		this.usermsg = usermsg;
	}

	public int getCompmsg() {
		return compmsg;
	}

	public void setCompmsg(int compmsg) {
		this.compmsg = compmsg;
	}

	public String getDataehora() {
		return dataehora;
	}

	public void setDataehora(String dataehora) {
		this.dataehora = dataehora;
	}

	public String getDateWithHours() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String datagora = simpleDateFormat.format(new Date());
		try {
			date = simpleDateFormat.parse(datagora);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataehora=datagora;
		return datagora;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

}
