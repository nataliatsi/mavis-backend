package com.nataliatsi.mavis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportantExam {

    private String type;

    private LocalDate date;

    @Column(name = "summary_result")
    private String summaryResult;
}

