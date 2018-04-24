/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa.teste;

import rsa.Demanda;

/**
 *
 * @author winderdias
 */
public class EventoSaidaDemanda extends Evento{

    private Demanda demanda;
    
    public EventoSaidaDemanda(Demanda demanda) {
        this.demanda = demanda;
    }
    
    public Demanda getDemanda() {
        return this.demanda;
    }
    
    public String toString() {
        return "Saida: "+Long.toString(demanda.getId());
    }
}
