package com.mastermind.MasterMind.repository;

import com.mastermind.MasterMind.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);

    //CONSULTA PARA VERIFICAR SI EXISTE UN TURNO , Y NO SE SOLAPEN.
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE " +
           "(:id IS NULL OR a.id != :id) AND " +
            "(a.startTime < :endTime AND a.endTime > :startTime)")
    boolean existsOverlappingAppointment(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("id") Long id);



}
