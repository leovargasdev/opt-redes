# README #
* Author: Yruí Giovan Neris - yruigneris@gmail.com - 2017. Currículo Lattes: http://lattes.cnpq.br/2613310044827573
* Universidade Federal da Fronteira Sul - Chapecó - SC
* Trabalho de Conclusão de Curso
*	- Ciência da Computação
*	- Problema Quadrático de Alocação aplicado ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações;
*   - Orientador: Claunir Pavan. Currículo Lattes: http://lattes.cnpq.br/7362574930328474;

### Propósito ###
 Este repositório contém o algortimo genético que aplica o Problema Quadrático de Alocação ao Mapeamento de Topologias de Redes Ópticas de Transporte de Telecomunicações, implementado na Disciplina de Conclusão de Curso do curso de Ciência da Computação da
 Universidade Federal da Fronteira Sul - Chapecó - SC.
 
### Resumo ###
 O surgimento de aplicações que utilizam e se beneficiam de altas demandas de tráfego de dados tem causado um crescimento elevado de tráfego
 nas redes de telecomunicações. As redes ópticas de telecomunicações se demonstram eficientes para atender as demandas existentes,
 porém a distância é um limitador de alcance por formato de modulação, quanto maior a distância menor será a capacidade de transferência de dados. 
 Neste trabalho foi desenvolvida uma meta-heurística aplicando o problema quadrático de alocação e algoritmo genético, ao mapeamento
 das redes ópticas de telecomunicações, minimizando a soma do comprimentos de seus enlaces, com a alteração do mapeamento da topologia. 
 Ao todo foram testadas 27 topologias do mundo real, e através da alteração do mapeamento foi possível minimizar o comprimento de 18 redes.

### Implementação ###
* Linguagem: C++;
* Ferramenta: Microsoft Visual Studio 2015;
* Sistema Operacional: Windows;
 
### Utilização ###
 As entradas para o algoritmo são topologias do Mundo real, as 27 topologias testadas estão em Nós - Lat Long, onde existem 27 redes Originais e 
 27 redes que passaram por um tramento de melhoria apresentadas no trabalho "How reliable are the real-world opticaltransport networks?" 
 (C. Pavan, L. S. de Lima, M. H. M. Paiva, and M. E. V. Segatto).
 
 A representação das redes são baseadas no conceito de Grafos.
 Para cada rede existem 2 arquivos, um arquivo contem os nós, trazendo as informações de Nome, Coordenadas de Latitude e Longitude (Mundo Real)
 , o outro arquivo contém as informações de como os nós se conectam, trazendo a informação do ID dos dois nós por linha, observando que a conexão é 
 bidirecional. A atribuição do ID para o nó é conforme a leitura, o primeiro é o nó 0, o segundo é o nó 1, e o nó n possui o ID n-1.
 
 As configurações de entrada do Algoritmo estão no arquivo Header/bibliotecas.h, nas variáveis:
*	caminhoVertices = Variável que aponta para o arquivo que contém as informações do Nome e Coordenadas do Nó;
*   caminhoArestas = Variável que aponta para o arquivo que contém as informações de conectividade dos Nós; 
*	caminhoSaida = Variável que aponta para a pasta que irá gravar o resultado do algoritmo;
*	melhorCaminhoRSA = Variável utilizada para aplicar o RSA ao caminho, onde a variavél recebe o caminho;
*	tipoRede = Original ou Harary, utilizada no arquivo de resultado;
	
 As variáveis caminhoVertices, caminhoArestas e caminhoSaida são utilizadas pelo Algoritmo Genético e RSA, para o funcionamento correto do programa é sempre
 necessário estar executando para a mesma rede, ou seja, caminhoVertices e caminhoArestas devem estar apontando para a mesma rede, se as redes não forem
 a mesma o algoritmo irá apresentar um erro em momentos onde o numéro de nós for diferente ao numero de nós do arquivo de conectividade.
  
* No menu existem 3 Opções;
*   - 1 - Algoritmo Genético: Busca minimizar a Rede;                     
*	- 2 - Força Bruta: Testa todas as possibilidades;                          
*	- 3 - RSA: Executa o RSA para a rede especifica;

 Ao fim da execução todas as opções salvam o arquivo de resultado contendo principais informações e o resultado da execução.

### Contato ###
 yruigneris@gmail.com
 
 “Nossas dúvidas são traidoras e nos fazem perder o que seria nosso pelo simples medo de tentar.” WILLIAM SHAKESPEARE.
 
 Se você tem alguma dúvida, pergunte!