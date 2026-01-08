package org.acme.lambda;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import io.quarkus.arc.Unremovable;
import org.acme.dto.WeeklyReportDTO;
import org.acme.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Unremovable
public class WeeklyReportJob {

    @Inject
    WeeklyReportService reportService;

    @Inject
    NotificationService notificationService;

    @Inject
    EmailTemplateService sns;

    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportJob.class);

    @Transactional
    public String executar() {
        WeeklyReportDTO relatorio = reportService.gerarRelatorio();

        String assunto = "Relatório semanal de feedback dos alunos";
        String mensagem = sns.gerarTexto(relatorio);

        notificationService.publicar(assunto, mensagem);

        logger.info("Relatório semanal gerado e publicado no SNS");

        return "Relatório semanal gerado e publicado no SNS";
    }
}
