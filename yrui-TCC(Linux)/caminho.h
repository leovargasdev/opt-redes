//Classe que cont�m os caminhos e seus custos
class Caminho {
public:
	char caminho[tamanhoString];
	double custoCaminho;

	//Fun��o utilizada no SORT dos melhores caminhos
	double getCusto() const {
		return custoCaminho;
	}
};

//Fun��es Globais
bool comparaCusto(Caminho a, Caminho b) {
	return a.getCusto() < b.getCusto();
}
