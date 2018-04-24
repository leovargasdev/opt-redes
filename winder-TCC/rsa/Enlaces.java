package rsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Essa classe faz a modelagem da topologia física em um conjunto de enlaces
 *
 * @author Winder Dias
 */
public class Enlaces implements Cloneable {

    /**
     *
     * @return Uma cópia do grafo
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        Enlaces novo = new Enlaces();
        for (int i = 0; i < adjacencias.size(); i++) {
            HashMap<Integer, Enlace> hm = adjacencias.get(i);
            for (Integer j : hm.keySet()) {
                Enlace e = hm.get(j);
                novo.addEnlace(i, j.intValue(), e.custo, e.nSlots, e.granularidade);
            }
        }
        return novo;
    }

    /**
     * Essa classe modela um enlace da rede
     */
    public static class Enlace {

        private int custo, nSlots;
        private float granularidade;
        private Double fragmentacao;
        private int slotsLivres;
        private double frag;

        /**
         *
         * @param custo Distância de um enlace
         * @param nSlots Quantidade de slots pertencentes a um enlace
         * @param granularidade Espaçamento de cada slots do enlace
         */
        public Enlace(int custo, int nSlots, float granularidade) {
            this.custo = custo;
            this.nSlots = nSlots;
            this.fragmentacao = 0.0;
            this.slotsLivres = 320;
            this.frag = 0.0;
        }

        /**
         *
         * @return A quantidade de slots livres no enlace
         */
        public int getSlotsLivres() {
            return slotsLivres;
        }

        /**
         * Seta a quantidade de slots livres no enlace
         * @param slotsLivres
         */
        public void setSlotsLivres(int slotsLivres) {
            this.slotsLivres = slotsLivres;
        }

        /**
         *
         * @return A distância de cada enlace
         */
        public int getCusto() {
            return custo;
        }

        /**
         * Seta a distância/custo do enlace
         * @param custo
         */
        public void setCusto(int custo) {
            this.custo = custo;
        }

        /**
         *
         * @return A quantidade total de slots do enlace
         */
        public int getnSlots() {
            return nSlots;
        }

        /**
         * Seta a quantidade total de slots presentes em um enlace
         * @param nSlots
         */
        public void setnSlots(int nSlots) {
            this.nSlots = nSlots;
        }

        /**
         *
         * @return O espaçamento de cada slot do enlace
         */
        public float getGranularidade() {
            return granularidade;
        }

        /**
         * Seta o espaçamento de um slot do enlace
         * @param granularidade
         */
        public void setGranularidade(float granularidade) {
            this.granularidade = granularidade;
        }

        /**
         * Seta a quantidade de fragmentação externa de um enlace
         * @param fragmentacao
         */
        public void setFragmentacao(Double fragmentacao) {
            this.fragmentacao = fragmentacao;
        }

        /**
         *
         * @return Retorna a quantidade de fragmentação externa de um enlace
         */
        public Double getFragmentacao() {
            return this.fragmentacao;
        }
    }

    private List<HashMap<Integer, Enlace>> adjacencias = new ArrayList<HashMap<Integer, Enlace>>();

    /**
     *
     * @return A matriz de adjacencias representando o grafo lido
     */
    public List<HashMap<Integer, Enlace>> getAdjacencias() {
        return adjacencias;
    }

    /**
     *
     * @return O tamanho da matriz de adjacencias
     */
    public int size() {
        return adjacencias.size();
    }

    /**
     * Esse metodo instancia e adiciona um novo enlace direcional
     * @param origem No de origem
     * @param destino No de destino
     * @param custo Distância entre origem e destino
     * @param nSlots Quantidade de slots que o enlace possui
     * @param granularidade Espaçamento de um slot no enlace
     */
    public void addEnlace(int origem, int destino, int custo, int nSlots, float granularidade) {
        for (int i = origem - adjacencias.size(); i >= 0; i--) {
            adjacencias.add(new HashMap<Integer, Enlace>());
        }
        adjacencias.get(origem).put(destino, new Enlace(custo, nSlots, granularidade));
    }

    /**
     * Esse metodo instancia e adiciona um novo enlace bidirecional
     * @param origem No de origem
     * @param destino No de destino
     * @param custo Distância entre origem e destino
     * @param nSlots Quantidade de slots que o enlace possui
     * @param granularidade Espaçamento de um slot no enlace
     */
    public void addEnlaceBidirecional(int origem, int destino, int custo, int nSlots, float granularidade) {
        addEnlace(origem, destino, custo, nSlots, granularidade);
        addEnlace(destino, origem, custo, nSlots, granularidade);
    }

    /**
     *
     * @param origem No de origem
     * @param destino No de destino
     */
    public void removeEnlace(int origem, int destino) {
        adjacencias.get(origem).remove(destino);
    }

    /**
     *
     * @param origem No de origem
     * @param destino No de destino
     * @param custo Distância entre origem e destino
     */
    public void alteracustoEnlace(int origem, int destino, int custo) {
        adjacencias.get(origem).get(destino).setCusto(custo);
    }

    /**
     *
     * @param origem No de origem
     * @param destino No de destino
     * @return O objeto Enlace correspondente a origem/destino
     */
    public Enlace getEnlace(int origem, int destino) {
        return adjacencias.get(origem).get(destino);
    }

    /**
     *
     * @param origem No de origem
     * @param destino No de destino
     * @param fragmentacao Nova quantidade de fragmentacao do enlace
     */
    public void alteraFragmentacaoEnlace(int origem, int destino, Double fragmentacao) {
        adjacencias.get(origem).get(destino).setFragmentacao(fragmentacao);
    }
}
