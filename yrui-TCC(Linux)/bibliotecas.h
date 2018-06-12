#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <math.h>
#include <time.h>
// #include <curses.h>
#include <limits.h>
#include <fstream>
#include <sstream>

template <typename T>
std::string to_string(T value){
	std::ostringstream os ;
	os << value ;
	return os.str() ;
}


using namespace std;

int quantidadeVertices = 0;
int *matrizF;
int numeroLinks = 0;
double distanciaMaxima;
double porcentagemMaiorDistancia; // Parametro que permite quantos % do individuo podem ser maior que a distancia maxima
int numeroNosExcedeDistanciaMaxima = 0;

double totalBloqueios;
int totalBloqueiosPrincipal;
int totalBloqueisSecundarios;

double *matrizD;
#define tamanhoBloco 500
#define tamanhoString 500

clock_t t0, tf;
double tempo_gasto;

// char *caminhoVertices = "coxUsa_nodes.txt";
// char *caminhoArestas = "coxUsa_links.txt";

char *caminhoVertices = "rnpBrazil_nodes.txt";
char *caminhoArestas = "rnpBrazil_links.txt";

char *caminhoSaida = "";

char melhorCaminhoRSA[tamanhoString] = "0,1,2,3,4,5,6,7,9,8,10,11";
char tipoRede[10] = "Original";
