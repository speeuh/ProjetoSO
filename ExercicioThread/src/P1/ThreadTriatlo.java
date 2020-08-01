/*Objetivo:     Numa prova de triatlo moderno, o circuito se da da seguinte maneira:
				- 3Km de corrida onde os atletas correm entre 20 e 25 m / 30 ms
				- 3 tiros ao alvo com pontuacao de 0 a 10
				- 5 km de ciclismo onde os atletas pedalam entre 30 e 40 m/ 40 ms
				25 atletas participam da prova e largam juntos, no entanto, apenas 5 armas de tiro estao a
				disposicao. Cada atleta leva de 0,5 a 3s por tiro. Conforme os atletas finalizam o circuito de
				corrida, em ordem de chegada, pegam a arma para fazer os disparos. Uma vez encerrados os
				disparos, a arma e liberada para o proximo, e o atleta segue para pegar a bicicleta e continuar
				o circuito.
				Para determinar o ranking final dos atletas, considera-se a seguinte regra:
				- O primeiro que chegar recebe 250 pontos, o segundo recebe 240, o terceiro recebe
				230, ... , o ultimo recebe 10.
				- Soma-se a  pontuacao de cada atleta, o total de pontos obtidos nos 3 tiros (somados)
				Ordenar a pontuacao e exibir o resultado final do maior pontuador para o menor.
*/
package P1;

import java.util.concurrent.Semaphore;

public class ThreadTriatlo extends Thread {

	private int id;
	private static int posicaoChegada;
	private static int pontuacaoTOTAL = 250;
	private int pontuacaoFINAL = 0;
	private Semaphore semaforo;
	private static int[][] matriz = new int[26][2];
	int pontosTiro;
	

	public ThreadTriatlo (int id, Semaphore semaforo) {
		this.id = id;
		this.semaforo = semaforo;
	}

	public void run() {
		triatlo();
		
		try {
			semaforo.acquire();
			tiro();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforo.release();
			ciclismo();
			pontuacao();
		}
	}

	private void triatlo() {
		int distanciaTotal = 3000;
		int distanciaPercorrida = 0;
		int tempo = 30;
		
		while(distanciaPercorrida < distanciaTotal) {
			int desloc = (int) ((Math.random() * 6) + 20);
			distanciaPercorrida += desloc;
			try {
				sleep(tempo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Corredor #%d correu:%d \n", id, distanciaPercorrida);
		}
	}

	private void tiro() {
		int tempo = (int) ((Math.random() * 2600) + 500);
		for(int i = 1; i < 4; i++) {
			try {
				sleep(tempo);
				int pontuacao = (int) (Math.random() *11);
				pontosTiro += pontuacao;
				System.out.printf("Corredor #%d pontuou:%d pelo tiro %d \n", id, pontuacao, i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void ciclismo() {
		int distanciaTotal = 5000;
		int distanciaPercorrida = 0;
		int tempo = 40;
		
		while(distanciaPercorrida < distanciaTotal) {
			int desloc = (int) ((Math.random() * 11) + 30);
			try {
				sleep(tempo);
				distanciaPercorrida += desloc;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.printf("Corredor #%d pedalou:%d \n", id, distanciaPercorrida);
		}
	}
	
	private void pontuacao() {
		pontuacaoFINAL = pontuacaoTOTAL + pontosTiro;
		System.out.printf("O corredor #%d chegou na posição %d e pontuou %d: \n", id, ++posicaoChegada, pontuacaoFINAL);
		matriz[posicaoChegada][0] = id;
		matriz[posicaoChegada][1] = pontuacaoFINAL;
		pontuacaoTOTAL -= 10; 
		if(posicaoChegada == 25) {
			ordenar();
		}
	}
	
	private void ordenar() {
		matriz[0][1] = 9999; // espaco 0 nunca sera usado, valor alto para evitar trocas
		for (int x = 0; x < matriz.length; x++) {
			for (int y = 1; y < matriz.length -1; y++) {
				if (matriz[y][1] < matriz[y + 1][1]) {
					int aux = matriz[y][1];
					matriz[y][1] = matriz[y + 1][1];
					matriz[y + 1][1] = aux;

					int aux1 = matriz[y][0];
					matriz[y][0] = matriz[y + 1][0];
					matriz[y + 1][0] = aux1;
				}
			}
		}
		for (int i = 1; i < 26; i++) {
			System.out.println("O Corredor#" + matriz[i][0] + " tem " + matriz[i][1] + " pontos!");
		}
	}
}
