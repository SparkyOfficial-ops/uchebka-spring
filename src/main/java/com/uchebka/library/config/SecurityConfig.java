package com.uchebka.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Цепочка фильтров стоит перед контроллером. Запрос без права сюда даже не доходит:
 * в логе не будет строки интерцептора, а клиент получит 401.
 *
 * <p>Чтение открыто. POST, PUT и DELETE ждут заголовок Basic с логином {@code student}
 * и паролем {@code student}. Если свой {@code UserDetailsService} не объявить,
 * Boot при старте напечатает случайный пароль пользователя {@code user}.
 *
 * <p>{@code roles("STUDENT")} кладёт в пользователя право {@code ROLE_STUDENT}.
 * Проверка роли выглядела бы как {@code hasRole("STUDENT")} — приставка {@code ROLE_} уже внутри.
 *
 * <p>CSRF выключен, потому что клиент шлёт заголовок Authorization, а не cookie-сессию формы.
 * У сайта с формами и cookie эту защиту оставляют.
 * Консоли H2 нужна возможность встраивать страницу в frame, поэтому для неё разрешён sameOrigin.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails student = User.withUsername("student")
                .password(passwordEncoder.encode("student"))
                .roles("STUDENT")
                .build();
        return new InMemoryUserDetailsManager(student);
    }
}
