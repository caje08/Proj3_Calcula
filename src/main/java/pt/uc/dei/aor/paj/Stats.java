package pt.uc.dei.aor.paj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Carlos Santos e Filipa Pedrosa Classe que armazena as estatísticas
 *         dos operadores usados durante a utilizacao da aplicacao Calculadora.
 *         Esta classe garante que os valores das estatisticas estao disponíveis
 *         enquanto a aplicação estiver a funcionar e por diversas sessões.
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
	 * Método que na classe Stats faz a gestao das estatíticas da aplicacao
	 * sempre que os utilizadores a utilizam i.e, submetem operacoes atraves da
	 * tecla 'igual'(=). Verifica se o comando existe já no array e incrementa a
	 * sua quantidade ou adiciona ao array caso seja a primeira vez que o
	 * operador é utilizado na aplicação.
	 * 
	 * @param cmd
	 *            Array de Strings com os operadores usados pelo utilizador e
	 *            que devem actualizar o array 'estatisticas'
	 * 
	 */
	public void newaddStats(ArrayList<String> cmd) {
		int ncmd = cmd.size();
		int dim = estatisticas.size();
		int ct = 0;// nº items já registados para o comando "comd'
		Operacao strop;
		Operacao tmpop = new Operacao(0, "", "", 0l);
		String comd = "";

		for (int j = 0; j < ncmd; j++) {
			comd = cmd.get(j);
			if (comd.length() > 3) {
				if (comd.charAt(0) == '+' && comd.charAt(1) == '/'
						&& comd.charAt(comd.length() - 1) == '-') {
					comd = "+/-";
				}
			}
			if (dim == 0) {
				strop = new Operacao(1, comd, "", 0l);
				this.estatisticas.add(strop);
				dim = estatisticas.size();
			} else {

				for (int i = 0; i < dim; i++) {
					if (estatisticas.get(i).getComando().equals(comd)) {
						ct = estatisticas.get(i).getId() + 1;
						estatisticas.get(i).setId(ct);
						if (dim > 1) {
							tmpop = estatisticas.get(i);
							estatisticas.remove(i);
							inserOrdenado(tmpop);
						}
						i = dim;
					} else {
						if (i + 1 == dim) {
							strop = new Operacao(1, comd, "", 0l);
							// this.estatisticas.add(strop);
							inserOrdenado(strop);
						}
					}
				}
				dim = estatisticas.size();
			}
		}
	}

	public void inserOrdenado(Operacao op) {
		int n = estatisticas.size();
		int qty = op.getId(), tmp = 0;
		int ind1 = 0, ind2 = 0;

		for (int i = 0; i < n; i++) {
			tmp = estatisticas.get(i).getId();
			if (qty > tmp) {
				this.estatisticas.add(i, op);
				i = n;
			} else {
				if (qty == tmp) {
					ind1 = Integer.parseInt(posicao(op.getComando(), 0));
					ind2 = Integer.parseInt(posicao(estatisticas.get(i)
							.getComando(), 0));
					if (ind1 < ind2) {
						this.estatisticas.add(i, op);
						i = n;
					} else {
						if (i + 1 == n) {
							this.estatisticas.add(op);
							i = n;
						}
						// else {
						// for (int j = i + 1; j < n; j++) {
						// tmp = estatisticas.get(j).getId();
						// ind1 = Integer.parseInt(posicao(
						// op.getComando(), 0));
						// ind2 = Integer.parseInt(posicao(estatisticas
						// .get(j).getComando(), 0));
						// if (qty == tmp && ind1 < ind2) {
						// this.estatisticas.add(j, op);
						// i = n;
						// j = n;
						// } else if (qty < tmp) {
						// this.estatisticas.add(j - 1, op);
						// i = n;
						// j = n;
						// } else if (j + 1 == n) {
						// this.estatisticas.add(op);
						// i = n;
						// }
						// }
						// }
					}
				} else {
					if (i + 1 == n) {
						this.estatisticas.add(op);
						i = n;
					}
					// else if (qty < tmp) {
					// this.estatisticas.add(i, op);
					// i = n;
					// }
				}
			}
		}
	}

	/**
	 * Método que dá a posição para ordenar por ordem alfabética no método
	 * 'inserOrdenado()';
	 * 
	 * @param s
	 *            Operador a ordenar devem actualizar o array 'estatisticas'
	 * 
	 */
	public String posicao(String s, int op) {
		int p = 0;
		String str = "";

		switch (s) {

		case "+":
			p = 1;
			str = "Adicao";
			break;// Adicao
			// case "acosd":
		case "acos":
			p = 2;
			str = "Arco co-seno";
			break;// Arco co-seno
			// case "asind":
		case "asin":
			p = 3;
			str = "Arco seno";
			break;// Arco seno
			// case "cosd":
		case "cos":
			p = 4;
			str = "Co-seno";
			break;// Co-seno
			// case "coshd":
		case "cosh":
			p = 5;
			str = "Co-seno hiperbólico";
			break;// Co-seno hiperbólico
		case "10^x":
			p = 6;
			str = "Dez elevado a X";
			break;// Dez elevado a X
		case "/":
			p = 7;
			str = "Divisao";
			break;// Divisao
		case "e":
			p = 8;
			str = "Exponencial";
			break;// Exponencial
		case "e^x":
			p = 9;
			str = "Exponencial de x";
			break;// Exponencial de x
		case "n!":
			p = 10;
			str = "Factorial";
			break;// Factorial
		case "1/x":
			p = 11;
			str = "Inverso de x";
			break;// Inverso de x
		case "Log2":
			p = 12;
			str = "Log base 2";
			break;// Log base 2
		case "Log10":
			p = 13;
			str = "Log base 10";
			break;// Log base 10
		case "Log":
			p = 14;
			str = "Log Nepperiano";
			break;// Log Nepperiano
		case "+/-":
			p = 15;
			str = "MaisouMenos";
			break;// Mais Menos
		case "*":
			p = 16;
			str = "Multiplicação";
			break;// Multiplicação
		case "π":
			p = 17;
			str = "Pi";
			break;// Pi
		case "x^2":
			p = 18;
			str = "QuadradoDeX";
			break;// QuadradoDeX
		case "3√x":
			p = 19;
			str = "Raiz Cúbica";
			break;// Raiz Cúbica
		case "sqrt":
			p = 20;
			str = "Raiz Quadrada";
			break;// Raiz Quadrada
		case "y√x":
			p = 21;
			str = "Raiz y de x";
			break;// Raiz y de x
		case "sind":
		case "sin":
			p = 22;
			str = "Seno";
			break;// Seno
			// case "sinhd":
		case "sinh":
			p = 23;
			str = "Seno hiperbólico";
			break;// Seno hiperbólico
		case "-":
			p = 24;
			str = "Subtracao";
			break;// Subtracao
			// case "tand":
		case "tan":
			p = 25;
			str = "Tangente";
			break;// Tangente
			// case "tanhd":
		case "tanh":
			p = 26;
			str = "Tangente hiperbólica";
			break;// Tangente hiperbólica
		case "x^3":
			p = 27;
			str = "x ao cubo";
			break;// x ao cubo
		case "x^y":
			p = 28;
			str = "x elevado a y";
			break;// x elevado a y

		default:
			p = 0;
			break;
		}
		if (op == 0)
			return Integer.toString(p);
		else
			return str;
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
