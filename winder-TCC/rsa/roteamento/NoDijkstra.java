package rsa.roteamento;

import java.util.List;

public class NoDijkstra implements Comparable<Object> {
	public int no;
	public int distancia;
	public List<Integer> caminho;

	public NoDijkstra(int no, int distancia, List<Integer> caminho) {
		this.no = no;
		this.distancia = distancia;
		this.caminho = caminho;
	}

	@Override
	public int compareTo(Object o) {
		NoDijkstra pr = (NoDijkstra) o;
		if (distancia == pr.distancia){
			if(no > pr.no)
				return 1;
			return -1;
		}
		if (distancia > pr.distancia)
			return 1;
		return -1;
	}
}