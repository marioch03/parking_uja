package com.parking.backend.repository;

import com.parking.backend.model.plaza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlazaRepository extends JpaRepository<plaza, Integer> {

}

