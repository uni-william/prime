package br.com.sis.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.sis.entity.Manutencao;
import br.com.sis.entity.Veiculo;
import br.com.sis.repository.ManutencaoRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.service.ManutencaoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class ManutencoesVeiculoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ManutencaoService manutencaoService;

	@Inject
	private VeiculoRepository veiculoRepository;

	@Inject
	private ManutencaoRepository manutencaoRepository;

	private Veiculo veiculo;

	private Manutencao manutencao;
	private Manutencao manutencaoSelecionada;

	private List<Manutencao> manutencoesVeiculo = new ArrayList<Manutencao>();

	private UploadedFile uploadedFile;

	private StreamedContent file;

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Manutencao getManutencao() {
		return manutencao;
	}

	public void setManutencao(Manutencao manutencao) {
		this.manutencao = manutencao;
	}

	public List<Manutencao> getManutencoesVeiculo() {
		return manutencoesVeiculo;
	}

	public void setManutencoesVeiculo(List<Manutencao> manutencoesVeiculo) {
		this.manutencoesVeiculo = manutencoesVeiculo;
	}

	public Manutencao getManutencaoSelecionada() {
		return manutencaoSelecionada;
	}

	public void setManutencaoSelecionada(Manutencao manutencaoSelecionada) {
		this.manutencaoSelecionada = manutencaoSelecionada;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}
	

	public void inicializar() {
		if (veiculo == null) {
			FacesUtil.redirecionarPagina("/Home.xhtml");
		} else {
			manutencao = new Manutencao();
			manutencao.setData(new Date());
			manutencao.setVeiculo(veiculo);
			manutencoesVeiculo = manutencaoRepository.manutencoesVeiculo(veiculo);
		}
	}

	public void adicionarManutenco() {
		boolean podeGravar = true;
		veiculo = veiculoRepository.porId(veiculo.getId());
		veiculo.getManutencoes().add(manutencao);
		manutencao.setVeiculo(veiculo);
		if (uploadedFile != null && uploadedFile.getSize() > 0) {
				manutencao.setComprovante(uploadedFile.getContents());
		}
		if (podeGravar) {
			manutencaoService.salvar(manutencao);
			manutencao = new Manutencao();
			manutencao.setData(new Date());
			uploadedFile = null;
			manutencao.setVeiculo(veiculo);
			manutencoesVeiculo = manutencaoRepository.manutencoesVeiculo(veiculo);
		} else {
			FacesUtil.addErroMessage("A imagem do comprovante excede o tamanho permitido.");
		}

	}

	public BigDecimal getTotalManutencoes() {
		return manutencaoRepository.totalManutencoesPorVeiculo(veiculo);
	}

	public void excluir() {
		manutencaoRepository.remover(manutencaoSelecionada);
		manutencoesVeiculo = manutencaoRepository.manutencoesVeiculo(veiculo);
	}

	public void baixarComprovante(Manutencao manu) {
		String fotoDisco = gerarImagemDisco(manu);
		String nomeDownload = "comprovante_" + manu.getId().toString() + ".jpg";
		try {
			InputStream stream = new BufferedInputStream(new FileInputStream(fotoDisco));
			file = new DefaultStreamedContent(stream, "image/jpg", nomeDownload);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public String gerarImagemDisco(Manutencao manu) {
		manu = manutencaoRepository.porId(manu.getId());
		String local = FacesUtil.localFotos();
		String nome = "comprovante.jpg";
		Path localFoto = FileSystems.getDefault().getPath(local, "temp");
		local = local + File.separator + "temp" + File.separator + nome;
		try {
			Files.createDirectories(localFoto);
			Path fotoTemp = localFoto.resolve(nome);
			Files.write(fotoTemp, manu.getComprovante());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return local;
	}
}
