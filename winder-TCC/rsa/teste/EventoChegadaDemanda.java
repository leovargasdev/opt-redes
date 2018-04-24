
package rsa.teste;

import rsa.Demanda;

/**
 *
 * @author winderdias
 */
public class EventoChegadaDemanda extends Evento {
	
    private Demanda demanda;
    private EventoSaidaDemanda eventoSaida;
    
    public EventoChegadaDemanda(Demanda demanda) {
        this.demanda = demanda;
    }
    
    public Demanda getDemanda() {
        return this.demanda;
    }
    
    public String toString() {
        return "Chegada: "+demanda.toString();
    }

    public EventoSaidaDemanda getEventoSaida() {
        return eventoSaida;
    }

    public void setEventoSaida(EventoSaidaDemanda eventoSaida) {
        this.eventoSaida = eventoSaida;
    }
    
}
