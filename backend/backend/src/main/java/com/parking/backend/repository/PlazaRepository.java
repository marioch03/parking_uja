package com.parking.backend.repository;

import com.parking.backend.model.Plaza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlazaRepository extends JpaRepository<Plaza, Integer> {

}

