package rsa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rsa.alocacaodeespectro.AlocadorDeEspectro;
import rsa.roteamento.Dijkstra;
import rsa.roteamento.ParCaminhoDisjuntos;
import rsa.teste.*;
import rsa.teste.GerenciadorDeEventos.*;
import java.util.Arrays;
import rsa.distribuicao.DesvioPadrao;

public class Principal {
        public static List <Double> listaProbabilidadesBloqueio1;
        public static List <Double> listaProbabilidadesBloqueio2;
        public static List <Double> listaProbabilidadesBloqueio4;
	
        public static void main(String[] args) {

            LeitorDeEntrada leitor;
            
            
            Estatisticas estatisticas;
            
            int nNos;
            
            
            if (args.length < 1) {
                System.out.println("Informe o arquivo contendo o tipo de topologia");
                return;
            }
            
            try {
                Evento evento;
                
                leitor = new LeitorDeEntrada();
                
                //carrega arquivo contendo os enlaces e suas conexoes
                Enlaces enlaces = leitor.getEnlacesByArqEntrada(args[0]);
                //armazena a quantidade de vertices do grafo
                nNos = enlaces.size();
                
                double pcBloqueio;
                
                int erlang;
                for(erlang = 100;erlang<=1000;erlang+=100){
                    listaProbabilidadesBloqueio1 = new ArrayList<>();
                    listaProbabilidadesBloqueio2 = new ArrayList<>();
                    listaProbabilidadesBloqueio4 = new ArrayList<>();
                    List <Double> listaProbabilidadesBloqueioTotal = new ArrayList<>();
                    List <Double> mediaFragmentacaoTotal = new ArrayList<>();
                    List <Double> listaMediaUtilizacaoTotal = new ArrayList<>();
                    List <Integer> listaMediaSaltosUtilizados = new ArrayList<>();
                    List <Integer> listaDemandasRealocadas = new ArrayList<>();
                    
                    System.out.println("***----------------------------------------------------------------***");
                    System.out.println("Trafego: "+erlang+" erlangs");
                    System.out.println("***----------------------------------------------------------------***");
                    for(int seed = 0; seed<10;seed++){
                        //prepara alocador de espectro com os enlaces lidos
                        AlocadorDeEspectro alocacao = new AlocadorDeEspectro(enlaces);

                        GerenciadorDeEventos eventos = new GerenciadorDeEventos();

                        //prepara gerador de demandas com os enlaces lidos
                        GeradorDeDemandas demandas = new GeradorDeDemandas(enlaces);

                        /*constroi matriz de requisicoes com demandas randomicas
                            passa como argumento a 'semente' que eh um inteiro de 0...99
                        */
                        List <Integer> ocorrenciasDemandasDiferentes = new ArrayList<>();
                        demandas.geraMatrizDeDemandas(eventos,(seed+1),erlang);

                        RSA rsa = new RSA(enlaces,alocacao,true);
                        int contaEventos = 0;


                        while((evento = eventos.popEvent()) != null){

                            //metodo para impressao dos caminhos gerados
                            //imprimeCaminhosGerados(demandas.getListaDemandas());

                            if (evento instanceof EventoChegadaDemanda){
                                Demanda demandaAtiva = ((EventoChegadaDemanda) evento).getDemanda();

                                //System.out.println("\n\n- Nova Demanda - "+"["+evento.getTempo()+"] ID: "+demandaAtiva.getId()+"instante de saida: "+((EventoChegadaDemanda) evento).getEventoSaida().getTempo());

                                rsa.chegadaDeDemanda(demandaAtiva);

                                //remove o evento de saida se caso a demanda entrar em espera
                                if(demandaAtiva.getStandBy()==1){
                                    eventos.addFilaEspera(evento);
                                    eventos.removeEvent((((EventoChegadaDemanda) evento).getEventoSaida()));
                                }
                                else
                                    ocorrenciasDemandasDiferentes.add(demandaAtiva.getnSlots());

                            }

                            else if (evento instanceof EventoSaidaDemanda){
                                Demanda demandaRemove = ((EventoSaidaDemanda) evento).getDemanda();
                                //System.out.println("EVento saida ID: "+demandaRemove.getId());
                                if (demandaRemove.getCaminhoPrincipalAlocado() != null && demandaRemove.getCaminhoBackupAlocado() != null){
                                    alocacao.removeDemanda(demandaRemove.getCaminhoPrincipalAlocado(),demandaRemove.getId());
                                    alocacao.removeDemanda(demandaRemove.getCaminhoBackupAlocado(), demandaRemove.getId());
                                    //System.out.println("Demanda id: "+demandaRemove.getId()+" Removida! Tempo: "+evento.getTempo());
                                    eventos.atualizaFilaEspera(evento);
                                }

                            }
                            if(contaEventos%100==0){
                                calculaMediaFragmentacaoRede(enlaces,alocacao,rsa.getEstatisticas());
                                calculaMediaEspectroUtilizacao(enlaces,alocacao,rsa.getEstatisticas());
                            }
                            contaEventos++;
                        }

                        //System.out.println("Tamanho da fila: "+eventos.getFilaEspera().size());

                        estatisticas = rsa.getEstatisticas();

                        imprimeBloqueiosPorDemanda(ocorrenciasDemandasDiferentes,estatisticas.getOcorrenciasBloqueiosPorDemandas());

                        //System.out.println("\nDemandas realocadas: "+estatisticas.getTotalDemandasRealocadas());

                        //System.out.println("\nTotal demandas: "+ estatisticas.getNumChegadas()+" Aceitas: "+estatisticas.getDemandasAceitas()+" Bloqueadas: "+estatisticas.getDemandasBloqueadas()+"\n");

                        pcBloqueio = (double) estatisticas.getDemandasBloqueadas() / (double) estatisticas.getNumChegadas();
                        
                        listaDemandasRealocadas.add(estatisticas.getTotalDemandasRealocadas());
                        listaProbabilidadesBloqueioTotal.add(pcBloqueio);
                        listaMediaSaltosUtilizados.add((estatisticas.getTotalSaltos()));

                        double mediafragtotal=0.0;
                        for(int p = 0; p<estatisticas.getMediaFragmentacaoRede().size();p++)
                            mediafragtotal += estatisticas.getMediaFragmentacaoRede().get(p);

                        mediaFragmentacaoTotal.add((mediafragtotal/estatisticas.getMediaFragmentacaoRede().size()));
                        //System.out.println("Media fragmentacao total: "+mediafragtotal/estatisticas.getMediaFragmentacaoRede().size());

                        double mediautilizacaototal=0.0;
                        for(int p = 0; p<estatisticas.getMediaUtilizacaoRede().size();p++)
                            mediautilizacaototal += estatisticas.getMediaUtilizacaoRede().get(p);

                        listaMediaUtilizacaoTotal.add((mediautilizacaototal/estatisticas.getMediaUtilizacaoRede().size()));
                        //System.out.println("Media utilizacao total: "+mediautilizacaototal/estatisticas.getMediaUtilizacaoRede().size());


                        //System.out.println("\nNumero total de Bloqueios: "+ estatisticas.getDemandasBloqueadas()+" Numero de saltos :"+estatisticas.getTotalSaltos()+" Numero de Demandas: "+estatisticas.getNumChegadas()+" Probabilidade de bloqueio : "+pcBloqueio+"\n");
                    }

                    DesvioPadrao dvFrag = new DesvioPadrao(mediaFragmentacaoTotal);
                    DesvioPadrao dvUtilizacao = new DesvioPadrao(listaMediaUtilizacaoTotal);
                    double mediaTotalSaltos = 0.0;
                    double desvioPadraoUtilizacao = 0.0;
                    double desvioPadraoFrag = 0.0;
                    double mediaUtilizacao = 0.0;
                    double mediaFrag = 0.0;
                    double mediaBloqueios = 0.0;
                    double mediaBloqueios1 = 0.0;
                    double mediaBloqueios2 = 0.0;
                    double mediaBloqueios4 = 0.0;
                    double mediaDemandasRealocadas = 0.0;
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    
                    for(int seed = 0;seed<10;seed++){
                        mediaDemandasRealocadas += listaDemandasRealocadas.get(seed);
                        mediaTotalSaltos += listaMediaSaltosUtilizados.get(seed);
                        mediaUtilizacao += listaMediaUtilizacaoTotal.get(seed);
                        mediaFrag += mediaFragmentacaoTotal.get(seed);
                         mediaBloqueios += listaProbabilidadesBloqueioTotal.get(seed);
                         mediaBloqueios1 += listaProbabilidadesBloqueio1.get(seed);
                         mediaBloqueios2 += listaProbabilidadesBloqueio2.get(seed);
                         mediaBloqueios4 += listaProbabilidadesBloqueio4.get(seed);
                         System.out.println("Execucao numero "+(seed+1)+" Trafego: "+erlang+" erlangs "+" BloqueiosTotal: "+listaProbabilidadesBloqueioTotal.get(seed)+" Bloqueios1: "+listaProbabilidadesBloqueio1.get(seed)+" Bloqueios2: "+listaProbabilidadesBloqueio2.get(seed)+" Bloqueios4: "+listaProbabilidadesBloqueio4.get(seed)+ " Fragmentation index: "+mediaFragmentacaoTotal.get(seed)+" Utilization ration: "+listaMediaUtilizacaoTotal.get(seed)+" Demandas realocadas: "+listaDemandasRealocadas.get(seed));
                    }
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("\nMedia bloqueios total: "+mediaBloqueios/10.0+" Media bloqueios1: "+mediaBloqueios1/10.0+" Media bloqueios2: "+mediaBloqueios2/10.0+" Media bloqueios4: "+mediaBloqueios4/10.0);
                    System.out.println("Enlaces utilizados: "+mediaTotalSaltos/10.0);
                    System.out.println("Media frag total: "+mediaFrag/10.0+" Desvio padrao frag: "+dvFrag.getDesvioPadrao());
                    System.out.println("Media utilizacao total: "+mediaUtilizacao/10.0+" Desvio padrao utilizacao: "+dvUtilizacao.getDesvioPadrao());
                    System.out.println("Media demandas realocadas: "+mediaDemandasRealocadas/10.0);
                    System.out.println("***-------------------------------[FIM "+erlang+" erlang]-------------------------------***");
                    
                } 
            } catch (Exception e) {
                    e.printStackTrace();
            }
            
	}

	private static void imprimeCaminhosGerados(List<Demanda> caminhos) {
		for (Demanda caminho : caminhos) {
			if (caminho.getCaminhos().isEmpty())
				continue;
			System.out.println("-----------------------------------------------------");
			System.out.print("Origem : " + caminho.getOrigem() + " Destino : " + caminho.getDestino());
			//para cada caminho existente em uma determinada demanda
                        for (List<Integer> c : caminho.getCaminhos()) {
                                int distancia = 0;
				System.out.print(" Principal:[");
				for (Integer i : c) {
					System.out.print(i + ",");
				}
			}

		}
	}
        
        public static void imprimeCustoTotalCaminho(List <Integer> caminho,Enlaces enlaces,AlocadorDeEspectro alocacao){
            int distancia = 0;
            double frag = 0.0;
            int slotsLivres = 0;
            
            System.out.println("*---------------- [DADOS SOBRE O CAMINHO "+caminho+" ] --------------*");
            for (int i = 0; i<caminho.size()-1;i++){
                distancia += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getCusto();
                frag += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getFragmentacao();
                slotsLivres += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getSlotsLivres();
            }
            
            System.out.println(" Caminho: "+caminho+" Distancia "+distancia+" Fragmentacao "+frag+" Slots Livres: "+slotsLivres);
            //System.out.println(" Caminho: "+caminho+" Distancia "+distancia+" Fragmentacao ");
            alocacao.imprimeSlots(caminho);
            System.out.println("*---------------- [FIM DADOS SOBRE O CAMINHO] --------------*");
        }
        

    private static void imprimeBloqueiosPorDemanda(List<Integer> ocorrenciasDemandasDiferentes,List<Integer> ocorrenciasBloqueiosPorDemandas) {
        
        int contaBlock1,contaBlock2,contaBlock4;
        contaBlock1=contaBlock2=contaBlock4=0;
        
        Set <Integer> tamanhoDemandasBloqueadas = new HashSet<Integer>(ocorrenciasBloqueiosPorDemandas);
        //System.out.println("\n");
        //Conta o numero de bloqueios para cada tipo de demanda (e.g de tamanho,1,2,3...etc)
        for (int tamanhoDemandaBloqueada : tamanhoDemandasBloqueadas){
            int contador = 0;

            for (int j=0;j<ocorrenciasBloqueiosPorDemandas.size();j++){
                if (tamanhoDemandaBloqueada == ocorrenciasBloqueiosPorDemandas.get(j))
                    contador ++;
            }
            if(tamanhoDemandaBloqueada == 1) contaBlock1 = contador;
            if(tamanhoDemandaBloqueada == 2) contaBlock2 = contador;
            if(tamanhoDemandaBloqueada == 4) contaBlock4 = contador;
            //System.out.print("Bloqueio de demandas de tamanho "+tamanhoDemandaBloqueada+" :"+contador+"  |\t ");
        }
        
        listaProbabilidadesBloqueio1.add((double)contaBlock1/50000);
        listaProbabilidadesBloqueio2.add((double)contaBlock2/50000);
        listaProbabilidadesBloqueio4.add(((double)contaBlock4/50000)); 
    }

    
    public static void calculaMediaFragmentacaoRede(Enlaces enlaces, AlocadorDeEspectro alocacao, Estatisticas estatisticas) {
        OrdenadorCaminhos oc = new OrdenadorCaminhos(enlaces,alocacao);
        List<Double> mediaFragmentacaoRede = estatisticas.getMediaFragmentacaoRede();
        double somafrag = 0.0;
        int qtdEnlaces = 0;
        for (int i = 0; i < enlaces.size(); i++)
                for (int j = i + 1; j < enlaces.size(); j++) {
                        Enlaces.Enlace e = enlaces.getEnlace(i, j);
                        if (e == null)
                                continue;
                        somafrag += oc.calculaFragmentacaoEnlace(i, j);
                        
               //         System.out.println(oc.calculaFragmentacaoEnlace(i, j));
             //           alocacao.imprimeSlotsEnlace(i, j);
                        qtdEnlaces++;
                }
        //System.out.println("somafrag: "+somafrag+" tamanho da rede: "+qtdEnlaces+" mediafrag "+(somafrag/qtdEnlaces));
        mediaFragmentacaoRede.add((somafrag/qtdEnlaces));
        estatisticas.setMediaFragmentacaoRede(mediaFragmentacaoRede);
    }

    public static void calculaMediaEspectroUtilizacao(Enlaces enlaces, AlocadorDeEspectro alocacao, Estatisticas estatisticas) {
        OrdenadorCaminhos oc = new OrdenadorCaminhos(enlaces,alocacao);
        List<Double> mediaUtilizacaoRede = estatisticas.getMediaUtilizacaoRede();
        double somaUtilizacao = 0.0;
        int qtdEnlaces = 0;
        for (int i = 0; i < enlaces.size(); i++)
                for (int j = i + 1; j < enlaces.size(); j++) {
                        Enlaces.Enlace e = enlaces.getEnlace(i, j);
                        if (e == null)
                                continue;
                        somaUtilizacao += oc.calculaUtilizacaoEnlace(i, j);
                        
                        //System.out.println(oc.calculaUtilizacaoEnlace(i, j));
                        //alocacao.imprimeSlotsEnlace(i, j);
                        qtdEnlaces++;
                }
        //System.out.println("somafrag: "+somaUtilizacao+" tamanho da rede: "+qtdEnlaces+" mediafrag "+(somaUtilizacao/qtdEnlaces));
        mediaUtilizacaoRede.add((somaUtilizacao/qtdEnlaces));
        estatisticas.setMediaUtilizacaoRede(mediaUtilizacaoRede);
    }
}
