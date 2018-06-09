class Nodo{
    private double lati;
    private double longi;
    private String city;

    public Nodo(String l){
        String[] campos = l.replaceAll("\t", " ").split(" ");
        int aux = campos.length-1;
        this.city = campos[0];
        this.lati = Double.parseDouble(campos[aux-1]);
        this.longi = Double.parseDouble(campos[aux]);
    }
    public double getLat(){
        return this.lati;
    }
    public double getLong(){
        return this.longi;
    }
    public void getCampos(){
        System.out.println("Cidade: " + this.city);
        System.out.println("Latitude: " + this.lati);
        System.out.println("Longitude: " + this.longi);
        System.out.println("-   -   -   -   -   -   -");
    }
}
