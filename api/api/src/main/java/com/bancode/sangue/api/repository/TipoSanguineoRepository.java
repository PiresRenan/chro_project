package com.bancode.sangue.api.repository;

import com.bancode.sangue.api.models.TipoSanguineo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoSanguineoRepository extends JpaRepository<TipoSanguineo, String> {
}

