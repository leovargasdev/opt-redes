class Nodo{
    private double lati;
    private double longi;
    private String city;

    public Nodo(String l){
        String[] campos = l.replaceAll("\t", " ").split(" ");
        this.city = campos[0];
        this.lati = Double.parseDouble(campos[1]);
        this.longi = Double.parseDouble(campos[2]);
    }

    public void getCampos(){
        System.out.println("Cidade: " + this.city);
        System.out.println("Latitude: " + this.lati);
        System.out.println("Longitude: " + this.longi);
        System.out.println("-  -  -  -  -  -  -  -  -\n");
    }
}
