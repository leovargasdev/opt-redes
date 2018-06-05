/*
	Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapecó - SC
	Trabalho de Conclusão de Curso:
		- Ciência da Computação
		- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações
*/

//Classe que contém os caminhos e seus custos
class Caminho {
public:
	char caminho[tamanhoString];
	double custoCaminho;

	//Função utilizada no SORT dos melhores caminhos
	double getCusto() const {
		return custoCaminho;
	}

};

//Funções Globais
bool comparaCusto(Caminho a, Caminho b) {
	return a.getCusto() < b.getCusto();
}
