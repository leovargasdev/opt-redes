/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import rsa.alocacaodeespectro.AlocadorDeEspectro;
import rsa.roteamento.ParCaminhoDisjuntos;
import rsa.teste.Evento;
import rsa.teste.EventoChegadaDemanda;
import rsa.teste.EventoSaidaDemanda;
import rsa.teste.GerenciadorDeEventos;

/**
 *
 * @author winderdias
 */
public class RSA {
    private Estatisticas estatisticas;
    private Enlaces enlaces;
    private AlocadorDeEspectro alocador;
    private List <Demanda> demandasRecebidas;
    private boolean comFila;
    
    public RSA(Enlaces enlaces,AlocadorDeEspectro alocador, boolean comFila){
        this.enlaces = enlaces;
        this.estatisticas = new Estatisticas();
        this.alocador = alocador;
        this.demandasRecebidas = new ArrayList<>();
        this.comFila = comFila;
    }
    
    public boolean chegadaDeDemanda(Demanda demandaAtiva){
        
        boolean alocado;
        OrdenadorCaminhos ordenaCaminhos = new OrdenadorCaminhos(enlaces,alocador);
        int nSlots = demandaAtiva.getnSlots();
        
        if(!demandasRecebidas.contains(demandaAtiva)){
            demandasRecebidas.add(demandaAtiva);
            //System.out.println("\n\n- Nova Demanda - "+demandaAtiva.getOrigem()+" -> "+demandaAtiva.getDestino()+" : "+demandaAtiva.getnSlots());
            //Numero de  slots requisitados pela demanda
            estatisticas.setTotalSlots(estatisticas.getTotalSlots() + nSlots);
            //incrementa o numero de demandas que chegaram
            estatisticas.setNumChegadas(estatisticas.getNumChegadas()+1);
        
        }

        //recebe lista com os k shortests paths e seus respectivos backups
        List<ParCaminhoDisjuntos> caminhosDisjuntos = demandaAtiva.getCaminhosDisjuntos();
        List<ParCaminhoDisjuntos> caminhosDisjuntosMenosFrag = ordenaCaminhos.ordenaCaminhosMenosFragmentadoPrimeiro(caminhosDisjuntos);
        //List<ParCaminhoDisjuntos> caminhosDisjuntosOrdenadosMostSlotsOverHops = ordenaCaminhos.ordenaCaminhosMostSlotsOverHopsFreeFirst(caminhosDisjuntos);
        //List<ParCaminhoDisjuntos> caminhosDisjuntosMostSlots = ordenaCaminhos.ordenaCaminhosMostSlotsFreeFirst(caminhosDisjuntos);
        //List<ParCaminhoDisjuntos> caminhosDisjuntosOrdenadosMenosEnlaces = ordenaCaminhos.ordenaCaminhosMenosEnlacesPrimeiro(caminhosDisjuntos);
        //List<ParCaminhoDisjuntos> caminhosDisjuntosMenosFragMedia = ordenaCaminhos.ordenaCaminhosMenosFragmentadoMediaPrimeiro(caminhosDisjuntos);
        
        alocado = alocaEspectroCaminhosDisjuntos(caminhosDisjuntosMenosFrag,nSlots,demandaAtiva);
        
        if(alocado){
            
            if(demandaAtiva.getStandBy() == 1){
                //System.out.println("Demanda id: "+demandaAtiva.getId()+" Realocada com sucesso!");
                estatisticas.setTotalDemandasRealocadas(estatisticas.getTotalDemandasRealocadas()+1);
                demandaAtiva.setStandBy(2);
            }
            //incrementa o numero de saltos de demandas aceitas nas estatisticas
            estatisticas.setTotalSaltosPrimario(estatisticas.getTotalSaltosPrimario()+(demandaAtiva.getCaminhoPrincipalAlocado().size()-1));
            estatisticas.setTotalSaltosSecundario(estatisticas.getTotalSaltosSecundario()+(demandaAtiva.getCaminhoBackupAlocado().size()-1));
            
            //incrementa a quantidade de demandas aceitas
            estatisticas.setDemandasAceitas((estatisticas.getDemandasAceitas()+1));
            //alocador.imprimeSlots(demandaAtiva.getCaminhoPrincipalAlocado());
            //alocador.imprimeSlots(demandaAtiva.getCaminhoBackupAlocado());
            return true;
        }
        else{ 
            if(demandaAtiva.getStandBy() == 0 && this.comFila == true){
                demandaAtiva.setStandBy(1);
                //System.out.println("Demanda adicionada a lista de espera : "+demandaAtiva.getId());       
                return false;
            }
            
            demandaAtiva.setStandBy(2);
            //incrementa em 1 o numero de bloqueios
            estatisticas.setDemandasBloqueadas(estatisticas.getDemandasBloqueadas()+1);
            
            //incrementa em 1 a ocorrencia de uma demanda de tamanho x
            List <Integer> ocorrencias = estatisticas.getOcorrenciasBloqueiosPorDemandas();
            ocorrencias.add(nSlots);
            estatisticas.setOcorrenciasBloqueiosPorDemandas(ocorrencias);
            //System.out.println("Bloqueio da demanda "+ demandaAtiva.getId() + " Numero de slots: "+nSlots);
            return false;
        }
    }
        
    void saidaDeDemanda(Demanda demandaRemove) {
        if (demandaRemove.getCaminhoPrincipalAlocado() != null && demandaRemove.getCaminhoBackupAlocado() != null){
            alocador.removeDemanda(demandaRemove.getCaminhoPrincipalAlocado(),demandaRemove.getId());
            alocador.removeDemanda(demandaRemove.getCaminhoBackupAlocado(), demandaRemove.getId());
            System.out.println("Demanda id: "+demandaRemove.getId()+" Removida");
            
            estatisticas.setNumSaidas((estatisticas.getNumSaidas()+1));
        }
    }
    
    private boolean alocaEspectroCaminhosDisjuntos(List<ParCaminhoDisjuntos> caminhosDisjuntos, int nSlots, Demanda demandaAtiva) {
        
        List<Integer> caminhoPrincipal = null, caminhoBackup = null;
        //System.out.println("listagem dos caminhos"+caminhosDisjuntos.size()+"\n");
        
        for (ParCaminhoDisjuntos par : caminhosDisjuntos){
             
            caminhoPrincipal = par.getCaminhoPrincipal();
            caminhoBackup = par.getCaminhoSecundario();
            
            if (alocador.alocaCaminhoFirstFitB(caminhoPrincipal, nSlots, demandaAtiva.getId())) { 
                if (alocador.alocaCaminhoFirstFitB(caminhoBackup, nSlots, demandaAtiva.getId())) {
                    demandaAtiva.setCaminhoPrincipalAlocado(caminhoPrincipal);
                    demandaAtiva.setCaminhoBackupAlocado(caminhoBackup);
                    //System.out.println("CaminhoPrincipal disjunto: "+caminhoPrincipal+ " Custo: "+getCustoTotalCaminho(caminhoPrincipal) + "\t CaminhoPrincipal secundario: "+caminhoBackup+" Custo: "+getCustoTotalCaminho(caminhoBackup)+ " Total : "+(getCustoTotalCaminho(caminhoBackup)+getCustoTotalCaminho(caminhoPrincipal)));
                    return true;
                }
                else{
                    
                    alocador.removeDemanda(caminhoPrincipal,demandaAtiva.getId());
                }
            }
        }
        
        return false;
    }
    
    private double getCustoTotalCaminho(List <Integer> caminho){
        
        int distancia = 0;
        double frag = 0.0;
        
        for (int i = 0; i<caminho.size()-1;i++){
            distancia += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getCusto();
            //frag += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getFragmentacao();
        }
        
        return distancia;
    }
    private static void imprimeCustoTotalCaminho(List <Integer> caminho,Enlaces enlaces,AlocadorDeEspectro alocacao){
        int distancia = 0;
        double frag = 0.0;
        System.out.println(" ");
        for (int i = 0; i<caminho.size()-1;i++){
            distancia += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getCusto();
            //frag += enlaces.getEnlace(caminho.get(i), caminho.get(i+1)).getFragmentacao();
        }

        //System.out.println(" Caminho: "+caminho+" Distancia "+distancia+" Fragmentacao "+frag);
        System.out.println(" Caminho: "+caminho+" Distancia "+distancia+" Fragmentacao ");
        System.out.println(" ");
        alocacao.imprimeSlots(caminho);
    }
    
    public Estatisticas getEstatisticas() {
        return estatisticas;
    }

    public List<Demanda> getDemandasRecebidas() {
        return demandasRecebidas;
    }

    public void setDemandasRecebidas(List<Demanda> demandasRecebidas) {
        this.demandasRecebidas = demandasRecebidas;
    }
    
}
