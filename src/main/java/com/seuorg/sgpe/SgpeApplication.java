package com.seuorg.sgpe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.seuorg.sgpe.domain.Perfil;
import com.seuorg.sgpe.domain.Usuario;
import com.seuorg.sgpe.repo.UsuarioRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SgpeApplication {

  public static void main(String[] args) {
    SpringApplication.run(SgpeApplication.class, args);
  }

  // Seeder para criar admin inicial
  @Bean
  CommandLineRunner seedAdmin(UsuarioRepo repo, PasswordEncoder encoder) {
    return args -> {
      if (repo.count() == 0) {
        Usuario admin = new Usuario();
        admin.setNomeCompleto("Admin Master");
        admin.setCpf("00011122233");
        admin.setEmail("admin@mail.com");
        admin.setCargo("CTO");
        admin.setLogin("admin");
        admin.setSenha(encoder.encode("admin123")); // BCRYPT
        admin.setPerfil(Perfil.ADMINISTRADOR);
        repo.save(admin);
        System.out.println(">>> ADMIN criado: login=admin / senha=admin123");
      }
    };
  }
}
