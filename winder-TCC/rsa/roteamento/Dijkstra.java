package rsa.roteamento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import rsa.Enlaces;
import rsa.Enlaces.Enlace;

public class Dijkstra {
	private Enlaces enlaces;

	public Dijkstra(Enlaces enlaces) throws CloneNotSupportedException {
		this.enlaces = enlaces;
	}

	public List<List<Integer>> getKCaminhoMinimo(int origem, int destino, int k) {
		List<List<Integer>> resultado = new ArrayList<>();
		PriorityQueue<NoDijkstra> pq = new PriorityQueue<>();
		List<Integer> caminho = new ArrayList<>();
		caminho.add(origem);
		int[] count = new int[enlaces.size()];
		Arrays.fill(count, 0);
		pq.add(new NoDijkstra(origem, 0, caminho));
		NoDijkstra no;
		do {
			no = pq.poll();
			if (no.no == destino) {
				resultado.add(no.caminho);
				if (resultado.size() == k)
					break;
				continue;
			}

			if (++count[no.no] > k)
				continue;

			HashMap<Integer, Enlace> vizinhos = enlaces.getAdjacencias().get(no.no);
			for (int i : vizinhos.keySet()) {
				if (no.caminho.contains(i))
					continue;
				caminho = new ArrayList<>(no.caminho);
				caminho.add(i);
				// atualiza fila de prioridade
				pq.add(new NoDijkstra(i, no.distancia + vizinhos.get(i).getCusto(), caminho));
			}
			no = null;
		} while (!pq.isEmpty());
		return resultado;
	}

}
