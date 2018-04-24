package rsa.roteamento;

import java.util.List;

public class ParCaminhoDisjuntos {

	private List<Integer> caminhoPrincipal, caminhoSecundario;

	public List<Integer> getCaminhoPrincipal() {
		return caminhoPrincipal;
	}

	public void setCaminhoPrincipal(List<Integer> caminhoPrincipal) {
		this.caminhoPrincipal = caminhoPrincipal;
	}

	public List<Integer> getCaminhoSecundario() {
		return caminhoSecundario;
	}

	public void setCaminhoSecundario(List<Integer> caminhoSecundario) {
		this.caminhoSecundario = caminhoSecundario;
	}

}
