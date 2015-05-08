package pt.uc.dei.aor.paj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class Chatarray implements Serializable {

	private static final long serialVersionUID = -7920602073308668803L;
	private int IDarray;
	private String key;
	private Chatclass value;
	private ArrayList<Chatclass> msgs;
	private CopyOnWriteArrayList<Chatclass> store;
	// ConcurrentHashMap
	private ConcurrentHashMap<String, Chatclass> concurrentmsg;

	public Chatarray() {
		this.IDarray = 0;
		this.key = "";
		this.value = new Chatclass();
		msgs = new ArrayList<Chatclass>();
		store = new CopyOnWriteArrayList<Chatclass>();
		this.concurrentmsg = new ConcurrentHashMap<String, Chatclass>();
	}

	public void addConcurrentMsg(Chatclass msg) {
		key = Integer.toString(IDarray + 1);
		concurrentmsg.put(key, msg);
		IDarray += 1;
		msgs.add(msg);
		store.add(0,msg);
	}

	public CopyOnWriteArrayList<Chatclass> getStore() {
		return store;
	}

	public void setStore(CopyOnWriteArrayList<Chatclass> store) {
		this.store = store;
	}

	public ArrayList<Chatclass> getMsgs() {
		return msgs;
	}

	public void setMsgs(ArrayList<Chatclass> msgs) {
		this.msgs = msgs;
	}

	public Chatclass getValorByKey(String k) {

		Chatclass valor = concurrentmsg.get(k);
		return valor;
	}

	public int getIDarray() {
		return IDarray;
	}

	public void setIDarray(int iDarray) {
		IDarray = iDarray;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Chatclass getValue() {
		return value;
	}

	public void setValue(Chatclass value) {
		this.value = value;
	}

	public ConcurrentHashMap<String, Chatclass> getConcurrentmsg() {
		return concurrentmsg;
	}

	public void setConcurrentmsg(
			ConcurrentHashMap<String, Chatclass> concurrentmsg) {
		this.concurrentmsg = concurrentmsg;
	}

}
