package rsa;

import java.util.ArrayList;
import java.util.List;
import rsa.alocacaodeespectro.AlocadorDeEspectro;
import rsa.roteamento.ParCaminhoDisjuntos;

/**
 * Essa classe utiliza métodos para a ordenação da lista de caminhos
 * de modo a priorizar os primeiros caminhos da lista baseado em um critério
 * @author Winder Dias
 */
public class OrdenadorCaminhos {
    private List<List<Integer>> caminhosOrdenados;
    private AlocadorDeEspectro alocacao;
    private Enlaces enlaces;
    
    /**
     *
     * @param enlaces Informações dos enlaces
     * @param alocacao Matriz de alocação
     */
    public OrdenadorCaminhos(Enlaces enlaces,AlocadorDeEspectro alocacao){
        this.alocacao = alocacao;
        this.enlaces = enlaces;
    }
 
    /**
     * Prioriza os caminhos Com mais Slots
     * @param listaCaminhosDisjuntos Lista ParCaminhoDisjuntos com a ordenação padrão
     * @return uma lista ParCaminhoDisjuntos reordenada pelos caminhos com mais slots
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosMostSlotsFreeFirst(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        
        int slotsLivres1 = 0;
        int slotsLivres2 = 0;
        double mediaSlotsLivres;
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosSlots = new ArrayList<>();
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            //para cada enlace, calcula a fragmentacao externa do caminho principal
            for (int i = 0; i < caminhoDisjunto1.size() - 1; i++){
                Enlaces.Enlace enlace = enlaces.getEnlace(caminhoDisjunto1.get(i), caminhoDisjunto1.get(i+1));
                slotsLivres1 += enlace.getSlotsLivres();
            }
            
            //para cada enlace, calcula a fragmentacao externa do caminho secundario
            for (int i = 0; i < caminhoDisjunto2.size() - 1; i++){
                Enlaces.Enlace enlace = enlaces.getEnlace(caminhoDisjunto2.get(i), caminhoDisjunto2.get(i+1));
                slotsLivres2 += enlace.getSlotsLivres();
            }
            
            //faz a media
            mediaSlotsLivres = ((double)slotsLivres2+(double)slotsLivres1)/2.0;
            //System.out.println("C1: "+caminhoDisjunto1+" C2: "+caminhoDisjunto2+" MediaSlotsLivres: "+mediaSlotsLivres);
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(mediaSlotsLivres);
                disjuntosOrdenadosSlots.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(mediaSlotsLivres > listaOrdenada.get(k)){
                        listaOrdenada.add(k,mediaSlotsLivres);
                        disjuntosOrdenadosSlots.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(mediaSlotsLivres);
                    disjuntosOrdenadosSlots.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
            slotsLivres1 = 0;
            slotsLivres2 = 0;
        }
   
        return disjuntosOrdenadosSlots;
    }
    
    /**
     * Prioriza os caminhos Com Slots sobre Saltos
     * @param listaCaminhosDisjuntos Lista ParCaminhoDisjuntos com a ordenação padrão
     * @return uma lista ParCaminhoDisjuntos reordenada pelos caminhos com mais slots sobre saltos
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosMostSlotsOverHopsFreeFirst(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        
        int slotsLivres = 0;
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosSlots = new ArrayList<>();
        double slotsOverHops1,slotsOverHops2,slotsOverHopsFinal;  
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
               
            slotsOverHops1=0.0;
            slotsOverHops2=0.0;
            
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            for (int i = 0; i < caminhoDisjunto1.size() - 1; i++){
                Enlaces.Enlace enlace = enlaces.getEnlace(caminhoDisjunto1.get(i), caminhoDisjunto1.get(i+1));
                slotsLivres += enlace.getSlotsLivres();
            }
            
            slotsOverHops1 = (double)slotsLivres/(double)(caminhoDisjunto1.size()-1);
            slotsLivres = 0;
            
            //para cada enlace, calcula a fragmentacao externa do caminho secundario
            for (int i = 0; i < caminhoDisjunto2.size() - 1; i++){
                Enlaces.Enlace enlace = enlaces.getEnlace(caminhoDisjunto2.get(i), caminhoDisjunto2.get(i+1));
                slotsLivres += enlace.getSlotsLivres();
            }
            
            slotsOverHops2 = (double)slotsLivres/(double)(caminhoDisjunto2.size()-1);
            
            slotsOverHopsFinal = (slotsOverHops1 + slotsOverHops2) / 2;
            
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(slotsOverHopsFinal);
                disjuntosOrdenadosSlots.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(slotsOverHopsFinal > listaOrdenada.get(k)){
                        listaOrdenada.add(k,slotsOverHopsFinal);
                        disjuntosOrdenadosSlots.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(slotsOverHopsFinal);
                    disjuntosOrdenadosSlots.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
            slotsLivres = 0;
            
        }
        
        /*System.out.println("Caminhos devolvidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  disjuntosOrdenadosSlots){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        
        return disjuntosOrdenadosSlots;
    }
    
    /**
     * Prioriza os caminhos com menor nível de fragmentação
     * @param listaCaminhosDisjuntos Lista ParCaminhoDisjuntos com a ordenação padrão
     * @return uma lista ParCaminhoDisjuntos reordenada crescentemente pela fragmentação
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosMenosFragmentadoMediaPrimeiro(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        Double FextRotaFinal = 0.0;
        Double FextRota1 = 0.0;
        Double FextRota2 = 0.0; 
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosFragmetacao = new ArrayList<>();
        
        /*System.out.println("Caminhos recebidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            //para cada enlace, calcula a fragmentacao externa do caminho principal
            for (int i = 0; i < caminhoDisjunto1.size() - 1; i++){
                FextRota1 += this.calculaFragmentacaoEnlace(caminhoDisjunto1.get(i),caminhoDisjunto1.get(i+1));
            }
            FextRota1 /= (double)(caminhoDisjunto1.size()-1);
            //para cada enlace, calcula a fragmentacao externa do caminho secundario
            for (int i = 0; i < caminhoDisjunto2.size() - 1; i++){
                FextRota2 += this.calculaFragmentacaoEnlace(caminhoDisjunto2.get(i),caminhoDisjunto2.get(i+1));
            }
            FextRota2 /= (double)(caminhoDisjunto2.size()-1);
            // faz a media de fragmetacao dos caminhos
            
            FextRotaFinal = (FextRota1+FextRota2)/2.0;
            
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(FextRotaFinal);
                disjuntosOrdenadosFragmetacao.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(FextRotaFinal < listaOrdenada.get(k)){
                        listaOrdenada.add(k,FextRotaFinal);
                        disjuntosOrdenadosFragmetacao.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(FextRotaFinal);
                    disjuntosOrdenadosFragmetacao.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
            FextRota1 = 0.0;
            FextRota2 = 0.0;
        }
        /*
        System.out.println("Caminhos devolvidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  disjuntosOrdenadosFragmetacao){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        
        return disjuntosOrdenadosFragmetacao;
    }
    
    /**
     * Prioriza os caminhos com maior nível de fragmentação
     * @param listaCaminhosDisjuntos Lista ParCaminhoDisjuntos com a ordenação padrão
     * @return uma lista ParCaminhoDisjuntos reordenada decrescentemente pela fragmentação
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosMaisFragmentadoPrimeiro(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        
        Double FextRota = 0.0;
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosFragmetacao = new ArrayList<>();
        /*
        System.out.println("Caminhos recebidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            //para cada enlace, calcula a fragmentacao externa do caminho principal
            for (int i = 0; i < caminhoDisjunto1.size() - 1; i++){
                FextRota += this.calculaFragmentacaoEnlace(caminhoDisjunto1.get(i),caminhoDisjunto1.get(i+1));
            }
            
            //para cada enlace, calcula a fragmentacao externa do caminho secundario
            for (int i = 0; i < caminhoDisjunto2.size() - 1; i++){
                FextRota += this.calculaFragmentacaoEnlace(caminhoDisjunto2.get(i),caminhoDisjunto2.get(i+1));
            }
            
            // faz a media de fragmetacao dos caminhos
            FextRota /= 2;
            
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(FextRota);
                disjuntosOrdenadosFragmetacao.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(FextRota > listaOrdenada.get(k)){
                        listaOrdenada.add(k,FextRota);
                        disjuntosOrdenadosFragmetacao.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(FextRota);
                    disjuntosOrdenadosFragmetacao.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
            FextRota = 0.0;
        }
        /*
        System.out.println("Caminhos devolvidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  disjuntosOrdenadosFragmetacao){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        
        return disjuntosOrdenadosFragmetacao;
    }
    
    /**
     * Esse metodo faz o calculo do nivel de utilizacao de um enlace
     * @param origem Origem do enlace
     * @param destino Destino do enlace
     * @return Valor de utilizacao
     */
    public Double calculaUtilizacaoEnlace(int origem,int destino){
        int slotsLivres;
        int totalEnlace;
        double utilizationRatio;
        
        Enlaces.Enlace enlace = enlaces.getEnlace(origem, destino);
        slotsLivres = enlace.getSlotsLivres();
        totalEnlace = enlace.getnSlots();
        utilizationRatio = ((double)totalEnlace - (double)slotsLivres)/(double)totalEnlace;
        //System.out.println(utilizationRatio);
        return utilizationRatio;
    }
    
    /**
     * Esse método faz o cálculo da fragmentação externa de um enlace
     * @param origem Origem do enlace
     * @param destino Destino do enlace
     * @return Valor de fragmentaçao
     */
    public Double calculaFragmentacaoEnlace(int origem,int destino){
        long [][][] mtrAlocacao = alocacao.getMtrAlocacao();
        int contaLivres = 0;
        Integer maiorLivre = Integer.MIN_VALUE;
        Integer contAux = 0;
        
        
        Enlaces.Enlace enlace = enlaces.getEnlace(origem, destino);
        
        for (int j = 0; j < enlace.getnSlots(); j++){
            if (mtrAlocacao[origem][destino][j] < 0){
                contaLivres+=1;
                contAux ++;
                if(contAux > maiorLivre)
                    maiorLivre = contAux;
            }
            else{
                while(mtrAlocacao[origem][destino][j] >= 0){
                    j++;
                    //320 slots lidos, break
                    if (j == enlace.getnSlots())
                        break;
                }
                //retorna para o slot ocupado, pois o loop principal ira avança lo
                j--;
                
                contAux = 0;
            }
            
        }
        //nenhum slot livre
        if (contaLivres == 0){
            enlace.setFragmentacao(0.0);
            return 1000.0;
        }
        
        //no caso de existir apenas um bloco com slots livres, seta ele como maior
        if(maiorLivre == Integer.MIN_VALUE)
            maiorLivre = contAux;
        
        enlace.setFragmentacao(1-((double)maiorLivre/(double)contaLivres));
        
        return 1-((double)maiorLivre/(double)contaLivres);
    }
    
    private int contaSlotsLivres(int origem,int destino){
        long [][][] mtrAlocacao = alocacao.getMtrAlocacao();
        Integer contaLivres = 0;
        
        Enlaces.Enlace enlace = enlaces.getEnlace(origem, destino);
        
        for (int j = 0; j < enlace.getnSlots(); j++){
            if (mtrAlocacao[origem][destino][j] < 0){
                contaLivres+=1;
            }
        }
        
        enlace.setSlotsLivres(contaLivres);
        return contaLivres;
    }
    
    /**
     *
     * @param listaCaminhosDisjuntos
     * @return
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosFewSlotsFreeFirst(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        
        int slotsLivres = 0;
        double mediaSlotsLivres;
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosSlots = new ArrayList<>();
        
        System.out.println("Caminhos recebidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            //para cada enlace, calcula a fragmentacao externa do caminho principal
            for (int i = 0; i < caminhoDisjunto1.size() - 1; i++){
                Enlaces.Enlace enlace = enlaces.getEnlace(caminhoDisjunto1.get(i), caminhoDisjunto1.get(i+1));
                slotsLivres += enlace.getSlotsLivres();
            }
            
            //para cada enlace, calcula a fragmentacao externa do caminho secundario
            for (int i = 0; i < caminhoDisjunto2.size() - 1; i++){
                Enlaces.Enlace enlace = enlaces.getEnlace(caminhoDisjunto2.get(i), caminhoDisjunto2.get(i+1));
                slotsLivres += enlace.getSlotsLivres();
            }
            
            //faz a media
            mediaSlotsLivres = slotsLivres/2;
            
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(mediaSlotsLivres);
                disjuntosOrdenadosSlots.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(mediaSlotsLivres < listaOrdenada.get(k)){
                        listaOrdenada.add(k,mediaSlotsLivres);
                        disjuntosOrdenadosSlots.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(mediaSlotsLivres);
                    disjuntosOrdenadosSlots.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
            slotsLivres = 0;
        }
       
        System.out.println("Caminhos devolvidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  disjuntosOrdenadosSlots){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }
        
        
        return disjuntosOrdenadosSlots;
    }
    
    /**
     *
     * @param listaCaminhosDisjuntos
     * @return
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosMenosEnlacesPrimeiro(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        
        int slotsLivres = 0;
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosMenosEnlaces = new ArrayList<>();
        double enlaces1,enlaces2,enlacesFinal;  
        /*System.out.println("Caminhos recebidos: ");
         for (ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
              
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            enlaces1 = (double)(caminhoDisjunto1.size()-1);
            
            enlaces2 = (double)(caminhoDisjunto2.size()-1);
            
            enlacesFinal = (enlaces1 + enlaces2) / 2;
            
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(enlacesFinal);
                disjuntosOrdenadosMenosEnlaces.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(enlacesFinal < listaOrdenada.get(k)){
                        listaOrdenada.add(k,enlacesFinal);
                        disjuntosOrdenadosMenosEnlaces.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(enlacesFinal);
                    disjuntosOrdenadosMenosEnlaces.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
             enlacesFinal = 0;
            
        }
        
       /* System.out.println("Caminhos devolvidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  disjuntosOrdenadosMenosEnlaces){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        
        return disjuntosOrdenadosMenosEnlaces;
    }

    /**
     *
     * @param listaCaminhosDisjuntos
     * @return
     */
    public List<ParCaminhoDisjuntos> ordenaCaminhosMenosFragmentadoPrimeiro(List<ParCaminhoDisjuntos> listaCaminhosDisjuntos){
        
        Double FextRota = 0.0;
        List <Double> listaOrdenada =  new ArrayList<>();
        List <Integer> caminhoDisjunto1;
        List <Integer> caminhoDisjunto2;
        List<ParCaminhoDisjuntos> disjuntosOrdenadosFragmetacao = new ArrayList<>();
        
        /*System.out.println("Caminhos recebidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        for(ParCaminhoDisjuntos caminhosDisjuntos :  listaCaminhosDisjuntos){
            caminhoDisjunto1 = caminhosDisjuntos.getCaminhoPrincipal();
            caminhoDisjunto2 = caminhosDisjuntos.getCaminhoSecundario();
            
            //para cada enlace, calcula a fragmentacao externa do caminho principal
            for (int i = 0; i < caminhoDisjunto1.size() - 1; i++){
                FextRota += this.calculaFragmentacaoEnlace(caminhoDisjunto1.get(i),caminhoDisjunto1.get(i+1));
            }
            
            //para cada enlace, calcula a fragmentacao externa do caminho secundario
            for (int i = 0; i < caminhoDisjunto2.size() - 1; i++){
                FextRota += this.calculaFragmentacaoEnlace(caminhoDisjunto2.get(i),caminhoDisjunto2.get(i+1));
            }
            
            // faz a media de fragmetacao dos caminhos
            FextRota /= 2;
            
            /* Se a lista de caminhos ordenados por fragmentacao esta vazia
            /* adiciona na primeira posicao */
            
            if(listaOrdenada.isEmpty()){
                listaOrdenada.add(FextRota);
                disjuntosOrdenadosFragmetacao.add(caminhosDisjuntos);
            }
            //se nao, adiciona ordenado por indice fragmentacao ext na lista
            else{
                int inserido = 0;
                
                for (int k = 0;k < listaOrdenada.size();k++)
                    if(FextRota < listaOrdenada.get(k)){
                        listaOrdenada.add(k,FextRota);
                        disjuntosOrdenadosFragmetacao.add(k,caminhosDisjuntos);
                        inserido = 1;
                        break;
                    }
                if (inserido == 0){
                    listaOrdenada.add(FextRota);
                    disjuntosOrdenadosFragmetacao.add(caminhosDisjuntos);
                }
            }//fim da insercao na lista
            
            /*seta a fragmentacao para 0, e uma nova fragmentacao sera calculada
            para um novo caminho candidato */
            FextRota = 0.0;
        }
        /*
        System.out.println("Caminhos devolvidos: ");
        for (ParCaminhoDisjuntos caminhosDisjuntos :  disjuntosOrdenadosFragmetacao){
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoPrincipal(), enlaces, alocacao);
            Principal.imprimeCustoTotalCaminho(caminhosDisjuntos.getCaminhoSecundario(), enlaces, alocacao);
        }*/
        
        
        return disjuntosOrdenadosFragmetacao;
    }
}
