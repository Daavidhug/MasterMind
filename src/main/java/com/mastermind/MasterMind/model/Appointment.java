package com.mastermind.MasterMind.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//nos trae toda la informacion
    @JoinColumn(name="patient_id", nullable=false)
    private Patient patient;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status;

    public Appointment(){}

    public Long getId(){return id;}
    public void setId(Long id){
        this.id = id;
    }

    public Patient getPatient(){
        return patient;
    }
    public void setPatient (Patient patient){
        this.patient = patient;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

}
