class Calculo{
    // private int[] caminhoOriginal;
    // private int[] vizinhos;
    // private double distanciaTotalOriginal;
    //
    // public Calculo(int quant){
    //     this.caminhoOriginal = new int[quant];
    //     this.vizinhos = new int[quant];
    //     for (int k = 0; k < quant; k++) this.vizinhos = this.caminhoOriginal[k] = 0;
    //     this.distanciaTotalOriginal = comprimentoTotal(quant);
    // }

    public double dTotal(double[][] custos, byte[][] conexoes, int tam){
        double distancia = 0.0;
        for(int k = 0; k < tam; k++)
            for (int i = k+1; i < tam; i++)
                if(conexoes[k][i] == 1)
                    // System.out.println("conexoes["+k+"]["+i+"]");
                    distancia+= custos[k][i];
        return distancia;
    }

}
