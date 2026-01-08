package org.acme.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.quarkus.arc.Arc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeeklyReportHandler implements RequestHandler<Object, String> {

    private static final Logger logger = LoggerFactory.getLogger(WeeklyReportHandler.class);

    @Override
    public String handleRequest(Object input, Context context) {
        logger.info("Acionando a lambda para publicação do relatório semanal");
        return Arc.container()
                .instance(WeeklyReportJob.class)
                .get()
                .executar();
    }
}