package org.acme.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "feedback_entity")
public class WeeklyReport {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    public double nota;

    public String descricao;

    @Enumerated(EnumType.STRING)
    public Urgency urgencia;

    @Column(name = "data_envio")
    private Instant dataEnvio;

}
