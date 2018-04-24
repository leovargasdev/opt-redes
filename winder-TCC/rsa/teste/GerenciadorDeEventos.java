/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa.teste;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import rsa.Demanda;

/**
 *
 * @author winderdias
 */
public class GerenciadorDeEventos {
    private PriorityQueue <Evento> filaEventos;
    private Map<Evento,Integer> filaEspera;
    
     public static class EventSort implements Comparator<Evento> {

    	/**
    	 * Compara dois eventos pelo tempo.
    	 * Retorna -1 se o primeiro parametro eh menor que o segundo, retorna 1 caso contrario
    	 * and zero if they are equal.
    	 */
        @Override
        public int compare(Evento o1, Evento o2) {
            if (o1.getTempo() < o2.getTempo()) {
                return -1;
            }
            if (o1.getTempo() > o2.getTempo()) {
                return 1;
            }
            
            return 0;
        }
        
    }
     
    /**
     * Cria uma nova fila de eventos com capacidade inicial para 100 eventos
     * e usa o comparador definido em EventSort para ordenar os tempos
     */
    public GerenciadorDeEventos() {
        EventSort eventSort = new EventSort();
        this.filaEventos = new PriorityQueue<Evento>(100, eventSort);
        this.filaEspera = new HashMap<Evento, Integer>();
    }
    
    /**
     * Adiciona um novo evento a fila de eventos.
     * 
     * @param evento sera adicionado a fila de eventos
     * @return boolean true um evento novo foi adicionado a fila;
     * 				   false se o evento ja esta na fila
     */
    public boolean addEvent(Evento evento) {
        return this.filaEventos.add(evento);
    }
    
    public boolean removeEvent(Evento evento) {
        return this.filaEventos.remove(evento);
    }
     /**
     * Retorna e remove o primeiro elemento da fila.
     * 
     * @return o primeiro evento da fila, ou null se nao tem eventos na fila
     */
    public Evento popEvent() {
        return this.filaEventos.poll();
    }
    
     /**
     * Retorna o numero de eventos da fila
     * Se existir mais que Integer.MAX_VALUE eventos,
     * retorna Integer.MAX_VALUE. 
     * 
     * @return numero de eventos na fila de eventos
     */
    public int numEvents() {
        return this.filaEventos.size();
    }
    
    
    public PriorityQueue<Evento> getFilaEventos() {
        return filaEventos;
    }

    public void setFilaEventos(PriorityQueue<Evento> filaEventos) {
        this.filaEventos = filaEventos;
    }
    
    public void adicionaListaEspera(Evento eventoChegada) {
        Demanda demandaAtiva = ((EventoChegadaDemanda) eventoChegada).getDemanda();
        double holdingTime=0.0;
        
        //faz uma copia de todos os eventos restantes
        PriorityQueue<Evento> eventos = this.filaEventos;
        int eventosRestantes = eventos.size();
        Evento [] copiaEventos = new Evento[eventosRestantes];
        eventos.toArray(copiaEventos);
        EventSort a = new EventSort();
        Arrays.sort(copiaEventos,a);
        
        //percorre os proximos eventos e procura pelo indice evento de saida da demanda a ser realocada
        Demanda demandaSaida;
        int i;
        
        for ( i = 0; i<copiaEventos.length; i++ ){  
            if(copiaEventos[i] instanceof EventoSaidaDemanda ){
                demandaSaida = ((EventoSaidaDemanda) copiaEventos[i]).getDemanda();
                if(demandaSaida.getId() == demandaAtiva.getId()){ 
                    holdingTime = copiaEventos[i].getTempo()-eventoChegada.getTempo();
                    //System.out.println ( "Evento chegada tempo: " + eventoChegada.getTempo()+" Evento Saida tempo: "+copiaEventos[i].getTempo()) ; 
                    this.filaEventos.remove(copiaEventos[i]);
                    break;
                    
                }
            }
            
            //System.out.println ( "Evento: " + copiaEventos[i].getTempo()) ; 
        }
        
        int contador = 0;
        for ( int j = 0; j<copiaEventos.length; j++ ){  
            if(copiaEventos[j] instanceof EventoSaidaDemanda ){
                contador ++;
                if(contador == 3){
                    eventoChegada.setTempo(copiaEventos[j].getTempo()+0.01);
                    
                    this.filaEventos.add(eventoChegada);
                    
                    Evento novoEventoSaida = new EventoSaidaDemanda(demandaAtiva);
                    novoEventoSaida.setTempo(eventoChegada.getTempo()+holdingTime);
                    this.filaEventos.add(novoEventoSaida);
                    break;
                }
            }
        }
        
        /*
        
        Evento novaTentativa = new EventoChegadaDemanda(demandaAtiva);
        novaTentativa.setTempo(eventoChegada.getTempo()+5);
        eventos.add(novaTentativa);
        
        int contador = 0;
        
        GerenciadorDeEventos copia = eventos;
        Evento eventoAux;
        
        while((eventoAux = copia.popEvent()) != null){
            if (eventoAux instanceof EventoSaidaDemanda){
                contador++;
                if(contador == 3){
                    Evento novaTentativa = new EventoChegadaDemanda(demandaAtiva);
                    novaTentativa.setTempo(eventoAux.getTempo()+0.1);
                    eventos.addEvent(novaTentativa);
                }
            }
        }*/
    }
    
    public void atualizaFilaEspera(Evento eventoAtual) {
        
        Iterator<Map.Entry<Evento, Integer>> it = this.filaEspera.entrySet().iterator();
        Demanda demandaAtual = ((EventoSaidaDemanda) eventoAtual).getDemanda();
        double instanteTempo = eventoAtual.getTempo();
        
        while (it.hasNext()){
          
          Map.Entry<Evento, Integer> par = it.next();
          Evento eventoChegadaNaFila = par.getKey();
          Integer contador = par.getValue();
          
          Demanda demandaNaFila = ((EventoChegadaDemanda)eventoChegadaNaFila).getDemanda();
          
        //  if(demandaAtual.getOrigem() == demandaNaFila.getOrigem() && demandaAtual.getDestino() == demandaNaFila.getDestino())
          contador+=1;
          
          this.filaEspera.put(eventoChegadaNaFila, contador);
          
          if(contador == 2){
              
            double holdingTime = ((((EventoChegadaDemanda) eventoChegadaNaFila).getEventoSaida().getTempo()) - eventoChegadaNaFila.getTempo());
            
            //System.out.println("Holding time : "+ holdingTime);
            //demandaNaFila.setStandBy(0);
            Evento novoEventoChegada = new EventoChegadaDemanda(demandaNaFila);
            novoEventoChegada.setTempo(instanteTempo+0.01);
                    
            Evento novoEventoSaida = new EventoSaidaDemanda(demandaNaFila);
            novoEventoSaida.setTempo(novoEventoChegada.getTempo()+holdingTime);
            
            ((EventoChegadaDemanda) novoEventoChegada).setEventoSaida((EventoSaidaDemanda) novoEventoSaida);
            this.filaEventos.add(novoEventoChegada);
            this.filaEventos.add(novoEventoSaida);
            //System.out.println("Demanda id: "+demandaNaFila.getId()+" Tempo de entrada: "+ novoEventoChegada.getTempo()+" Novo Evento saida "+novoEventoSaida.getTempo());
            it.remove();
          }
          
        }
    }
    
    public Map<Evento, Integer> getFilaEspera() {
        return filaEspera;
    }

    public void addFilaEspera(Evento eventoEspera) {
        this.filaEspera.put(eventoEspera, 0);
    }
    
}
