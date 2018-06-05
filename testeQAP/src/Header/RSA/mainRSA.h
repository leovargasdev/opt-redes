/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

#include "parametrosRSA.h"
#include "grafo.h"
#include "RSA.h"

void clonaMatrizes(double *matrizAdjacencias, double *matrizAdjacenciasAux) {
	for (int i = 0; i < quantidadeVertices; i++) {
		for (int j = 0; j < quantidadeVertices; j++) {
			matrizAdjacenciasAux[i* quantidadeVertices + j] = matrizAdjacencias[i* quantidadeVertices + j];
		}
	}
}

void atualizaMatrizAux(double *matrizAdjacencias, int *caminhoPrincipal) {
	int i, j;
	for (i = 0; i < quantidadeVertices; i++) {
		if (caminhoPrincipal[i + 1] != -1) {
			matrizAdjacencias[caminhoPrincipal[i] * quantidadeVertices + caminhoPrincipal[i + 1]] = 0;
		}
		else {
			i = quantidadeVertices;
		}
	}
}

void limpaCaminhos(int *caminhoPrincipal, int *caminhoSecundario, int *janelaSpectroInt) {
	int u;
	for (int u = 0; u < quantidadeVertices; u++) {
		caminhoPrincipal[u] = -1;
		caminhoSecundario[u] = -1;
	}

	for (u = 0; u < tamanhoJanelaSpectro; u++)
		janelaSpectroInt[u] = -1;
}

void verificaEnalecesRepetidos(int *caminhoPrincipal, int *caminhoSecundario) {
	Enlace enlace;

	vector<Enlace> enlacesPrincipal;
	vector<Enlace> enlacesSecundario;

	int *caminhoAux = new int[quantidadeVertices];

	int i, j;
	int fimEnlacePrincipal = 0;
	int fimEnlaceSecundario = 0;

	for (i = 0; i < quantidadeVertices; i++) {
		caminhoAux[i] = caminhoPrincipal[i];
	}

	//L� os dois caminhos e faz os pares de vertices que compoem o enlace
	for (i = 0; i < quantidadeVertices; i++) {
		if (caminhoPrincipal[i] != -1 && caminhoSecundario[i] != -1) {
			if (caminhoPrincipal[i] != -1) {
				enlace.no1 = caminhoPrincipal[i];
				enlace.no2 = caminhoPrincipal[i + 1];

				enlacesPrincipal.push_back(enlace);
			}

			if (caminhoSecundario[i] != -1) {
				enlace.no1 = caminhoSecundario[i];
				enlace.no2 = caminhoSecundario[i + 1];

				enlacesSecundario.push_back(enlace);
			}
		}
		else {
			i = quantidadeVertices;
		}
	}

	//Verifica se existe enlaces que se repetem 
	for (i = 0; i < enlacesPrincipal.size() - 1; i++) {
		for (j = 0; j < enlacesSecundario.size() - 1; j++) {
			if ((enlacesPrincipal[i].no1 == enlacesSecundario[j].no1 && enlacesPrincipal[i].no2 == enlacesSecundario[j].no2) ||
				(enlacesPrincipal[i].no2 == enlacesSecundario[j].no1 && enlacesPrincipal[i].no1 == enlacesSecundario[j].no2)) {
				fimEnlacePrincipal = i;
				fimEnlaceSecundario = j;
			}
		}
	}

	//printf("%d %d", fimEnlacePrincipal, fimEnlaceSecundario);
	int continua = 0;
	if (fimEnlacePrincipal > 0 && fimEnlaceSecundario > 0)
	{
		//Ajusta Caminhos
		for (i = 0, j = 0; i < quantidadeVertices; i++, j++) {
			if (i == fimEnlacePrincipal  && continua == 0) {
				continua = 1;
				i = fimEnlaceSecundario + 1;
			}

			if (continua == 1) {
				caminhoPrincipal[j] = caminhoSecundario[i];
			}
		}
		continua = 0;
		for (i = 0, j = 0; i < quantidadeVertices && j < quantidadeVertices; i++, j++) {
			if (i == fimEnlaceSecundario && continua == 0) {
				continua = 1;
				i = fimEnlacePrincipal + 1;
			}

			if (continua == 1) {
				caminhoSecundario[j] = caminhoAux[i];
			}
		}
	}
}

int buscaPrimeiraRequisaoDisponivel(int *requisicoesAtivas, int requisicoesSimultaneas) {
	for (int i = 0; i < requisicoesSimultaneas; i++) {
		if (requisicoesAtivas[i] == 0) {
			return i;
		}
	}
	return -1;
}

vector<NoGrafo> retirarRequisicoes(vector<NoGrafo> grafo, int * requisicoesAtivas, int requisicoesSimultaneas, int numeroRequisicoesRetirar) {
	int retirarRequisao, i, j, k;
	NoGrafo noGrafo;
	/*srand(quantidadeVertices);*/
	int *janelaSpectro = new int[tamanhoJanelaSpectro];

	for (i = 0; i < numeroRequisicoesRetirar; i++) {
		retirarRequisao = rand() % requisicoesSimultaneas;

		for (j = 0; j < grafo.size(); j++) {

			janelaSpectro = caminhoParaVetorRSA(janelaSpectro, grafo[j].janelaSpectro, tamanhoJanelaSpectro);

			for (k = 0; k < tamanhoJanelaSpectro; k++) {
				if (janelaSpectro[k] == retirarRequisao) {
					janelaSpectro[k] = -1;
				}
			}

			strcpy(grafo[j].janelaSpectro, caminhoParaStringRSA(janelaSpectro, tamanhoJanelaSpectro));
			requisicoesAtivas[retirarRequisao] = 0;
		}
	}


	return grafo;
}


double calculaFragmentacaoRede(vector<NoGrafo> grafo) {
	int i, j;
	int *janelaSpectro = new int[tamanhoJanelaSpectro];

	double fragmentacaoJanela = 0.0, fragmentacaoTotal = 0.0, totalLivres = 0.0, maiorBlocoLivre = 0.0, maiorBlocoLivreAux = 0.0;

	for (i = 0; i < grafo.size(); i++) {
		janelaSpectro = caminhoParaVetorRSA(janelaSpectro, grafo[i].janelaSpectro, tamanhoJanelaSpectro);

		for (j = 0; j < tamanhoJanelaSpectro; j++) {
			if (janelaSpectro[j] == -1) {
				maiorBlocoLivreAux++;
				totalLivres++;

				if (maiorBlocoLivreAux > maiorBlocoLivre)
					maiorBlocoLivre = maiorBlocoLivreAux;
			}
			else {
				maiorBlocoLivreAux = 0;
			}
		}
		if (totalLivres > 0)
			fragmentacaoTotal += 1 - (maiorBlocoLivre / totalLivres);
	}


	return fragmentacaoTotal / numeroLinks;
}

void mainRSA(char *caminho, vector<Vertice> pontos) {
	srand(quantidadeVertices);
	int *nosUtilizados = new int[quantidadeVertices];
	double fragmentacaoRede;
	int *caminhoPrincipal = new int[quantidadeVertices];
	int *caminhoSecundario = new int[quantidadeVertices];
	int *caminhoVet = new int[quantidadeVertices];
	int * janelaSpectroInt = new int[tamanhoJanelaSpectro];
	NoGrafo noGrafo;
	vector<NoGrafo> grafo;

	int i, j;
	char janelaSpectroChar[tamanhoStringRSA];
	/////////////////////////////////////////////////////
	//quantidadeVertices = 6;
	/////////////////////////////////////////////////////

	double *matrizAdjacencias = new double[quantidadeVertices * quantidadeVertices];
	double *matrizAdjacenciasAux = new double[quantidadeVertices * quantidadeVertices];


	for (j = 0; j < quantidadeVertices; j++) {
		nosUtilizados[j] = 0;
		caminhoVet[j] = -1;
	}
	

	limpaCaminhos(caminhoPrincipal, caminhoSecundario, janelaSpectroInt);

	strcpy(janelaSpectroChar, caminhoParaStringRSA(janelaSpectroInt, tamanhoJanelaSpectro));

	calculaMatrizAdjacencias(matrizAdjacencias, caminhoParaVetor(caminhoVet, caminho));

	clonaMatrizes(matrizAdjacencias, matrizAdjacenciasAux);

	for (i = 0; i < quantidadeVertices; i++) {
		for (j = 0; j < quantidadeVertices; j++) {
			if (i > j) {
				if (matrizAdjacencias[i * quantidadeVertices + j] > 0) {
					strcpy(noGrafo.janelaSpectro, janelaSpectroChar);
					noGrafo.no1 = i;
					noGrafo.no2 = j;


					grafo.push_back(noGrafo);

					strcpy(noGrafo.janelaSpectro, "");
					noGrafo.no1 = 0;
					noGrafo.no2 = 0;
				}

			}
		}
	}

	limpaCaminhos(caminhoPrincipal, caminhoSecundario, janelaSpectroInt);

	requisicoesSimultaneas = tamanhoJanelaSpectro;
	double totalDeRequisicoes = 900;
	int numeroRequisicoesRetirar = requisicoesSimultaneas * 20 / 100;
	int requisicao = 0;
	int *requisicoesAtivas = new int[requisicoesSimultaneas];

	for (i = 0; i < requisicoesSimultaneas; i++)
		requisicoesAtivas[i] = 0;

	int noOrigem, noDestino, blocos;

	totalBloqueios = 0;
	totalBloqueiosPrincipal = 0;
	totalBloqueisSecundarios = 0;
	for (i = 0; i < totalDeRequisicoes; i++) {

		if (i % 100 == 0) {
			printf("%d,%f\n", i, totalBloqueios);
		}

		requisicao = buscaPrimeiraRequisaoDisponivel(requisicoesAtivas, requisicoesSimultaneas);

		if (requisicao == -1) {
			retirarRequisicoes(grafo, requisicoesAtivas, requisicoesSimultaneas, numeroRequisicoesRetirar);
			requisicao = buscaPrimeiraRequisaoDisponivel(requisicoesAtivas, requisicoesSimultaneas);
		}



		noOrigem = rand() % (quantidadeVertices);
		noDestino = rand() % (quantidadeVertices);
		blocos = (rand() % 4);

		if (noOrigem == noDestino) {
			if (noOrigem >= 0 && noOrigem < quantidadeVertices - 1) {
				noOrigem = noOrigem + 1;
			}

			if (noOrigem == quantidadeVertices - 1) {
				noOrigem = noOrigem - 1;
			}
		}

		if (blocos == 0) {
			blocos = 1;
		}
		else if (blocos == 3) {
			blocos = 4;
		}


		caminhoPrincipal = dijkstra(caminhoPrincipal, matrizAdjacencias, noOrigem, noDestino);

		atualizaMatrizAux(matrizAdjacenciasAux, caminhoPrincipal);

		caminhoSecundario = dijkstra(caminhoSecundario, matrizAdjacenciasAux, noOrigem, noDestino);

		verificaEnalecesRepetidos(caminhoPrincipal, caminhoSecundario);

		for (j = 0; j < quantidadeVertices; j++) {
			nosUtilizados[caminhoPrincipal[j]] ++;
			nosUtilizados[caminhoSecundario[j]] ++;
		}

		grafo = RSA(grafo, caminhoPrincipal, caminhoSecundario, blocos, requisicao);

		limpaCaminhos(caminhoPrincipal, caminhoSecundario, janelaSpectroInt);

		clonaMatrizes(matrizAdjacencias, matrizAdjacenciasAux);

		requisicoesAtivas[requisicao] = 1;

	}
	fragmentacaoRede = 0;
	fragmentacaoRede = calculaFragmentacaoRede(grafo);
	int noCentral, maisUsado = 0;
	for (j = 0; j < quantidadeVertices; j++) {
		if (nosUtilizados[j] > maisUsado) {
			noCentral = j;
			maisUsado = nosUtilizados[j];
		}
	}

	printf("%d,%f\n", i, totalBloqueios);
	printf("\nTotal de Requisicoes %f, Total de Bloqueios %f, TAxa de Bloqueio ,%f ,%d, Principais, %d, Secundario, Fragmentacao ,%f, No centra N� , %s\n", totalDeRequisicoes, totalBloqueios, (totalBloqueios / totalDeRequisicoes), totalBloqueiosPrincipal, totalBloqueisSecundarios, fragmentacaoRede, pontos[noCentral].nome);

	montaSaidaRSA(tipoRede, pontos, caminhoVet, fragmentacaoRede, totalDeRequisicoes, totalBloqueios, noCentral);

}
