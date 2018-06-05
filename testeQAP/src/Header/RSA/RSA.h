/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/
int posicaoInicial;

int verBloqueio(vector<NoGrafo> grafo, int *caminho, int blocos) {
	int i, j, k, no1, no2;

	int *janelaSpectroGrafo = new int[tamanhoJanelaSpectro];
	int *janelaSpectroOcupadaCaminho = new int[tamanhoJanelaSpectro];

	for (j = 0; j < tamanhoJanelaSpectro; j++) {
		janelaSpectroOcupadaCaminho[j] = -1;
		janelaSpectroGrafo[j] = -1;
	}

	for (i = 0; i < quantidadeVertices; i++) {
		if (caminho[i + 1] != -1) {
			no1 = caminho[i];
			no2 = caminho[i + 1];

			for (j = 0; j < grafo.size(); j++) {
				if ((grafo[j].no1 == no1 && grafo[j].no2 == no2) ||
					(grafo[j].no2 == no1 && grafo[j].no1 == no2)) {

					janelaSpectroGrafo = caminhoParaVetorRSA(janelaSpectroGrafo, grafo[j].janelaSpectro, tamanhoJanelaSpectro);

					for (k = 0; k < tamanhoJanelaSpectro; k++) {
						if (janelaSpectroGrafo[k] >= 0) {
							janelaSpectroOcupadaCaminho[k] = janelaSpectroGrafo[k];
						}
					}
				}
			}
		}
	}
	j = 0;
	posicaoInicial = 0;
	for (i = 0; i < tamanhoJanelaSpectro; i++) {

		if (janelaSpectroOcupadaCaminho[i] == -1) {
			j++;
		}
		else {
			posicaoInicial = i + 1;
		}

		if (j >= blocos) {
			return 0;
		}
	}

	return 1;
}

vector<NoGrafo> alocaRequisicaoGrafo(vector<NoGrafo> grafo, int *caminho, int posicaoInicial, int blocos, int requisicao) {
	int i, j, k, no1, no2;

	int *janelaSpectroGrafo = new int[tamanhoJanelaSpectro];
	int *janelaSpectroOcupadaCaminho = new int[tamanhoJanelaSpectro];

	for (i = 0; i < quantidadeVertices; i++) {
		if (caminho[i + 1] != -1) {
			no1 = caminho[i];
			no2 = caminho[i + 1];

			for (j = 0; j < grafo.size(); j++) {
				if ((grafo[j].no1 == no1 && grafo[j].no2 == no2) ||
					(grafo[j].no2 == no1 && grafo[j].no1 == no2)) {

					janelaSpectroGrafo = caminhoParaVetorRSA(janelaSpectroGrafo, grafo[j].janelaSpectro, tamanhoJanelaSpectro);

					for (k = posicaoInicial; k < posicaoInicial + blocos; k++) {
						janelaSpectroGrafo[k] = requisicao;
					}

					strcpy(grafo[j].janelaSpectro, caminhoParaStringRSA(janelaSpectroGrafo, tamanhoJanelaSpectro));
				}
			}
		}
	}

	return grafo;
}

vector<NoGrafo> RSA(vector<NoGrafo> grafo, int *caminhoPrincipal, int *caminhoSecundario, int blocos, int requisicao) {
	int posicaoInicialPrincipal;
	int posicaoInicialSecundario;
	int bloqueioPrincipal = 0;
	int bloqueioSecundario = 0;

	posicaoInicialPrincipal = 0;
	posicaoInicial = 0;
	bloqueioPrincipal = verBloqueio(grafo, caminhoPrincipal, blocos);

	if (bloqueioPrincipal == 0) {
		posicaoInicialPrincipal = posicaoInicial;
		posicaoInicialSecundario = 0;
		posicaoInicial = 0;
		bloqueioSecundario = verBloqueio(grafo, caminhoSecundario, blocos);
		posicaoInicialSecundario = posicaoInicial;
	}
	
	if (bloqueioPrincipal == 0 && bloqueioSecundario == 0) {
		grafo = alocaRequisicaoGrafo(grafo, caminhoPrincipal, posicaoInicialPrincipal, blocos, requisicao);
		grafo = alocaRequisicaoGrafo(grafo, caminhoSecundario, posicaoInicialPrincipal, blocos, requisicao);
	}
	else {
		totalBloqueios++;
		if (bloqueioPrincipal == 1) {
			totalBloqueiosPrincipal++;
		}
		if (bloqueioSecundario == 1) {
			totalBloqueisSecundarios++;
		}

	}
	

	
	return grafo;
}