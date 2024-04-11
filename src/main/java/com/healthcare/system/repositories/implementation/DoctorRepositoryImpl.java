package com.healthcare.system.repositories.implementation;

import com.healthcare.system.entities.Doctor;
import com.healthcare.system.exceptions.WrongCredentials;
import com.healthcare.system.repositories.DoctorRepository;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepositoryImpl implements DoctorRepository {

    List<Doctor> doctorList;

    public DoctorRepositoryImpl() {
        doctorList = new ArrayList<>();
    }

    @Override
    public void save(Doctor doctor) throws WrongCredentials {
        if(doctorList.contains(doctor)) {
            update(doctor);
            return;
        }
        doctorList.add(doctor);
    }

    @Override
    public Doctor getById(int id) {
        return doctorList.stream().filter(doctor -> doctor.getId() == id).findFirst().orElseGet(() -> null);
    }

    @Override
    public Doctor deleteById(int id) throws WrongCredentials {
        Doctor doctor = doctorList.stream().filter(d -> d.getId() == id).findFirst().orElseGet(() -> null);
        if(doctor == null) {
            throw new WrongCredentials("doctor with id " + id + " does not exist");
        }
        doctorList.remove(doctor);
        return doctor;
    }

    @Override
    public List<Doctor> getByName(String name) {
        return doctorList.stream().filter(d -> d.getName().equals(name)).toList();
    }

    @Override
    public void update(Doctor doctor) throws WrongCredentials {
        Doctor prevDoctor = doctorList.stream().filter(d -> d.getId() == doctor.getId()).findFirst().orElseGet(() -> null);
        if(prevDoctor == null) {
            throw new WrongCredentials("doctor with id " + doctor.getId() + " does not exist");
        }
        prevDoctor.setEmail(doctor.getEmail());
        prevDoctor.setName(doctor.getName());
        prevDoctor.setAppointmentList(doctor.getAppointmentList());
        prevDoctor.setPassword(doctor.getPassword());
        prevDoctor.setComplaintList(doctor.getComplaintList());
        prevDoctor.setHealthProvider(doctor.getHealthProvider());
        prevDoctor.setPatientList(doctor.getPatientList());
    }

    @Override
    public List<Doctor> findAll() {
        return doctorList.stream().toList();
    }
}
