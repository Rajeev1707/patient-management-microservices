package com.rajeev.patientservice.service;

import com.rajeev.patientservice.dto.PatientRequestDTO;
import com.rajeev.patientservice.dto.PatientResponseDTO;
import com.rajeev.patientservice.exception.EmailAlreadyExistsException;
import com.rajeev.patientservice.exception.PatientNotFoundException;
import com.rajeev.patientservice.grpc.BillingServiceGrpcClient;
import com.rajeev.patientservice.mapper.PatientMapper;
import com.rajeev.patientservice.model.Patient;
import com.rajeev.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository  patientRepository;
    @Autowired
    private BillingServiceGrpcClient billingServiceGrpcClient;

    public List<PatientResponseDTO> getPatients()
    {
        List<Patient> patients=   patientRepository.findAll();
        List<PatientResponseDTO>  patientResponseDTOs=patients.stream()
                .map(patient-> PatientMapper.toDTO(patient)).toList();
        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail()))
        throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDTO.getEmail());
        Patient newPatient = patientRepository.
                save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBilling(newPatient.getId().toString(),newPatient.getName(),newPatient.getEmail());
        return PatientMapper.toDTO(newPatient);

    }
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient=patientRepository.
                findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with id "+ id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id))
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDTO.getEmail());

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setEmail(patientRequestDTO.getEmail());

Patient updatedPatient = patientRepository.save(patient);
return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }


}
