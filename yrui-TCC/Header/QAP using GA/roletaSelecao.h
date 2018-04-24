/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

int roletaSeleciona(double *P) {
	int i;

	double valorRand;
	double somaP = 0.0;
	valorRand = ((double)rand() / (double)RAND_MAX);
	//Sorteia um valor double 
	for (i = 0; i < nPop; i++) {
		somaP += P[i];
		//P é a população, cada um da população recebe um valo, quando o valor sorteado for menor que a soma, traz o ultimo i
		//Exemplo: i = 0 tem valor 10 e i = 1 tem valor 15, e o sorteado foi o 20, no primeiro caso vai comaparar 15 > 20,
		//na proxima interação vai somar 10 + 15 e vai comparar com 25 > 20, e vai traz o i = 1, pois o valor sorteado,
		//esta no intervalo de i = 1;
		if (somaP > valorRand) {
			return i;
		}
	}

	return i - 1;
}