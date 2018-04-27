/*
	Author: Yru� Giovan Neris - yruigneris@gmail.com - 2017
	Universidade Federal da Fronteira Sul - Chapec� - SC
	Trabalho de Conclus�o de Curso:
		- Ci�ncia da Computa��o
		- Problema Quadr�tico de Aloca��o aplicado ao Mapeamento de Topologias de Redes �pticas de Transporte de Telecomunica��es
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <math.h>
#include <time.h>
#include <curses.h>
#include <limits.h>
#include <fstream>

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



//Caminho que contem as arestas, referente ao caminho dos v�rtices

//ARNES
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Arnes\\arnes_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Arnes\\arnes_17_20.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Arnes\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Arnes\\arnes_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Arnes\\arnes_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Arnes\\";


//ARPNET
//char *caminhoVertices = "N�s - Lat Long\\Originais\\ArpNet\\arpanet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\ArpNet\\arpanet_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\ArpNet\\";

//char *caminhoVertices = "N�s - Lat Long\\Harary\\ArpNet\\arpanet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\ArpNet\\arpanet_20_32.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\ArpNet\\";


//AUSTRIA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Austria\\austria_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Austria\\austria_15_22.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Austria\\";
//
//char *caminhoVertices = "N�s - Lat Long\\Originais\\Austria\\austria_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Austria\\austria_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Austria\\";



//CANARIE
char *caminhoVertices = "pionier_nodes.txt";
char *caminhoArestas = "pionier_links.txt";
char *caminhoSaida = "\\Header";
////
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Canarie\\canarie_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Canarie\\canarie_19_26.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Canarie\\";


//CESNET
//char *caminhoVertices = "N�s - Lat Long\\Originais\\Cesnet\\cesnet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Cesnet\\cesnet_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Cesnet\\";

//char *caminhoVertices = "N�s - Lat Long\\Harary\\Cesnet\\cesnet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Cesnet\\cesnet_12_19.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Cesnet\\";

char melhorCaminhoRSA[tamanhoString] = "0,1,2,3,4,5,6,7,9,8,10,11";
char tipoRede[10] = "Original";

//char melhorCaminhoRSA[tamanhoString] = "0,2,3,5,6,9,10,1,11,4,7,8";
//char tipoRede[10] = "Harary";

//char melhorCaminhoRSA[tamanhoString] ="0,1,2,3,4,5,6,7,9,8,10,11";
//char tipoRede[10] = "Melhorada";


//CoxUSA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\CoxUSA\\coxUsa_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\CoxUSA\\cox_24_40.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\CoxUSA\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\CoxUSA\\coxUsa_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\CoxUSA\\coxUsa_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\CoxUSA\\";


//EON
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Eon\\eon_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Eon\\eon_19_37.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Eon\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Eon\\eon_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Eon\\eon_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Eon\\";


//GEANT
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Geant\\geant2_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Geant\\geant2_32_51.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Geant\\";
//
//char *caminhoVertices = "n�s - lat long\\Originais\\Geant\\geant2_nodes.txt";
//char *caminhoArestas = "n�s - lat long\\Originais\\Geant\\geant2_links.txt";
//char *caminhoSaida = "n�s - lat long\\Originais\\Geant\\";
//

//GERMANI
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Germani\\germany_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Germani\\germany_17_26.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Germani\\";
//
//char *caminhoVertices = "N�s - Lat Long\\Originais\\Germani\\germany_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Germani\\germany_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Germani\\";


//INTERNET2 USA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Internet2 USA\\internet2Usa_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Internet2 USA\\internet2_56_61.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Internet2 USA\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Internet2Usa\\internet2Usa_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Internet2Usa\\internet2Usa_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Internet2Usa\\";


//ITALY
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Italy\\italy_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Italy\\italy_14_29.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Italy\\";
//
//char *caminhoVertices = "N�s - Lat Long\\Originais\\Italy\\italy_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Italy\\italy_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Italy\\";

//char melhorCaminhoRSA[tamanhoString] = "0,1,2,3,4,5,6,7,8,9,10,11,12,13";
//char tipoRede[10] = "Original";

//char melhorCaminhoRSA[tamanhoString] = "4,13,12,11,10,9,8,5,7,6,0,1,2,3";
//char tipoRede[10] = "Harary";

//char melhorCaminhoRSA[tamanhoString] = "0,1,2,3,4,5,6,7,8,9,13,12,11,10";
//char tipoRede[10] = "Melhorada";


//LAMBDARAILUSA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\LAMBDARAILUSA\\lambdaRailUsa_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\LAMBDARAILUSA\\lambdaRailUsa_19_23.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\LAMBDARAILUSA\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\LAMBDARAILUSA\\lambdaRailUsa_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\LAMBDARAILUSA\\lambdaRailUsa_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\LAMBDARAILUSA\\";


//MEMOREX
//char *caminhoVertices = "N�s - Lat Long\\Harary\\MemorexEurope\\memorexEurope_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\MemorexEurope\\memorex_19_24.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\MemorexEurope\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\MemorexEurope\\memorexEurope_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\MemorexEurope\\memorexEurope_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\MemorexEurope\\";


//METRONA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Metrona\\metrona_UK_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Metrona\\metrona_33_41.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Metrona\\";
////
//char *caminhoVertices = "N�s - Lat Long\\Originais\\Metrona\\metrona_UK_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Metrona\\metrona_UK_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Metrona\\";
//

//MZIMA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Mzima\\mzima_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Mzima\\mzima_15_19.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Mzima\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Mzima\\mzima_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Mzima\\mzima_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Mzima\\";


//NEWNET
//char *caminhoVertices = "N�s - Lat Long\\Harary\\NewNet\\newnet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\NewNet\\newnet_26_31.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\NewNet\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\NewNet\\newnet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\NewNet\\newnet_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\NewNet\\";


//NSFNET
//char *caminhoVertices = "N�s - Lat Long\\Harary\\NsfNet\\nsfnet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\NsfNet\\nsfnet_14_21.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\NsfNet\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\NsfNet\\nsfnet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\NsfNet\\nsfnet_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\NsfNet\\";

//char melhorCaminhoRSA[tamanhoString] = "0,1,2,3,4,5,6,7,8,9,10,11,12,13";
//char tipoRede[10] = "Original";

//char melhorCaminhoRSA[tamanhoString] = "5,2,3,4,7,8,12,9,0,1,6,10,11,13";
//char tipoRede[10] = "Harary";

//char melhorCaminhoRSA[tamanhoString] = "2,0,1,5,6,7,4,3,8,10,9,12,13,11";
//char tipoRede[10] = "Melhorada";

//ONMINICONEUROPE
//char *caminhoVertices = "N�s - Lat Long\\Harary\\OnminiconEurope\\OmnicomEurope_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\OnminiconEurope\\omnicom_38_54.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\OnminiconEurope\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\OnminiconEurope\\OmnicomEurope_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\OnminiconEurope\\OmnicomEurope_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\OnminiconEurope\\";


//PIONIER
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Pionier\\pionier_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Pionier\\pionier_21_25.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Pionier\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Pionier\\pionier_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Pionier\\pionier_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Pionier\\";


//PORTUGAL
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Portugal\\portugal_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Portugal\\portugal_26_36.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Portugal\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Portugal\\portugal_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Portugal\\portugal_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Portugal\\";


//RENATER
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Renater\\renater_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Renater\\renater_27_35.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Renater\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Renater\\renater_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Renater\\renater_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Renater\\";


//RNP
//char *caminhoVertices = "N�s - Lat Long\\Harary\\RnpBrasil\\rnpBrazil_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\RnpBrasil\\rnp_10_12.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\RnpBrasil\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\RnpBrasil\\rnpBrazil_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\RnpBrasil\\rnpBrazil_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\RnpBrasil\\";

//char melhorCaminhoRSA[tamanhoString] = "0,1,2,3,4,5,6,7,8,9";
//char tipoRede[10] = "Original";

//char melhorCaminhoRSA[tamanhoString] = "3,7,9,8,4,6,2,5,0,1";
//char tipoRede[10] = "Harary";

//char melhorCaminhoRSA[tamanhoString] = "1,0,2,7,9,5,6,3,8,4";
//char tipoRede[10] = "Melhorada";


//SANET
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Sanet\\sanet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Sanet\\sanet_25_28.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Sanet\\";
//
//char *caminhoVertices = "N�s - Lat Long\\Originais\\Sanet\\sanet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Sanet\\sanet_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Sanet\\";


//SPAIN
//char *caminhoVertices = "N�s - Lat Long\\Harary\\Spain\\spain_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\Spain\\redirisnet_17_28.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\Spain\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\Spain\\spain_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\Spain\\spain_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\Spain\\";

//USA
//char *caminhoVertices = "N�s - Lat Long\\Originais\\UsaGde\\usaGde_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\UsaGde\\usaGde_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\UsaGde\\";

//char *caminhoVertices = "N�s - Lat Long\\Harary\\UsaGde\\usaGde_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\UsaGde\\H_usa100.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\UsaGde\\";


//VBNS
//char *caminhoVertices = "N�s - Lat Long\\Harary\\VBns\\vbns_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\VBns\\vbns_12_17.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\VBns\\";

//char *caminhoVertices = "N�s - Lat Long\\Originais\\VBns\\vbns_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\VBns\\vbns_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\VBns\\";


//VIA
//char *caminhoVertices = "N�s - Lat Long\\Harary\\ViaDataCenterNet\\viaDatacenterNet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Harary\\ViaDataCenterNet\\viaDatacenterNet_9_12.txt";
//char *caminhoSaida = "N�s - Lat Long\\Harary\\ViaDataCenterNet\\";

//
//char *caminhoVertices = "N�s - Lat Long\\Originais\\ViaDataCenterNet\\viaDatacenterNet_nodes.txt";
//char *caminhoArestas = "N�s - Lat Long\\Originais\\ViaDataCenterNet\\viaDatacenterNet_links.txt";
//char *caminhoSaida = "N�s - Lat Long\\Originais\\ViaDataCenterNet\\";
