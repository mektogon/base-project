package ru.dorofeev.database.repository.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dorofeev.database.entity.Job;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

}
