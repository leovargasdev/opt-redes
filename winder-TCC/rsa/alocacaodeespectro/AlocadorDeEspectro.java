package rsa.alocacaodeespectro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rsa.Demanda;

import rsa.Enlaces;
import rsa.Enlaces.Enlace;
import rsa.distribuicao.DistribuicaoRandomica;

public class AlocadorDeEspectro {
    private int maior;

    private long[][][] mtrAlocacao;
    private DistribuicaoRandomica rand;
    private Enlaces enlaces;

    public AlocadorDeEspectro(Enlaces enlaces) {
        this.enlaces = enlaces;
        
        rand = new DistribuicaoRandomica();
        this.maior = 0;
        for (int i = 0; i < enlaces.size(); i++)
                for (int j = i + 1; j < enlaces.size(); j++) {
                        Enlace e = enlaces.getEnlace(i, j);
                        if (e == null)
                                continue;
                        int nSlots = e.getnSlots();
                        if (nSlots > maior)
                                maior = nSlots;
                }
        mtrAlocacao = new long[enlaces.size()][enlaces.size()][maior];
        for (int i = 0; i < enlaces.size(); i++)
                for (int j = 0; j < enlaces.size(); j++)
                        Arrays.fill(mtrAlocacao[i][j], -1);

        for (int i = 0; i < enlaces.size(); i++)
                for (int j = i + 1; j < enlaces.size(); j++) {
                        Enlace e = enlaces.getEnlace(i, j);
                        if (e == null)
                                continue;
                        for (int k = e.getnSlots(); k < maior; k++) {
                                mtrAlocacao[i][j][k] = Integer.MAX_VALUE;
                        }
                }
    }
    public boolean alocaCaminhoExactFitB(List<Integer> caminho, int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        int primeiroBlocoLivre = -1;
        //vetor resultante que guarda a fusao dos enlaces
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        int contador;
        int inicio;

        for (int i = 0; i < caminho.size() - 1; i++){
            inicio = 0;
            contador = 0;
            for (int j = 0; j < maior; j++){
                if (mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] >= 0){
                    merge[j] = Integer.MAX_VALUE;
                    contador = 0;
                    inicio = -1;
                }
                else{
                    contador++;
                    if (j>0)
                        if(mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j-1] >= 0 && inicio < 0)
                            inicio = j;
                }

                if(contador == nSlot && inicio != -1)
                    if (j+1 == maior || mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j+1] >= 0){
                        contador = Integer.MIN_VALUE;
                    }
                    else{
                        for(int k = inicio;mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][k]<0;k++)
                        {
                            merge[k] = Integer.MAX_VALUE;
                            if (k+1 == maior)
                                break;
                        }
                        contador  = Integer.MIN_VALUE;
                    }
            }       
        }

        //varaiveis de controle para determinar blocos que atendem a demanda
        inicio = -1;
        contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        for (int i = 0; i < maior; i++){

            if (inicio == -1)
                inicio = i;

            if (merge[i]>0){
                inicio = -1;
                contador = -1;
            }

            contador++;

            if(contador == nSlot && inicio >= 0 && i+1 != maior)
            {
                //se o proximo bloco esta ocupado, significa tamanho exato
                if(merge[i+1] > 0){
                    primeiroBlocoLivre = inicio;
                    break;
                }
            }
        }
        //para cada enlace, preenche os slots correspondentes ao primeiro bloco
        if (primeiroBlocoLivre >= 0){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = primeiroBlocoLivre; j < primeiroBlocoLivre + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            
            atualizaSlotsLivres(caminho,nSlot);
        
        }
        //se nao conseguiu alocar um tamanho exato, usa a politica first-fit pra escolher
        else
            return alocaCaminhoFirstFitB(caminho,nSlot,indiceCaminho);

        return true;
    }

    public boolean alocaCaminhoFirstFitB(List<Integer> caminho, int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        int primeiroBlocoLivre = -1;
        //vetor resultante que guarda a fusao dos enlaces
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int inicio = -1;
        int contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        for (int i = 0; i < maior; i++){

            if (inicio == -1)
                inicio = i;

            if (merge[i]>=0){
                inicio = -1;
                contador = -1;
            }

            contador++;

            if(contador >= nSlot && inicio >= 0)
            {
                primeiroBlocoLivre = inicio;
                break;
            }
        }
        //para cada enlace, preenche os slots correspondentes ao primeiro bloco
        if (primeiroBlocoLivre >= 0){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = primeiroBlocoLivre; j < primeiroBlocoLivre + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            
            atualizaSlotsLivres(caminho,nSlot);
            //System.out.println("bloco alocado: "+primeiroBlocoLivre+" qtd slots: "+contador);
            //imprimeSlots(caminho);
        }
        else
            return false;

        return true;
    }

    public boolean alocaCaminhoDedicatedPartition(List <Integer> caminho,int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        int primeiroBlocoLivre = -1;
        int inicioParticao=Integer.MIN_VALUE;
        int fimParticao=Integer.MAX_VALUE;
        //vetor resultante que guarda a fusao dos enlaces
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;
//80 * 50 / 12
        if (nSlot == 1){
            inicioParticao = 0;
            fimParticao = 45;
        }
        if (nSlot == 2 ){
            inicioParticao = 46;
            fimParticao = 136;
        }    
        if (nSlot == 4){
            inicioParticao = 137;
            fimParticao = 319;
        }    
        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int inicio = -1;
        int contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        for (int i = inicioParticao; i <= fimParticao; i++){

            if (inicio == -1)
                inicio = i;

            if (merge[i]>0){
                inicio = -1;
                contador = -1;
            }

            contador++;

            if(contador >= nSlot && inicio >= 0)
            {
                primeiroBlocoLivre = inicio;
                break;
            }
        }
        //para cada enlace, preenche os slots correspondentes ao primeiro bloco
        if (primeiroBlocoLivre >= 0){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = primeiroBlocoLivre; j < primeiroBlocoLivre + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            atualizaSlotsLivres(caminho,nSlot);
            
        }
        else
            return false;

        return true;
    }
    public boolean alocaCaminhoEqualPartition(List <Integer> caminho,int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        int primeiroBlocoLivre = -1;
        int inicioParticao=Integer.MIN_VALUE;
        int fimParticao=Integer.MAX_VALUE;
        //vetor resultante que guarda a fusao dos enlaces
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        if (nSlot == 1){
            inicioParticao = 0;
            fimParticao = 106;
        }
        if (nSlot == 4 ){
            inicioParticao = 107;
            fimParticao = 213;
        }    
        if (nSlot == 8){
            inicioParticao = 214;
            fimParticao = 319;
        }    
        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int inicio = -1;
        int contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        for (int i = inicioParticao; i <= fimParticao; i++){

            if (inicio == -1)
                inicio = i;

            if (merge[i]>0){
                inicio = -1;
                contador = -1;
            }

            contador++;

            if(contador >= nSlot && inicio >= 0)
            {
                primeiroBlocoLivre = inicio;
                break;
            }
        }
        //para cada enlace, preenche os slots correspondentes ao primeiro bloco
        if (primeiroBlocoLivre >= 0){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = primeiroBlocoLivre; j < primeiroBlocoLivre + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            atualizaSlotsLivres(caminho,nSlot);
            
           // System.out.println("bloco alocado: "+primeiroBlocoLivre+" qtd slots: "+contador);
        }
        else
            return false;

        return true;
    }

    public boolean alocaCaminhoSharedPartition(List <Integer> caminho,int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        int primeiroBlocoLivre = -1;
        int inicioParticao=Integer.MIN_VALUE;
        int fimParticao=Integer.MAX_VALUE;
        //vetor resultante que guarda a fusao dos enlaces
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        int auxnSlot = nSlot;

        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int inicio = -1;
        int contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        while(primeiroBlocoLivre<0){
            //System.out.println("tentar de novo "+auxnSlot);
            inicio = -1;
            contador = 0;

            if (auxnSlot == 1){
                inicioParticao = 0;
                fimParticao = 45;
            }
            if (auxnSlot == 2 ){
                inicioParticao = 46;
                fimParticao = 136;
            }    
            if (auxnSlot == 4 ){
                inicioParticao = 137;
                fimParticao = 319;
            }

            for (int i = inicioParticao; i <= fimParticao; i++){

                if (inicio == -1)
                    inicio = i;

                if (merge[i]>0){
                    inicio = -1;
                    contador = -1;
                }

                contador++;

                if(contador >= nSlot && inicio >= 0)
                {
                    primeiroBlocoLivre = inicio;
                    break;
                }
            }
            if(primeiroBlocoLivre < 0){
                if (auxnSlot == 4){
                    //System.out.println(" era na part 4 tenta na 2 agora");
                    auxnSlot = 2; continue;}
                else if(auxnSlot == 2){
                    auxnSlot = 1;
                    //System.out.println(" era na 2, tenta na 1 agora");
                }
                else if(auxnSlot == 1){
                    //System.out.println("Nao consegui alocar, proxima!");
                    break;
                }
            }

        }
        //para cada enlace, preenche os slots correspondentes ao primeiro bloco
        if (primeiroBlocoLivre >= 0){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = primeiroBlocoLivre; j < primeiroBlocoLivre + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            atualizaSlotsLivres(caminho,nSlot);
            
            //System.out.println("bloco alocado: "+primeiroBlocoLivre+" qtd slots: "+contador);
            //imprimeSlots(caminho);
        }
        else
            return false;

        return true;
    }
    
    public boolean alocaCaminhoRandomFit(List<Integer> caminho, int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        //vetor resultante que guarda a fusao dos enlaces
        ArrayList<Integer> inicioBlocos = new ArrayList<>();
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int inicio = -1;
        int contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        for (int i = 0; i < maior; i++){

            if (inicio == -1)
                inicio = i;

            if (merge[i]>0){
                inicio = -1;
                contador = -1;
            }

            contador++;

            if(contador >= nSlot && inicio >= 0)
                if (!inicioBlocos.contains(inicio)){
                    inicioBlocos.add(inicio);
                    inicio = -1;
                    contador = 0;
                }
        }

        //para cada enlace, preenche os slots correspondentes ao menor bloco
        if (!inicioBlocos.isEmpty()){

            int blocoRandomico = inicioBlocos.get(rand.nextInt(inicioBlocos.size()));

            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = blocoRandomico; j < blocoRandomico + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            atualizaSlotsLivres(caminho,nSlot);
            //System.out.println("Blocos Livres no enlace: "+inicioBlocos.size()+" bloco randomico alocado: "+blocoRandomico+" qtd slots: "+nSlot);
        }
        else
            return false;

        return true;
    }
    public boolean alocaCaminhoBestFit(List<Integer> caminho, int nSlot, long indiceCaminho){
        int [] merge = new int[maior];
        //vetor resultante que guarda a fusao dos enlaces
        ArrayList<Integer> inicioBlocos = new ArrayList<>();
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int inicio = -1;
        int contador = 0;

        /*laÃ§o adiciona para lista 'inicioBlocos' todos os inicios de blocos 
        suficientemente grandes para atender a demanda*/
        for (int i = 0; i < maior; i++){

            if (inicio == -1)
                inicio = i;

            if (merge[i]>0){
                inicio = -1;
                contador = -1;
            }

            contador++;

            if(contador >= nSlot && inicio >= 0)
                if (!inicioBlocos.contains(inicio))
                    inicioBlocos.add(inicio);
        }

        //variaveis para selecao dos menores blocos da lista
        int menorBloco = Integer.MAX_VALUE;
        int indiceMenor = Integer.MAX_VALUE;

        //laÃ§o escolhe o menor bloco, entre os blocos resultantes da 'inicioBlocos'
        for (int j = 0; j < inicioBlocos.size(); j++){
            contador = 0;
            int k = inicioBlocos.get(j);

            while(merge[k] < 0){
                contador++;
                k++;
                if (k == maior)
                    break;
            }

            if (contador <= menorBloco)
            {
                menorBloco = contador;
                indiceMenor = inicioBlocos.get(j);
            }
        }

        //para cada enlace, preenche os slots correspondentes ao menor bloco
        if (indiceMenor != Integer.MAX_VALUE){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = indiceMenor; j < indiceMenor + nSlot; j++) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
           // System.out.println("bloco alocado: "+indiceMenor+" qtd slots: "+menorBloco);
            atualizaSlotsLivres(caminho,nSlot);
        }
        else
            return false;

        return true;
    }

    public boolean alocaCaminhoLastFitB(List<Integer> caminho, int nSlot, long indiceCaminho) {
        int [] merge = new int[maior];
        int ultimoBlocoLivre = -1;
        //vetor resultante que guarda a fusao dos enlaces
        //preenche vetor resultante com -1
        Arrays.fill(merge, -1);

        //se demanda eh igual a zero retorna true
        if (nSlot == 0)
            return true;

        /*para cada quadro ocupado no enlace, pinta o quadro correspondente no vetor resultante
        Formando um vetor apenas, com os blocos livres e comuns entre todos enlaces
        */
        mergeSlots(merge,caminho);

        //varaiveis de controle para determinar blocos que atendem a demanda
        int fim = -1;
        int contador = 0;


        for (int i = maior-1; i >= 0; i--){

            if (fim == -1)
                fim = i;

            if (merge[i]>0){
                fim= -1;
                contador = -1;
            }

            contador++;

            if(contador >= nSlot && fim < maior)
            {
                ultimoBlocoLivre = fim;
                break;
            }
        }
        //para cada enlace, preenche os slots correspondentes ao primeiro bloco
        if (ultimoBlocoLivre >= 0){
            for (int i = 0; i < caminho.size() - 1; i++) {
                for (int j = ultimoBlocoLivre; j > ultimoBlocoLivre - nSlot; j--) {
                    mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = indiceCaminho;
                }
            }
            atualizaSlotsLivres(caminho,nSlot);
            
            //System.out.println("bloco alocado: "+ultimoBlocoLivre+" qtd slots: "+contador);
            //imprimeSlots(caminho);
        }
        else
            return false;

        return true;
    }

    public boolean alocaPseudoPartition(List<Integer> caminho, int nSlot, long indiceCaminho) {
        /* Essa politica de alocacao de espectro aloca as demandas que requerem
        menos banda na primeira metade do espectro, enquanto as demandas
        que usam mais banda sao alocadas no fim do espectro
        */
        if (nSlot < 3)
            return alocaCaminhoFirstFitB(caminho,nSlot,indiceCaminho);

        if (nSlot > 3)
            return alocaCaminhoLastFitB(caminho,nSlot,indiceCaminho);

        return false;
    }

    public long[][][] getMtrAlocacao() {
            return mtrAlocacao;
    }


    public void setMtrAlocacao(long[][][] mtrAlocacao) {
            this.mtrAlocacao = mtrAlocacao;
    }

    public void mergeSlots(int [] merge, List<Integer> caminho){
        for (int i = 0; i < caminho.size() - 1; i++){
            for (int j = 0; j < maior; j++){
                if (mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] >= 0){
                    merge[j] = Integer.MAX_VALUE;
                }
            }     
        }
    }

    public int getMaior() {
        return maior;
    }
    public void imprimeSlots(List <Integer> caminho){
        String buffer = new String();
        buffer = caminho.get(0)+"->"+caminho.get(caminho.size()-1)+" ";

        for (int i = 0; i < caminho.size() - 1; i++){
            for (int j = 0; j< maior; j++)
            {
                if(mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] == Integer.MAX_VALUE)
                    break;
                //System.out.println(" " +mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j]);
                buffer += " "+mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j];
            }

                buffer += "|||";
        }
        System.out.println(" "+caminho+" "+buffer);
    }
    
    public void imprimeSlotsEnlace(int origem,int destino){
        String buffer = new String();
            for (int j = 0; j< maior; j++)
            {
              //System.out.println(" " +mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j]);
                buffer += " "+mtrAlocacao[origem][destino][j];
            }
            System.out.println("origem "+origem+" destino "+destino+" "+buffer);
    }
    
    public void removeDemanda(List<Integer> caminho, long indiceCaminho){
        
        for (int i = 0; i < caminho.size() - 1; i++) {
                Enlaces.Enlace enlace = enlaces.getEnlace(caminho.get(i), caminho.get(i+1));
                
                for (int j = 0; j < maior; j++) {
                    if (mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] == indiceCaminho){
                        mtrAlocacao[caminho.get(i)][caminho.get(i + 1)][j] = -1;
                        enlace.setSlotsLivres((enlace.getSlotsLivres()+1));
                    }
                    
                }
        }
    }

    private void atualizaSlotsLivres(List<Integer> caminho, int nSlot) {
         for (int i = 0; i < caminho.size() - 1; i++){
            Enlaces.Enlace enlace = enlaces.getEnlace(caminho.get(i), caminho.get(i+1));
            int slotsLivresAtual = enlace.getSlotsLivres();
            enlace.setSlotsLivres(slotsLivresAtual-nSlot);
         }
    }

}
