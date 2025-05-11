package ru.dorofeev.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dorofeev.database.entity.CommonSetting;

import java.util.UUID;

@Repository
public interface CommonSettingRepository extends JpaRepository<CommonSetting, UUID> {

}
