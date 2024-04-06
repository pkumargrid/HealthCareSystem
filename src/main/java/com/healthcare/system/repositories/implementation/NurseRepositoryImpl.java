package com.healthcare.system.repositories.implementation;

import com.healthcare.system.entities.HealthRecord;
import com.healthcare.system.entities.Nurse;
import com.healthcare.system.entities.Patient;
import com.healthcare.system.repositories.NurseRepository;

import java.util.ArrayList;
import java.util.List;

public class NurseRepositoryImpl implements NurseRepository {
    List<Nurse> nurseList;
    public NurseRepositoryImpl() {
        this.nurseList = new ArrayList<>();
    }
    @Override
    public Nurse findById(int id) {
        return nurseList.stream().filter(nurse -> nurse.getId()==id).findFirst().get();
    }

    @Override
    public List<Nurse> findAll() {
        return nurseList;
    }

    @Override
    public void saveNurse(Nurse nurse) {
        if(nurseList.contains(nurse)) {
            updateNurse(nurse);
            return;
        }
        nurseList.add(nurse);
    }

    @Override
    public void updateNurse(Nurse nurse) {
        return;
    }

    @Override
    public void deleteNurseById(int id) {
        Nurse nurse = nurseList.stream().filter(nurse1 -> nurse1.getId() == id).findFirst().get();
        nurseList.remove(nurse);
    }

    @Override
    public HealthRecord accessPatientRecord(Patient patient) {
        return null;
    }

}
