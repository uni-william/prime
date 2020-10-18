package br.com.sis.enuns;

public enum Funcionalidade {
	
	CONF_MAIL("Configuração E-mail"),	
	PERFIL_ACESS("Perfil - Acesso"),
	PERFIL_INSERT("Perfil - Inserir"),
	PERFIL_EDIT("Perfil - Editar"),
	PERFIL_DEL("Perfil - Excluir"),
	FUNCIONARIO_ACESS("Funcionário - Acesso"),
	FUNCIONARIO_INSERT("Funcionário - Inserir"),
	FUNCIONARIO_EDIT("Funcionário - Editar"),
	FUNCIONARIO_DEL("Funcionário - Excluir"),	
	USUARIO_ACESS("Usuário - Acesso"),
	USUARIO_INSERT("Usuário - Inserir"),
	USUARIO_EDIT("Usuário - Editar"),
	USUARIO_DEL("Usuário - Excluir"),
	CLIENTE_ACESS("Cliente - Acesso"),
	CLIENTE_INSERT("Cliente - Inserir"),
	CLIENTE_EDIT("Cliente - Editar"),
	CLIENTE_DEL("Cliente - Excluir"),	
	FABRICANTE_ACESS("Fabricante/Modelos - Acesso"),
	FABRICANTE_INSERT("Fabricante/Modelos - Inserir"),
	FABRICANTE_EDIT("Fabricante/Modelos - Editar"),
	FABRICANTE_DEL("Fabricante/Modelos - Excluir"),
	ACESSORIO_ACESS("Acessório - Acesso"),
	ACESSORIO_INSERT("Acessório - Inserir"),
	ACESSORIO_EDIT("Acessório - Editar"),
	ACESSORIO_DEL("Acessório - Excluir"),
	VEICULO_ACESS("Veículo - Acesso"),
	VEICULO_INSERT("Veículo - Inserir"),
	VEICULO_EDIT("Veículo - Editar"),
	VEICULO_DEL("Veículo - Excluir"),
	BANCO_ACESS("Banco - Acesso"),
	BANCO_INSERT("Banco - Inserir"),
	BANCO_EDIT("Banco - Editar"),
	BANCO_DEL("Banco - Excluir"),	
	MOVIMENTACOES("Compras/Vendas realizadas"),
	VENDA("Nova Venda"),
	COMPRA("Nova Compra"),
	PARCEIRO_ACESS("Parceiros"),
	VEICULO_PARC_ACESS("Veículos de Parceiros"),
	ITERMEDIACAO_ACESS("Intermediações"),
	VEICULO_ALUG_ACESS("Aluguel - Veículos"),
	ALUGUEL_VEICULO("Aluguel - Realizar Aluguel de Veículos"),
	ALUGUEL_RENOVACAO("Aluguel - Renovação"),
	ALUGUEL_DEVOLUCAO("Aluguel - Devolução"),
	ALUGUEL_SEMANAL("Aluguel - Pagamento Semanal"),
	ALUGUEL_LISTA("Aluguel - Contratos de Aluguel"),
	ALUGUEL_CHECKLIST("Aluguel - ChekcList"),
	BACKUP_ACESS("Backup - Efetuar backup"),
	DESPESA_ACESS("Despesa - Acesso"),
	DESPESA_INSERT("Despesa - Inserir"),
	DESPESA_EDIT("Despesa - Editar"),
	DESPESA_DEL("Despesa - Excluir"),
	TIPO_DESPESA_ACESS("Tipo de despesa - Acesso"),
	TIPO_DESPESA_INSERT("Tipo de despesa - Inserir"),
	TIPO_DESPESA_EDIT("Tipo de despesa - Editar"),
	TIPO_DESPESA_DEL("Tipo de despesa - Excluir");	

	Funcionalidade(String descricao) {
		this.descricao = descricao;
	}
		private String descricao;
	
	public String getDescricao() {
		return descricao;
	}	


}
