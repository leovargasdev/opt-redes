package rsa;

import java.util.List;
import rsa.roteamento.ParCaminhoDisjuntos;

/**
 * Essa classe representa uma demanda de tráfego
 *
 * @author Winder Dias
 */
public class Demanda {

    private int origem, destino, nSlots;
    private long id;
    private List<List<Integer>> caminhos;
    private List<Integer> caminhoPrincipalAlocado;
    private List<Integer> caminhoBackupAlocado;
    private List<ParCaminhoDisjuntos> caminhosDisjuntos;
    //Esse atributo informa se a demanda foi colocada na fila
    private int standBy;

    /**
     *
     * @param origem Inteiro que identifica a origem da transmissao
     * @param destino Inteiro que identifica o destino da transmissao
     * @param nSlots Inteiro que recebe a quantidade de slots necessarios para
     * transmissao
     * @param caminhosDisjuntos Lista contendo NxM pares de caminhos disjuntos
     * possíveis para a demanda
     * @param caminhos Lista que possui k caminhos mais curtos entre origem e
     * destino
     * @param id Id da demanda
     */
    public Demanda(int origem, int destino, int nSlots, List<ParCaminhoDisjuntos> caminhosDisjuntos, List<List<Integer>> caminhos, int id) {
        super();
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.nSlots = nSlots;
        this.caminhos = caminhos;
        this.caminhoPrincipalAlocado = null;
        this.caminhoBackupAlocado = null;
        this.caminhosDisjuntos = caminhosDisjuntos;
        this.standBy = 0;
    }

    /**
     * @return a origem da demanda
     */
    public int getOrigem() {
        return origem;
    }

    /**
     * Seta o no de origem
     *
     * @param origem Novo no de origem
     */
    public void setOrigem(int origem) {
        this.origem = origem;
    }

    /**
     * @return o destino da demanda
     */
    public int getDestino() {
        return destino;
    }

    /**
     * @return o id da demanda
     */
    public long getId() {
        return id;
    }

    /**
     * Seta o no de destino
     *
     * @param destino Novo no de destino
     */
    public void setDestino(int destino) {
        this.destino = destino;
    }

    /**
     *
     * @return o numero de slots que a demanda utiliza
     */
    public int getnSlots() {
        return nSlots;
    }

    /**
     * Seta a quantidade de slots da demanda
     *
     * @param nSlots Nova quantidade de slots
     */
    public void setnSlots(int nSlots) {
        this.nSlots = nSlots;
    }

    /**
     *
     * @return NxM pares de caminhos disjuntos
     */
    public List<ParCaminhoDisjuntos> getCaminhosDisjuntos() {
        return caminhosDisjuntos;
    }

    /**
     * Seta a lista de NxM pares de caminhos disjuntos
     *
     * @param caminhosDisjuntos
     */
    public void setCaminhosDisjuntos(List<ParCaminhoDisjuntos> caminhosDisjuntos) {
        this.caminhosDisjuntos = caminhosDisjuntos;
    }

    /**
     *
     * @return a lista de k caminhos mínimos entre origem e destino
     */
    public List<List<Integer>> getCaminhos() {
        return caminhos;
    }

    /**
     * Seta a lista de k caminhos mínimos entre origem e destino
     *
     * @param caminhos
     */
    public void setCaminhos(List<List<Integer>> caminhos) {
        this.caminhos = caminhos;
    }

    /**
     *
     * @return O caminho alocado como principal no atendimento a demanda
     */
    public List<Integer> getCaminhoPrincipalAlocado() {
        return caminhoPrincipalAlocado;
    }

    /**
     * Seta O caminho alocado como principal no atendimento a demanda
     *
     * @param caminhoPrincipalAlocado
     */
    public void setCaminhoPrincipalAlocado(List<Integer> caminhoPrincipalAlocado) {
        this.caminhoPrincipalAlocado = caminhoPrincipalAlocado;
    }

    /**
     *
     * @return O caminho alocado como secundário no atendimento a demanda
     */
    public List<Integer> getCaminhoBackupAlocado() {
        return caminhoBackupAlocado;
    }

    /**
     * Seta o caminho secundário alocado para a demanda
     *
     * @param caminhoBackupAlocado
     */
    public void setCaminhoBackupAlocado(List<Integer> caminhoBackupAlocado) {
        this.caminhoBackupAlocado = caminhoBackupAlocado;
    }

    /**
     *
     * @return Um inteiro informando se a demanda esta em modo de espera
     */
    public int getStandBy() {
        return standBy;
    }

    /**
     * Seta o estado de espera da demanda
     *
     * @param standBy
     */
    public void setStandBy(int standBy) {
        this.standBy = standBy;
    }

}
