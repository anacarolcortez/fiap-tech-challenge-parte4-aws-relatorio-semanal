package org.acme.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record WeeklyReportDTO(
        double mean,
        double ammountPerDay,
        Map<String, Long> amountPerUrgency,
        List<String> coments,
        LocalDateTime geradoEm
) {
}
