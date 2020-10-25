package br.com.sis.service;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;


public class FotoService implements Serializable {

	private static final long serialVersionUID = 1L;

	private Path diretorioRaiz;
	private Path diretorioRaizTemp;

	@PostConstruct
	void init() {
		Path raizAplicacao = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".pedweb");

		diretorioRaiz = raizAplicacao.resolve("fotos-funcionario");
		diretorioRaizTemp = FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir"), "pedweb-fotos-temp");

		try {
			Files.createDirectories(diretorioRaiz);
			Files.createDirectories(diretorioRaizTemp);
		} catch (IOException e) {
			throw new RuntimeException("Problema ao criar diretórios de fotos", e);
		}
	}

	public String salvarFotoTemp(String nome, byte[] conteudo) throws IOException {
		String novoNome = renomarArquivo(nome);
		Path fotoTemp = diretorioRaizTemp.resolve(novoNome);

		Files.write(fotoTemp, conteudo);

		return novoNome;

	}

	private String renomarArquivo(String original) {
		return UUID.randomUUID().toString() + "___" + original;
	}

	public void deletar(String nome) throws IOException {
		deletar(diretorioRaiz, nome);
	}

	public void deletarTemp(String nome) throws IOException {
		deletar(diretorioRaizTemp, nome);
	}

	private void deletar(Path raiz, String nome) throws IOException {
		if (StringUtils.isEmpty(nome)) {
			return;
		}

		Path foto = raiz.resolve(nome);

		if (Files.exists(foto)) {
			Files.delete(foto);
		}
	}

	public void recuperarFotoTemporaria(String nome) throws IOException {
		if (StringUtils.isEmpty(nome)) {
			return;
		}
		Path fotoTemp = diretorioRaizTemp.resolve(nome);
		if (!Files.exists(fotoTemp)) {
			return;
		}

		byte[] conteudo = Files.readAllBytes(fotoTemp);
		Path foto = diretorioRaiz.resolve(nome);
		Files.write(foto, conteudo);

		Files.delete(fotoTemp);
	}

	public byte[] recuperar(String nome) throws IOException {
		Path foto = diretorioRaizTemp.resolve(nome);
		if (Files.exists(foto)) {
			return Files.readAllBytes(foto);
		}

		foto = diretorioRaiz.resolve(nome);

		if (Files.exists(foto)) {
			return Files.readAllBytes(foto);
		}

		throw new RuntimeException("Não encontrada imagem " + nome);

	}
}
