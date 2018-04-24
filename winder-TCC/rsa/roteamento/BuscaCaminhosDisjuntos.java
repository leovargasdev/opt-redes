package rsa.roteamento;

import java.util.ArrayList;
import java.util.List;
import rsa.Enlaces;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author winderdias
 */
public class BuscaCaminhosDisjuntos {
    private Enlaces enlaces;

    public BuscaCaminhosDisjuntos(Enlaces enlaces) {
    	this.enlaces = enlaces;
    }
    public List<ParCaminhoDisjuntos> getNxMParDeCaminhosDisjuntos(int origem, int destino, int n, int m)
			throws Exception {
		List<ParCaminhoDisjuntos> resultado = new ArrayList<>();
		Dijkstra dj = new Dijkstra(enlaces);
		List<List<Integer>> caminhosPrincipais = dj.getKCaminhoMinimo(origem, destino, n);
                
		dj = null;
		
                for (List<Integer> caminho : caminhosPrincipais) {
                    //System.out.println(" Origem: " +origem+" Destino " +destino+ " caminho: "+caminho);
			Enlaces atualizado = (Enlaces) enlaces.clone();
			// remove o caminho e seta 0 no peso das arestas do caminho reverso
			for (int i = 0; i < caminho.size() - 1; i++) {
				atualizado.removeEnlace(caminho.get(i), caminho.get(i + 1));
				atualizado.alteracustoEnlace(caminho.get(i + 1), caminho.get(i), 0);
			}
			dj = new Dijkstra(atualizado);
			List<List<Integer>> caminhosSecundarios = dj.getKCaminhoMinimo(origem, destino, m);
			dj = null;
			if (caminhosSecundarios.isEmpty())
				throw new Exception("Nao possui dois caminhos disjuntos para " + origem + "->" + destino);
			for (List<Integer> caminho2 : caminhosSecundarios) {
				Enlaces anel = new Enlaces();
				for (int i = 0; i < caminho.size() - 1; i++) {
					anel.addEnlaceBidirecional(caminho.get(i), caminho.get(i + 1),
							enlaces.getEnlace(caminho.get(i), caminho.get(i + 1)).getCusto(), 0, (float) 12.5);
				}
				for (int i = 0; i < caminho2.size() - 1; i++) {
					if (anel.getEnlace(caminho2.get(i), caminho2.get(i + 1)) != null) {
						anel.removeEnlace(caminho2.get(i), caminho2.get(i + 1));
						anel.removeEnlace(caminho2.get(i + 1), caminho2.get(i));
					} else
						anel.addEnlaceBidirecional(caminho2.get(i), caminho2.get(i + 1),
								enlaces.getEnlace(caminho2.get(i), caminho2.get(i + 1)).getCusto(), 0, (float) 12.5);
				}
				dj = new Dijkstra(anel);
				ParCaminhoDisjuntos pcd = new ParCaminhoDisjuntos();
				// caminho de trabalho mais curto
				List<List<Integer>> cam = dj.getKCaminhoMinimo(origem, destino, 1);
				pcd.setCaminhoPrincipal(cam.get(0));
				// remmove os enlaces do caminho de trabalho
				for (int i = 0; i < cam.get(0).size() - 1; i++) {
					anel.removeEnlace(cam.get(0).get(i), cam.get(0).get(i + 1));
					anel.removeEnlace(cam.get(0).get(i + 1), cam.get(0).get(i));
				}
				// segundo caminho para backup
				cam = dj.getKCaminhoMinimo(origem, destino, 1);
				pcd.setCaminhoSecundario(cam.get(0));
                                /*
                                for(ParCaminhoDisjuntos pcdAux : resultado ){
                                    
                                    if(pcd.getCaminhoPrincipal().equals(pcdAux.getCaminhoPrincipal()) && pcd.getCaminhoSecundario().equals(pcdAux.getCaminhoSecundario())){
                                        pcd.setCaminhoPrincipal(pcdAux.getCaminhoSecundario());
                                        pcd.setCaminhoSecundario(pcdAux.getCaminhoPrincipal());
                                        pcd.
                                    }
                                }*/
                                
				resultado.add(pcd);
				dj = null;
			}
		}
		return resultado;
	}
}
