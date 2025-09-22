package com.seuorg.sgpe.config;

import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean BCryptPasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean
  UserDetailsService userDetailsService(UsuarioRepo repo){
    return username -> {
      Usuario u = repo.findByLogin(username)
          .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));
      String role = "ROLE_" + u.getPerfil().name();
      return User.withUsername(u.getLogin())
          .password(u.getSenhaHash())
          .roles(role.replace("ROLE_",""))
          .build();
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health").permitAll()
            .requestMatchers("/api/usuarios/**").hasRole(Perfil.ADMINISTRADOR.name())
            .requestMatchers("/api/projetos/**").hasAnyRole(Perfil.ADMINISTRADOR.name(), Perfil.GERENTE.name())
            .requestMatchers("/api/equipes/**").hasAnyRole(Perfil.ADMINISTRADOR.name(), Perfil.GERENTE.name())
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
