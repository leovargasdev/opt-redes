class Caminho{
    private double custoCaminho;
    public String caminho;

    public Caminho(double c, String caminho){
        this.caminho = caminho;
        this.custoCaminho = c;
    }

    public double getCusto(){
        return this.custoCaminho;
    }

    public void setCusto(double c){
        this.custoCaminho = c;
    }

}
