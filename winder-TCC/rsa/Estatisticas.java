/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa;

import java.util.ArrayList;
import java.util.List;
import rsa.teste.Evento;

/**
 * Essa classe é responsável por armazenar os resultados das simulações 
 * @author winderdias
 */
public class Estatisticas {
    private int numChegadas;
    private int numSaidas;
    private int demandasAceitas;
    private int demandasBloqueadas;
    private int totalSlots;
    private int totalSlotsAceitos;
    private int totalSlotsBloqueados;
    private int totalSaltosPrimario;
    private int totalSaltosSecundario;
    private int totalDemandasRealocadas;
    private List <Double> mediaFragmentacaoRede;
    private List <Double> mediaUtilizacaoRede;
    private List <Integer> ocorrenciasBloqueiosPorDemandas;

    /**
     *
     */
    public Estatisticas(){
        numChegadas = 0;
        numSaidas = 0;
        totalSlots = 0;
        demandasAceitas = 0;
        demandasBloqueadas = 0;
        totalSaltosPrimario = 0;
        totalSaltosSecundario = 0;
        totalSlotsAceitos = 0;
        totalSlotsBloqueados = 0;
        totalDemandasRealocadas = 0;
        ocorrenciasBloqueiosPorDemandas = new ArrayList<>();
        mediaFragmentacaoRede = new ArrayList<>();
        mediaUtilizacaoRede = new ArrayList<>();
    }
    
    /**
     *
     * @return O número total de slots utilizados em cada execução
     */
    public int getTotalSlots() {
        return totalSlots;
    }

    /**
     * Seta o número total de slots utilizados
     * @param totalSlots
     */
    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }
    
    /**
     *
     * @return O número de demandas que chegaram
     */
    public int getNumChegadas() {
        return numChegadas;
    }

    /**
     * Seta o número de demandas que chegaram
     * @param numChegadas
     */
    public void setNumChegadas(int numChegadas) {
        this.numChegadas = numChegadas;
    }

    /**
     *
     * @return Retorna o número de demandas terminadas
     */
    public int getNumSaidas() {
        return numSaidas;
    }

    /**
     * Seta o número de demandas terminadas
     * @param numSaidas
     */
    public void setNumSaidas(int numSaidas) {
        this.numSaidas = numSaidas;
    }

    /**
     * 
     * @return o número total de saltos utilizados
     */
    public int getTotalSaltos() {
        return totalSaltosPrimario + totalSaltosSecundario;
    }

    /**
     * 
     * @return o número total de demandas aceitas
     */
    public int getDemandasAceitas() {
        return demandasAceitas;
    }

    /**
     * Seta o número total de demandas aceitas
     * @param demandasAceitas
     */
    public void setDemandasAceitas(int demandasAceitas) {
        this.demandasAceitas = demandasAceitas;
    }

    /**
     *
     * @return O número de demandas bloqueadas
     */
    public int getDemandasBloqueadas() {
        return demandasBloqueadas;
    }

    /**
     * Seta o número de demandas bloqueadas
     * @param demandasBloqueadas
     */
    public void setDemandasBloqueadas(int demandasBloqueadas) {
        this.demandasBloqueadas = demandasBloqueadas;
    }

    /**
     *
     * @return O número total de slots que foram alocados
     */
    public int getTotalSlotsAceitos() {
        return totalSlotsAceitos;
    }

    /**
     * Seta o número total de slots que foram alocados
     * @param totalSlotsAceitos
     */
    public void setTotalSlotsAceitos(int totalSlotsAceitos) {
        this.totalSlotsAceitos = totalSlotsAceitos;
    }

    /**
     *
     * @return O número total de slots que foram bloqueados
     */
    public int getTotalSlotsBloqueados() {
        return totalSlotsBloqueados;
    }

    /**
     * Seta o número total de slots que foram bloqueados
     * @param totalSlotsBloqueados
     */
    public void setTotalSlotsBloqueados(int totalSlotsBloqueados) {
        this.totalSlotsBloqueados = totalSlotsBloqueados;
    }

    /**
     * Esse método vai retornar uma lista com os tamanhos das demandas bloquedas,
     * por exemplo: Na lista a seguir [1,4,4,4,8,1,1] temos 3 bloqueios de demandas
     * de tamanho 1, 3 bloqueios de demandas de tamanho 4 e 1 bloqueio de demandas
     * de tamanho 8
     * @return Uma lista contendo todos os tamanhos das demandas que foram bloqueadas
     */
    public List<Integer> getOcorrenciasBloqueiosPorDemandas() {
        return ocorrenciasBloqueiosPorDemandas;
    }

    /**
     * Seta a lista contendo todos os tamanhos das demandas que foram bloqueadas
     * @param ocorrenciaDemandas
     */
    public void setOcorrenciasBloqueiosPorDemandas(List<Integer> ocorrenciaDemandas) {
        this.ocorrenciasBloqueiosPorDemandas = ocorrenciaDemandas;
    }

    /**
     * 
     * @return A quantidade de saltos utilizados no caminho primario
     */
    public int getTotalSaltosPrimario() {
        return totalSaltosPrimario;
    }

    /**
     * Seta a quantidade de saltos utilizados no caminho primario
     * @param totalSaltosPrimario
     */
    public void setTotalSaltosPrimario(int totalSaltosPrimario) {
        this.totalSaltosPrimario = totalSaltosPrimario;
    }

    /**
     *
     * @return A quantidade de saltos utilizados para o caminho secundario
     */
    public int getTotalSaltosSecundario() {
        return totalSaltosSecundario;
    }

    /**
     * Seta a quantidade de saltos utilizados para o caminho secundario
     * @param totalSaltosSecundario
     */
    public void setTotalSaltosSecundario(int totalSaltosSecundario) {
        this.totalSaltosSecundario = totalSaltosSecundario;
    }

    /**
     *
     * @return A quantidade de demandas que foram realocadas em segunda tentativa
     */
    public int getTotalDemandasRealocadas() {
        return totalDemandasRealocadas;
    }

    /**
     * Seta a quantidade de demandas que foram realocadas em segunda tentativa
     * @param totalDemandasRealocadas
     */
    public void setTotalDemandasRealocadas(int totalDemandasRealocadas) {
        this.totalDemandasRealocadas = totalDemandasRealocadas;
    }

    /**
     *
     * @return A quantidade média de fragmentação obtida para todas as simulações
     */
    public List<Double> getMediaFragmentacaoRede() {
        return mediaFragmentacaoRede;
    }

    /**
     * Seta a quantidade média de fragmentação obtida para todas as simulações
     * @param mediaFragmentacaoRede
     */
    public void setMediaFragmentacaoRede(List<Double> mediaFragmentacaoRede) {
        this.mediaFragmentacaoRede = mediaFragmentacaoRede;
    }

    public List<Double> getMediaUtilizacaoRede() {
        return mediaUtilizacaoRede;
    }

    public void setMediaUtilizacaoRede(List<Double> mediaUtilizacaoRede) {
        this.mediaUtilizacaoRede = mediaUtilizacaoRede;
    }
    
}
