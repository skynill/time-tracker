package com.skynill.timetracker.domain.timeentry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_entries")
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_entries_seq")
    @SequenceGenerator(name = "time_entries_seq", sequenceName = "time_entries_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "timestamp_value", nullable = false, updatable = false)
    private Instant timestamp;

    @Version
    private Long version;

}
