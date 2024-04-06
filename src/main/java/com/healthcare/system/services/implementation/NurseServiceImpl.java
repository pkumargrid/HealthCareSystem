package com.healthcare.system.services.implementation;

import com.healthcare.system.entities.HealthRecord;
import com.healthcare.system.entities.Nurse;
import com.healthcare.system.entities.Patient;
import com.healthcare.system.repositories.NurseRepository;
import com.healthcare.system.services.NurseService;

import java.util.List;

public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public Nurse findById(int id) {
        return nurseRepository.findById(id);
    }

    @Override
    public List<Nurse> findAll() {
        return nurseRepository.findAll();
    }

    @Override
    public void saveNurse(Nurse nurse) {
        nurseRepository.saveNurse(nurse);
    }

    @Override
    public void updateNurse(Nurse nurse) {
        nurseRepository.updateNurse(nurse);
    }

    @Override
    public void deleteNurseById(int id) {
        nurseRepository.deleteNurseById(id);
    }

    @Override
    public HealthRecord accessPatientRecord(Patient patient) {
        return nurseRepository.accessPatientRecord(patient);
    }

}
