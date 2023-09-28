package com.example.storyteller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Attribute")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "attribute")
public class Attribute {
    @Id
    @SequenceGenerator(
            name = "attribute_sequence",
            sequenceName = "attribute_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attribute_sequence"
    )
    private Long attributeId;

    @Column(
            name = "purpose",
            insertable = false,
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String purpose;

    @Column(name = "emotion")
    private String emotion;
}
