package ru.dorofeev.database.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BasicEntitySystemFields {

    @CreationTimestamp
    @Column(name = "create_date_time", nullable = false)
    private OffsetDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_date_time", nullable = false)
    private OffsetDateTime updateDateTime;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
