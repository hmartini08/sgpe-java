package com.seuorg.sgpe.config;

import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  // Encoder para senhas armazenadas com BCrypt (como a sua coluna "senha")
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // Carrega usuário do banco e entrega ao Spring Security
  @Bean
  public UserDetailsService userDetailsService(UsuarioRepo repo) {
    return username -> {
      Usuario u = repo.findByLogin(username)
          .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));
      // a coluna "senha" já está com hash BCrypt
      String role = u.getPerfil().name(); // ADMINISTRADOR, GERENTE, COLABORADOR
      return User.withUsername(u.getLogin())
          .password(u.getSenha())
          .roles(role) // Spring vai prefixar com ROLE_
          .build();
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // para facilitar testes com formulário simples
        .authorizeHttpRequests(auth -> auth
            // Libera arquivos estáticos e páginas públicas
            .requestMatchers(
                "/", "/index.html",
                "/login.html",
                "/css/**", "/js/**", "/images/**", "/webjars/**",
                "/actuator/health")
            .permitAll()
            // APIs protegidas por perfil
            .requestMatchers("/api/usuarios/**").hasRole(Perfil.ADMINISTRADOR.name())
            .requestMatchers("/api/projetos/**").hasAnyRole(Perfil.ADMINISTRADOR.name(), Perfil.GERENTE.name())
            .requestMatchers("/api/equipes/**").hasAnyRole(Perfil.ADMINISTRADOR.name(), Perfil.GERENTE.name())
            // qualquer outra rota requer usuário autenticado
            .anyRequest().authenticated())
        // Ativa formulário de login próprio
        .formLogin(form -> form
            .loginPage("/login.html") // sua tela
            .loginProcessingUrl("/login") // action do <form>
            .defaultSuccessUrl("/index.html", true) // para onde redireciona após logar
            .failureUrl("/login.html?error=true") // mensagem simples na tela
            .permitAll())
        // Logout simples
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login.html?logout=true")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll());
    return http.build();
  }
}
