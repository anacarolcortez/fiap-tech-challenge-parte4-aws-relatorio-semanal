package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.WeeklyReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WeeklyReportRepository implements PanacheRepository<WeeklyReport> {

    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportRepository.class);

    public Double mediaAvaliacoesUltimaSemana() {
        logger.info("Consultando a média de avaliações da última semana");

        return find(
                "dataEnvio >= ?1",
                Instant.now().minus(7, ChronoUnit.DAYS)
        ).stream()
                .mapToDouble(f -> f.nota)
                .average()
                .orElse(0.0);
    }

    public Map<String, Long> quantidadePorUrgencia() {
        logger.info("Calculando a quantidade de avaliações por tipo de urgência");

        return find(
                "dataEnvio >= ?1",
                Instant.now().minus(7, ChronoUnit.DAYS)
        ).stream()
                .collect(Collectors.groupingBy(
                        f -> f.urgencia.name(),
                        Collectors.counting()
                ));
    }

    public List<String> comentariosUltimaSemana() {
        logger.info("Consultando todas as avaliações da última semana");

        return find(
                "dataEnvio >= ?1",
                Instant.now().minus(7, ChronoUnit.DAYS)
        ).stream()
                .map(f -> f.descricao)
                .toList();
    }

    public Double mediaAvaliacoesPorDia() {
        logger.info("Calculando a média de avaliações por dia");

        long total = count(
                "dataEnvio >= ?1",
                Instant.now().minus(7, ChronoUnit.DAYS)
        );
        return total / 7.0;
    }
}
