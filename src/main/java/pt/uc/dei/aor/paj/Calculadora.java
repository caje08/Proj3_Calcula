package pt.uc.dei.aor.paj;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

/**
 * @author Carlos Santos e Filipa Pedrosa
 * Classe de recebe e trata os dados em cada sessão xhtml da aplicação "Calculadora". Reserva também o histórico
 * das operações efectuadas em cada sessão.
 */

@Named
@SessionScoped
public class Calculadora implements Serializable {

	private static final long serialVersionUID = 5640030917608265103L;

	private String nome;
	private ArrayList<String> nomes;
	private ArrayList<Operacao> historico;
	private ArrayList<String> linhacmd;
	private ArrayList<Integer> linhaindx;
	private String paramtext;
	private String opang;
	private Boolean angul, limpar;
	private int conta;
	private int cecont;
	private int contador; // for�a que ap�s o igual ('='), o pr�ximo
							// caracter encontre o display limpo e n�o
							// concatene o valor
	@Inject
	private Stats stats;

	// private String explang="1+1";

	public Calculadora() {
		this.nomes = new ArrayList<String>();
		this.historico = new ArrayList<Operacao>();
		this.linhacmd = new ArrayList<String>();
		this.linhaindx = new ArrayList<Integer>();
		this.paramtext = "";
		this.angul = true;
		this.limpar = false;
		this.conta = 1;
		this.contador = 0;
		this.opang = "1";
		this.cecont = 0;
	}

	public String getOpang() {
		return opang;
	}

	public void setOpang(String opang) {
		this.opang = opang;
	}

	public ArrayList<String> getNomes() {
		return nomes;
	}

	public void setNomes(ArrayList<String> nomes) {
		this.nomes = nomes;
	}

	public String getParamtext() {
		return paramtext;
	}

	public void setParamtext(String paramtext) {
		this.paramtext = paramtext;
	}

	public Boolean getAngul() {
		return angul;
	}

	public void setAngul(Boolean angul) {
		this.angul = angul;
	}

	/**
	 * Método que na classe Calculadora devolve o valor em radianos 
	 * @param ang valor dos angulos em graus
	 * @return Devolve um double com o valor do angulo em radianos  
	 */
	public Double toRadians(double ang) {
		double rad = 0.0;

		rad = ang * (Math.PI / 180);
		return rad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getLimpar() {
		return limpar;
	}

	public void setLimpar(Boolean limpar) {
		this.limpar = limpar;
	}

	public int getConta() {
		return conta;
	}

	public void setConta(int conta) {
		this.conta = conta;
	}

	public ArrayList<Operacao> getHistorico() {
		return historico;
	}

	public void setHistorico(ArrayList<Operacao> historico) {
		this.historico = historico;
	}

	public void noDisplay(String x) {
		paramtext = x;
	}
	/**
	 * Método que na classe Calculadora analiza o evento da tecla pressionada pelo utilizador
	 * @param event evento gerado pelo Actionlistener no ficheiro xhtml (tecla/operação) pressionada
	 * 
	 */
	public void keyAdd(ActionEvent event) {
		String txt = "";
		int nc = 0;// n� de caracteres do �ltimo numero introduzido e que v�o
					// ser retirados de 'paramtext' para adicionar com a
					// altera��o necess�ria
		boolean flag = false;// quando a 'true' faz a reset do display (fun��o
		int indx = 0; // 'C')

		Operacao operacao;

		switch (event.getComponent().getId()) {
		case "n0":
			txt = "0";
			break;
		case "n1":
			txt = "1";
			break;
		case "n2":
			txt = "2";
			break;
		case "n3":
			txt = "3";
			break;
		case "n4":
			txt = "4";
			break;
		case "n5":
			txt = "5";
			break;
		case "n6":
			txt = "6";
			break;
		case "n7":
			txt = "7";
			break;
		case "n8":
			txt = "8";
			break;
		case "n9":
			txt = "9";
			break;
		case "mais":
			linhacmd.add("+");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "+";
			// linhcomand += txt + ",";
			// numcmd++;

			break;
		case "menos":
			linhacmd.add("-");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "-";
			// linhcomand += txt + ",";
			// numcmd++;

			break;
		case "multip":
			linhacmd.add("*");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "*";
			// linhcomand += txt + ",";
			// numcmd++;

			break;
		case "divis":
			linhacmd.add("/");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "/";
			// linhcomand += txt + ",";
			// numcmd++;

			break;
		case "clear":
			txt = "";
			flag = true;
			break;
		case "percent":
			linhacmd.add("%");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = ultimoNumero(paramtext);
			nc = txt.length();
			if (txt.length() > 0) {
				txt = avalia(txt + "/100");
			} else
				txt = "";
			// linhcomand += "%,";
			// numcmd++;

			break;
		case "bcksp":
			if (paramtext.length() > 0 && !limpar) {
				paramtext = paramtext.substring(0, paramtext.length() - 1);
				if (linhaindx.size() > 0)
					checkIndx(paramtext.length());
			} else
				paramtext = "";
			break;
		case "ce":// vai limpar uma vez até ao último operador básico
					// introduzido ("+", "-","*","/")
			if (!limpar) {
				if (linhaindx.size() > 0) {
					for (int i = linhaindx.size() - 1; i >= 0; i--) {
						if (linhacmd.get(i).equals("+")
								|| linhacmd.get(i).equals("-")
								|| linhacmd.get(i).equals("*")
								|| linhacmd.get(i).equals("/")) {
							paramtext = paramtext.substring(0,
									linhaindx.get(i) + 1);
							cecont = linhaindx.get(i);
							i = -1;
						}
					}
				} else
					paramtext = "";
			} else
				paramtext = "";
			break;
		case "maismenos":
			linhacmd.add("+/-");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = ultimoNumero(paramtext);
			nc = txt.length();
			if (txt.charAt(0) != '-')
				txt = "(-" + txt + ")";
			// linhcomand += "+/-,";
			// numcmd++;

			break;
		case "igual":
			if (paramtext.length() > 0) {
				txt = express();
				operacao = new Operacao(conta, paramtext, txt);
				historico.add(operacao);
				nomes.add(operacao.getComando());
				// stats.addStats(operacao.getComando(),linhcomand,numcmd);
				// stats.newaddStats(linhacmd);
				// linhcomand = "";
				// numcmd = 0;
				conta++;
				if (contador != 2)
					contador = 1;// limpa o display para apresentar o valor da
									// operacao

			}
			break;
		case "sqrt":
			linhacmd.add("sqrt");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "sqrt";
			// linhcomand += "," + txt;
			// numcmd++;

			break;
		case "ponto":
			txt = ".";
			break;
		case "umdivx":
			linhacmd.add("1/x");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = ultimoNumero(paramtext);
			nc = txt.length();
			txt = avalia("1/" + txt);
			// linhcomand += ",1/x";
			// numcmd++;
			break;

		case "ln":
			linhacmd.add("log");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "log(";
			break;
		case "parcd":
			txt = "(";
			break;
		case "parce":
			txt = ")";
			break;
		case "asin":
			linhacmd.add("asin");
			indx = paramtext.length();
			linhaindx.add(indx);
			if (getAngul())
				txt = "asin(";
			else
				txt = "asind(";
			break;
		case "sinh":
			linhacmd.add("sinh");
			indx = paramtext.length();
			linhaindx.add(indx);
			if (getAngul())
				txt = "sinh(";
			else
				txt = "sinhd(";
			break;
		case "sin":
			linhacmd.add("sin");
			indx = paramtext.length();
			linhaindx.add(indx);
			System.out.println("Flag =" + getAngul());
			if (getAngul())
				txt = "sin(";
			else
				txt = "sind(";
			break;
		case "xquad":
			linhacmd.add("x^2");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "^2";
			break;
		case "nfact":
			linhacmd.add("n!");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "!(";
			break;
		case "acos":
			linhacmd.add("acos");
			indx = paramtext.length();
			linhaindx.add(indx);
			if (getAngul())
				txt = "acos(";
			else
				txt = "acosd(";
			break;
		case "cosh":
			linhacmd.add("cosh");
			indx = paramtext.length();
			linhaindx.add(indx);
			if (getAngul())
				txt = "cosh(";
			else
				txt = "coshd(";
			break;
		case "cos":
			linhacmd.add("cos");
			indx = paramtext.length();
			linhaindx.add(indx);
			if (getAngul())
				txt = "cos(";
			else
				txt = "cosd(";
			break;
		case "xlevy":
			linhacmd.add("x^y");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "^(";
			break;
		case "raizdey":
			linhacmd.add("y√x");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "^(1/";
			break;
		case "pi":
			linhacmd.add("π");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "pi";
			break;
		case "tanh":
			linhacmd.add("tanh");
			indx = paramtext.length();
			linhaindx.add(indx);
			if(getAngul())
				txt = "tanh(";
			else
				txt = "tanhd(";
				break;
		case "tan":
			linhacmd.add("tan");
			indx = paramtext.length();
			linhaindx.add(indx);
			if (getAngul())
				txt = "tan(";
			else
				txt = "tand(";
			break;
		case "xcub":
			linhacmd.add("x^3");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "^3";
			break;
		case "raizde3":
			linhacmd.add("3√x");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "^(1/3)";
			break;
		case "exp":
			linhacmd.add("e");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "e";
			break;
		case "expx":
			linhacmd.add("e^x");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "e^(";
			break;
		case "log2":
			linhacmd.add("log2");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "log2(";
			break;
		case "log10":
			linhacmd.add("log10");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "log10(";
			break;
		case "dezx":
			linhacmd.add("10^x");
			indx = paramtext.length();
			linhaindx.add(indx);
			txt = "10^(";
			break;
		default:
			txt = "0";
			break;
		}
		if (flag) { // "clear" foi selecionado
			paramtext = "";
			limpar = false;
		} else {
			if (nc > 0) {// significa que o �ltimo n� de 'paramtext' precisa de
							// ser substituido
				paramtext = paramtext.substring(0, paramtext.length() - nc);
			}
			// paramtext = paramtext + txt;
			if (contador == 0) {
				paramtext = paramtext + txt;

			} else {
				paramtext = txt;
				contador--;
				// limpar=false;
			}
		}
	}
	/**
	 * Método que na classe Calculadora que verifica qual o indice guardado na ultima posicao do Array e se 
	 * o "backspace" vai apagar o comando introduzido. Neste caso ele remove o comando do Array. 
	 * @param n valor do indice do item a verificar
	 *   
	 */
	public void checkIndx(int n) {
		int dim = linhaindx.size();

		// for(int i=dim-1; i>0; i--){
		if (linhaindx.get(dim - 1) >= n) {
			linhacmd.remove(dim - 1);
			linhaindx.remove(dim - 1);
		}
		// }
	}
	/**
	 * Método que na classe Calculadora cria o operador 'factorial' para ser usado pela função 'Expressbuilder' 
	 * @param args argumento queidentifica o valor inteiro do factorial a calcular
	 * @return Devolve um double com o valor resultado do factorial  
	 */
	Operator factorial = new Operator("!", 1, true,
			Operator.PRECEDENCE_POWER + 1) {

		@Override
		public double apply(double... args) {
			final int arg = (int) args[0];
			if ((double) arg != args[0]) {
				throw new IllegalArgumentException(
						"Operador para o factorial tam que ser um inteiro");
			}
			if (arg < 0) {
				throw new IllegalArgumentException(
						"O operador do factorial não pode ser menor que zero");
			}
			double result = 1;
			for (int i = 1; i <= arg; i++) {
				result *= i;
			}
			return result;
		}
	};

	// definição de novas funções para aplicar na expressao 'ExpressionBuilder'

	Function cosd = new Function("cosd", 1) {
		@Override
		public double apply(double... args) {
			return Math.cos(Math.toRadians(args[0]));
		}
	};

	Function sind = new Function("sind", 1) {
		@Override
		public double apply(double... args) {
			return Math.sin(Math.toRadians(args[0]));
		}
	};
	Function tand = new Function("tand", 1) {
		@Override
		public double apply(double... args) {
			return Math.tan(Math.toRadians(args[0]));
		}
	};
	Function coshd = new Function("coshd", 1) {
		@Override
		public double apply(double... args) {
			return Math.cosh(Math.toRadians(args[0]));
		}
	};

	Function sinhd = new Function("sinhd", 1) {
		@Override
		public double apply(double... args) {
			return Math.sinh(Math.toRadians(args[0]));
		}
	};
	Function tanhd = new Function("tanhd", 1) {
		@Override
		public double apply(double... args) {
			return Math.tanh(Math.toRadians(args[0]));
		}
	};
	Function asind = new Function("asind", 1) {
		@Override
		public double apply(double... args) {
			return Math.asin(Math.toRadians(args[0]));
		}
	};
	Function acosd = new Function("acosd", 1) {
		@Override
		public double apply(double... args) {
			return Math.acos(Math.toRadians(args[0]));
		}
	};
	/**
	 * Método que na classe Controladora que faz a operação pedida pelo utilizador e devolve uma String com o valor a apresentar no display  
	 * @param  Não tem valores de entrada
	 * @return Devolve uma String com o valor da expressao resultante do cálculo introduzido  
	 */
	public String express() {

		String mess = "";
		try {
			Expression e = new ExpressionBuilder(paramtext)
					.variables("pi", "e").operator(factorial).function(cosd)
					.function(sind).function(tand).function(sinhd)
					.function(coshd).function(tanhd).function(asind)
					.function(acosd).build().setVariable("pi", Math.PI)
					.setVariable("e", Math.E);
			// resultado = String.valueOf(e.evaluate());
			double res = e.evaluate();
			// _result = Double.toString(res);
			if (res % 1 == 0)
				mess = Integer.toString((int) res);
			else
				mess = Double.toString(res);
			stats.newaddStats(linhacmd);
			linhacmd.removeAll(linhacmd);
			linhaindx.removeAll(linhaindx);
		} catch (IllegalArgumentException o) {
			o.getMessage();
			mess = "Erro de sintaxe";
			contador = 2;
		} catch (ArithmeticException p) {
			p.getMessage();
			mess = "Divisão por zero";
			contador = 2;
		}
		return mess;
	}
	/**
	 * Método que na classe Calculadora avalia o resultado de uma operacao 
	 * @param x String com a expressao de calculo a avaliar pelo 'ExpressionBuilder'
	 * @return Devolve uma String com o valor da expressao resultante do cálculo introduzido   
	 */
	public String avalia(String x) {

		String mess = "";
		try {
			Expression e = new ExpressionBuilder(x).build();
			try {
				// resultado = String.valueOf(e.evaluate());
				double res = e.evaluate();
				// _result = Double.toString(res);
				if (res % 1 == 0)
					mess = Integer.toString((int) res);
				else
					mess = Double.toString(res);

			} catch (Exception o) {
				mess = o.getMessage();
				mess = "Erro de sintaxe";
				contador = 2;
			}
		} catch (Exception o) {
			mess = "Divisão por zero";
			contador = 2;
		}
		return mess;
	}
	/**
	 * Método que na classe Calculadora dá o último número introduzido pelo utilizador 
	 * @param x String com a expressao actual no display
	 * @return Devolve uma String com o valor do ultimo numero na expressao   
	 */
	public String ultimoNumero(String x) {
		String tmp = "", saida = "";
		char c;

		for (int i = paramtext.length() - 1; i >= 0; i--) {
			c = paramtext.charAt(i);
			if (Character.isDigit(c) || c == '.')
				tmp = tmp + c;
			else
				i = -1;
		}
		if (tmp.length() > 0) {
			for (int i = tmp.length() - 1; i >= 0; i--) {
				saida = saida + tmp.charAt(i);
			}
		}
		return saida;
	}
	/**
	 * Método que na classe Calculadora elimina a operacao do historico definida pelo utilizador 
	 * @param operacao referencia do item a remover do Array de historico
	 *   
	 */
	public void doEliminar(Operacao operacao) {
		historico.remove(operacao);
	}

	public void logout() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		ec.invalidateSession();
		ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
	}
}
