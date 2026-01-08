package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.dto.WeeklyReportDTO;
import org.acme.repository.WeeklyReportRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WeeklyReportService {

    @Inject
    WeeklyReportRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportService.class);

    public WeeklyReportDTO gerarRelatorio() {

        logger.info("Gerando relatório semanal");

        double media = repository.mediaAvaliacoesUltimaSemana();
        double porDia = repository.mediaAvaliacoesPorDia();
        Map<String, Long> porUrgencia = repository.quantidadePorUrgencia();
        List<String> comentarios = repository.comentariosUltimaSemana();
        LocalDateTime dataGeracao = LocalDateTime.now();

        logger.info("Relatório semanal gerado com sucesso");

        return new WeeklyReportDTO(media, porDia, porUrgencia, comentarios, dataGeracao);
    }
}
