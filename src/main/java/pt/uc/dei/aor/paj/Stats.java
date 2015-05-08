package pt.uc.dei.aor.paj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Carlos Santos e Filipa Pedrosa
 * Classe que armazena as estatísticas dos operadores usados durante a utilizacao da aplicacao Calculadora.
 * Esta classe garante que os valores das estatisticas estao disponíveis enquanto a aplicação estiver a funcionar e
 * por diversas sessões.
 */
@Named
@ApplicationScoped
public class Stats implements Serializable {

	private static final long serialVersionUID = -2631420503453550762L;
	private ArrayList<Operacao> estatisticas;
	private int num;
	private String comm;
	private HashMap<String, String> map;
	private ArrayList<String> keylist;

	public Stats() {
		map = new HashMap<String, String>();
		keylist = new ArrayList<String>(map.keySet());
		estatisticas = new ArrayList<Operacao>();
	}


	public void addMap(String k, String v) {
		this.map.put(k, v);
	}
	/**
	 * Método que na classe Stats faz a gestao das estatíticas da aplicacao sempre que os utilizadores a utilizam
	 * i.e, submetem operacoes atraves da tecla 'igual'(=). Verifica se o comando existe já no array e incrementa a sua quantidade 
	 * ou adiciona ao array caso seja a primeira vez que o operador é utilizado na aplicação. 
	 * @param cmd Array de Strings com os operadores usados pelo utilizador e que devem actualizar o array 'estatisticas' 
	 *  
	 */
	public void newaddStats(ArrayList<String> cmd) {
		int ncmd = cmd.size();
		int dim = estatisticas.size();
		int ct = 0;// nº items já registados para o comando "comd'
		Operacao strop;
		String comd = "";

		for (int j = 0; j < ncmd; j++) {
			comd = cmd.get(j);
			if (dim == 0) {
				strop = new Operacao(1, comd, "",0l);
				this.estatisticas.add(strop);
				dim = estatisticas.size();
			} else {

				for (int i = 0; i < dim; i++) {
					if (estatisticas.get(i).getComando().equals(comd)) {
						ct = estatisticas.get(i).getId() + 1;
						estatisticas.get(i).setId(ct);
						i = dim;
					} else {
						if (i + 1 == dim) {
							strop = new Operacao(1, comd, "",0l);
							this.estatisticas.add(strop);

						}
					}
				}
				dim = estatisticas.size();
			}
		}
	}

	public ArrayList<Operacao> getEstatisticas() {
		return estatisticas;
	}

	public void setEstatisticas(ArrayList<Operacao> estatisticas) {
		this.estatisticas = estatisticas;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getComm() {
		return comm;
	}

	public void setComm(String comm) {
		this.comm = comm;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public ArrayList<String> getKeylist() {
		return keylist;
	}

	public void setKeylist(ArrayList<String> keylist) {
		this.keylist = keylist;
	}

}
