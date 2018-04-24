package rsa;

import java.util.ArrayList;
import java.util.List;

import rsa.Demanda;
import rsa.Enlaces;
import rsa.teste.Evento;
import rsa.teste.EventoChegadaDemanda;
import rsa.teste.EventoSaidaDemanda;
import rsa.distribuicao.DistribuicaoRandomica;
import rsa.roteamento.BuscaCaminhosDisjuntos;
import rsa.roteamento.Dijkstra;
import rsa.roteamento.ParCaminhoDisjuntos;
import rsa.teste.GerenciadorDeEventos;

/**
 * - Classe responsável por gerar uma quantidade específica de demandas 
 * Todas as demandas sao geradas antes mesmo de executar a simulacao, assim
 * uma lista de eventos é gerada para depois ser processada pelo controlador 
 * - Demandas são geradas em um período de tempo randômico e permanecem na rede
 * durante x unidades de tempo, definidas por uma distribuição exponencial de
 * média m
 *
 * @author Winder Dias
 */
public class GeradorDeDemandas {

    private Enlaces enlaces;
    private int[][] matrizDemanda;
    List<Demanda> listaDemandas;

    /**
     * Recebe os enlaces que representam a rede
     * @param enlaces Lista de enlaces
     */
    public GeradorDeDemandas(Enlaces enlaces) {
        this.enlaces = enlaces;
    }

    /**
     *
     * @param eventos Variavel armazena todos os eventos que vao ocorrer na rede
     * @param seed Semente para geração de demandas diferentes
     * @param erlang Intensidade do tráfego
     * @throws Exception
     */
    public void geraMatrizDeDemandas(GerenciadorDeEventos eventos, int seed, int erlang) throws Exception {

        DistribuicaoRandomica dist1, dist2, dist3, dist4;
        Dijkstra dj = new Dijkstra(enlaces);
        BuscaCaminhosDisjuntos bcd = new BuscaCaminhosDisjuntos(enlaces);
        List<ParCaminhoDisjuntos> caminhosDisjuntos;

        erlang = erlang / 10;

        this.listaDemandas = new ArrayList<Demanda>();

        int nNos, fonte, dest, id;
        double tempo = 0.0;

        nNos = enlaces.size();

        int[][] mtr = new int[nNos][nNos];

        dist1 = new DistribuicaoRandomica(1, seed);
        dist2 = new DistribuicaoRandomica(2, seed);
        dist3 = new DistribuicaoRandomica(3, seed);
        dist4 = new DistribuicaoRandomica(4, seed);

        Evento eventoChegada;
        Evento eventoSaida;

        for (int i = 0; i < nNos; i++) {
            java.util.Arrays.fill(mtr[i], 0);
        }

        for (int i = 0; i < 50000; i++) {

            dest = dist1.nextInt(nNos);
            fonte = dist1.nextInt(nNos);

            while (fonte == dest) {
                dest = dist1.nextInt(nNos);
            }

            //int [] tipoDemanda = new int[]{1,2,3,4,5,6,7,8,9,10};
            //int [] tipoDemanda = new int[]{1,2,4,7,8,13,16,32,64,80};
            int[] tipoDemanda = new int[]{1, 2, 4};
            int quantidadeSlots = tipoDemanda[dist2.nextInt(3)];
            id = i;

            //3 caminhos principais com um caminho de backup para cada
            caminhosDisjuntos = bcd.getNxMParDeCaminhosDisjuntos(fonte, dest, 3, 3);

            Demanda demanda = new Demanda(fonte, dest, quantidadeSlots, caminhosDisjuntos, dj.getKCaminhoMinimo(fonte, dest, 3), id);

            eventoChegada = new EventoChegadaDemanda(demanda);
            //distribuiçao exponencial
            tempo += dist3.nextExponential(0.1);
            eventoChegada.setTempo(tempo);

            eventoSaida = new EventoSaidaDemanda(demanda);
            //holding time gerado a partir de distribuicao exp
            double holdingTime = dist4.nextExponential(erlang);
            eventoSaida.setTempo(tempo + holdingTime);

            listaDemandas.add(demanda);
            System.out.println(((tempo + holdingTime) - tempo));

            //System.out.println("demandaID: "+demanda.getId()+" holdingTime :"+((tempo+holdingTime)-tempo));
            ((EventoChegadaDemanda) eventoChegada).setEventoSaida((EventoSaidaDemanda) eventoSaida);

            eventos.addEvent(eventoChegada);
            eventos.addEvent(eventoSaida);
        }
    }
    
    /**
     * Método retorna as demandas geradas
     * @return Uma lista contendo todas as demandas geradas
     */
    public List<Demanda> getListaDemandas() {
        return listaDemandas;
    }

}
