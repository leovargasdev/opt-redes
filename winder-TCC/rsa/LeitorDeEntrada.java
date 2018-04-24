package rsa;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;
import rsa.distribuicao.DistribuicaoRandomica;

/**
 * Essa classe faz a leitura do arquivo de entrada contendo as informacoes sobre
 * cada enlace
 * @author Winder Dias
 * @author Tailan Bonassi
 */
public class LeitorDeEntrada {

    /**
     * Esse método retorna uma lista de enlaces que representa o grafo lido
     * @param caminhoArq Caminho do diretório onde o arquivo está presente
     * @return Lista de enlaces (grafo)
     * @throws IOException
     */
    public Enlaces getEnlacesByArqEntrada(String caminhoArq) throws IOException {
		Enlaces enlaces = new Enlaces();
		int origem, destino, custo, nSlots;
                DistribuicaoRandomica rand = new DistribuicaoRandomica(0);
		
                for (String linha : Files.readAllLines(FileSystems.getDefault().getPath(caminhoArq))) {
			String[] numeros = linha.split(" ");
			origem = Integer.parseInt(numeros[0]) - 1;
			destino = Integer.parseInt(numeros[1]) - 1;
                        custo = Integer.parseInt(numeros[2]);
                        System.out.println("Origem: "+origem+" Destino: "+destino+" custo: "+custo);
                        nSlots = 320;//qtd 320 pavan http://www.linktionary.com/w/wdm.html
			enlaces.addEnlaceBidirecional(origem, destino, custo, nSlots, (float) 12.5);
		}
		return enlaces;
	}
}
