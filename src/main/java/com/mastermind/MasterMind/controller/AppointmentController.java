package com.mastermind.MasterMind.controller;

import com.mastermind.MasterMind.dto.AppointmentDTO;
import com.mastermind.MasterMind.model.Appointment;
import com.mastermind.MasterMind.model.Patient;
import com.mastermind.MasterMind.repository.AppointmentRepository;
import com.mastermind.MasterMind.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO dto) {

        Optional<Patient> patientOptional = patientRepository.findById((dto.getPatientId()));

        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El paciente " + dto.getPatientId() + " no existe.");
        }

        //VALIDACION DE HORARIOS OCUPADOS
        boolean isOverlapping = appointmentRepository.existsOverlappingAppointment(
                dto.getStartTime(),
                dto.getEndTime(),
                null);

        if (isOverlapping){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El Horario seleccionado ya se encuentra ocupado por otro");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patientOptional.get());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setStatus(dto.getStatus() != null ? dto.getStatus() : "Pendiente");

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(){
            return ResponseEntity.ok(appointmentRepository.findAll());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId){
        return ResponseEntity.ok(appointmentRepository.findByPatientId(patientId));
    }

    @PutMapping("/{id}") // es id porque corresponde al turno(appointment)
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO dto ){
        // EL TURNO EXISTE ?
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El turno con ID " + id + " no existe.");
        }
        Optional<Patient> patientOptional = patientRepository.findById(dto.getPatientId());
        if (patientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El paciente con ID " + dto.getPatientId() + " no existe.");
        }

        boolean isOverlapping = appointmentRepository.existsOverlappingAppointment(
                dto.getStartTime(),
                dto.getEndTime(),
                null);

        if (isOverlapping){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El Horario Seleccionado ya se encuentra Ocupado por otro");
        }

        Appointment appointment = appointmentOptional.get();
        appointment.setPatient(patientOptional.get());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }

        Appointment updateAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.ok(updateAppointment);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id){
        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El turno con ID " + id + " no existe.");
        }

        appointmentRepository.deleteById(id);
        return ResponseEntity.ok("El turno con ID " + id + " eliminado correctamente.");

    }



}