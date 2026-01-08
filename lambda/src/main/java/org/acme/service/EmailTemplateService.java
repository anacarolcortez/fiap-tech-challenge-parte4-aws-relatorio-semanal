package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.WeeklyReportDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class EmailTemplateService {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String gerarTexto(WeeklyReportDTO r) {

        return """
            Relatório semanal de feedbacks

            Data de geração: %s

            Avaliação média: %.2f
            
            Feedbacks por dia: %.2f

            Quantidade de avaliações por urgência:
            %s

            Justificativa da nota pelos alunos:
            %s
            """.formatted(
                r.geradoEm().format(formatter),
                r.mean(),
                r.ammountPerDay(),
                formatarUrgencias(r.amountPerUrgency()),
                formatarComentarios(r.coments())
        );
    }

    private String formatarUrgencias(Map<String, Long> map) {
        return map.entrySet().stream()
                .map(e -> "- %s: %d".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
    }

    private String formatarComentarios(List<String> comentarios) {
        return comentarios.stream()
                .map(c -> "- " + c)
                .collect(Collectors.joining("\n"));
    }
}

