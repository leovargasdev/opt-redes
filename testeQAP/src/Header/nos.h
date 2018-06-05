/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

class Vertice {
	public:
	int id;
	char *nome;
	double latitude;
	double longitude;
	int totalNumeroNos;

	vector<Vertice> leVertices()
	{
		//Vetor que cont�m todos os pontos
		vector<Vertice> pontos;
		
		//Dados do v�rtice
		Vertice vertice;

		FILE *arquivo;
		char linha[tamanhoString];
		int numVertices = 0;
		char *linhas[tamanhoString];

		int i = 0;
		// Abre um arquivo TEXTO para LEITURA
		arquivo = fopen(caminhoVertices, "rt");
		if (arquivo == NULL)  // Se houve erro na abertura
		{
			printf("Problemas na abertura do arquivo\n");
			fclose(arquivo);
			return pontos;
		}

		while (fgets(linha, sizeof linha, arquivo) != NULL)
		{
			//Adiciona linha ao vetor
			linhas[i] = strdup(linha);
			i++;
			//Conta a quantidade de linhas
			numVertices += 1;
		}

		int j;
		int posicao;
		char *token;

		//Separa os dados, e guarda no vetor
		for (j = 0; j < numVertices; j++) {
			if (j == 0) {
				vertice.totalNumeroNos = numVertices - 1;
			}
			else {
				token = strtok(linhas[j], "\t");
				posicao = 0;
				vertice.id = j-1;
				while (token != NULL) {

					switch (posicao) {
					case 0:
						vertice.nome = token;
						break;
					case 1:
						vertice.latitude = atof(token);
						break;
					case 2:
						vertice.longitude = atof(token);
						break;
					}
					token = strtok(NULL, "\t");
					posicao++;
				}
				pontos.push_back(vertice);
			}


		};
		fclose(arquivo);
		return pontos;
	}
};
