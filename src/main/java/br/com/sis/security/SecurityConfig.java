package br.com.sis.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.sis.util.cdi.CDIServiceLocator;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public AppUserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		AppUserDetailsService appUserDetailsService = CDIServiceLocator.getBean(AppUserDetailsService.class);
		auth.userDetailsService(appUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
		jsfLoginEntry.setLoginFormUrl("/Login.xhtml");
		jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());
		
		JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
		jsfDeniedEntry.setLoginPath("/AcessoNegado.xhtml");
		jsfDeniedEntry.setContextRelative(true);
		
		http
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
			.and()
			
		.authorizeRequests()
			.antMatchers("/Login.xhtml", "/Erro.xhtml", "/EsqueciSenha.xhtml", "/javax.faces.resources/**").permitAll()
			.antMatchers("/Home.xhtml", "/AcessoNegado.xhtml", "/TrocaSenha.xhtml", "/DashBoard.xhtml", "/consultas/**").authenticated()
			.antMatchers("/configuracoes/**").hasAnyRole("CONF_MAIL")
			.antMatchers("/funcionarios/**").hasAnyRole("FUNCIONARIO_ACESS")
			.antMatchers("/clientes/**").hasAnyRole("CLIENTE_ACESS")
			.antMatchers("/perfis/**").hasAnyRole("PERFIL_ACESS")
			.antMatchers("/usuarios/**").hasAnyRole("USUARIO_ACESS")
			.antMatchers("/fabricantes/**").hasAnyRole("FABRICANTE_ACESS")
			.antMatchers("/acessorios/**").hasAnyRole("ACESSORIO_ACESS")
			.antMatchers("/tipos/**").hasAnyRole("TIPO_DESPESA_ACESS")
			.antMatchers("/depesas/**").hasAnyRole("DESPESA_ACESS")
			.antMatchers("/veiculos/**").hasAnyRole("VEICULO_ACESS")
			.antMatchers("/vendedores/**").hasAnyRole("VENDEDOR_ACESS")
			.antMatchers("/bancos/**").hasAnyRole("BANCO_ACESS")
			.antMatchers("/parceiros/**").hasAnyRole("PARCEIRO_ACESS")
			.antMatchers("/veiculos_parceiros/**").hasAnyRole("VEICULO_PARC_ACESS")
			.antMatchers("/intermediacoes/**").hasAnyRole("ITERMEDIACAO_ACESS")			
			.antMatchers("/movimentacoes/PesquisaMovimentacoes.xhtml").hasAnyRole("MOVIMENTACOES")
			.antMatchers("/alugueis/PesquisaVeiculosAluguel.xhtml").hasAnyRole("ALUGUEL_LISTA")
			.antMatchers("/alugueis/Aluguel.xhtml").hasAnyRole("ALUGUEL_VEICULO")
			.antMatchers("/alugueis/ListaAlugueis.xhtml").hasAnyRole("ALUGUEL_LISTA")
			.antMatchers("/checklists/PesquisaCheckLists.xhtml").hasAnyRole("ALUGUEL_CHECKLIST")
			.antMatchers("/alugueis/Renovacao.xhtml").hasAnyRole("ALUGUEL_RENOVACAO")
			.antMatchers("/alugueis/Devolucao.xhtml").hasAnyRole("ALUGUEL_DEVOLUCAO")
			.antMatchers("/alugueis/PagamentoSemanal.xhtml").hasAnyRole("ALUGUEL_SEMANAL")
			.antMatchers("/relatorios/RelatorioMovimentacoes.xhtml").hasAnyRole("REL_MOVIMENTACOES")
			.antMatchers("/relatorios/RelatorioResumoComissao.xhtml").hasAnyRole("REL_COMISSAO")
			.antMatchers("/relatorios/RelatorioResumoDespesas.xhtml").hasAnyRole("REL_DESPESAS")
			.antMatchers("/relatorios/RelatorioFluxoCaixa.xhtml").hasAnyRole("REL_FLUXO_CAIXA")
			.antMatchers("/relatorios/RelatorioVendasPeriodo.xhtml").hasAnyRole("REL_VEIC_VEND")
			.antMatchers("/consultas/ConsultaResumoVendas.xhtml").hasAnyRole("REL_VEIC_VEND")
			.and()
		.formLogin()
			.loginPage("/Login.xhtml")
			.failureUrl("/Login.xhtml?invalid=true")
			.and()
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.and()
			
		.exceptionHandling()
			.accessDeniedPage("/AcessoNegado.xhtml")
			.authenticationEntryPoint(jsfLoginEntry)
			.accessDeniedHandler(jsfDeniedEntry);
	}	
}

