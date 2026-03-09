package org.example;

import org.example.enums.Specialization;
import org.example.enums.patientStatus;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DoctorInfo {
    private String password;
    private LocalTime[] schedule;
    private Map<String, patientStatus> patients;
    private Specialization specialization;

    public DoctorInfo(String password, LocalTime[] schedule, Specialization specialization) {
        this.password = password;
        this.schedule = schedule;
        this.specialization = specialization;
        this.patients = new LinkedHashMap<>();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalTime[] getSchedule() { return schedule; }
    public void setSchedule(LocalTime[] schedule) { this.schedule = schedule; }

    public Map<String, patientStatus> getPatients() { return patients; }
    public void setPatients(Map<String, patientStatus> patients) { this.patients = patients; }

    public Specialization getSpecialization(){ return specialization; }
    public void setSpecialization(Specialization specialization) { this.specialization = specialization; }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        DoctorInfo that = (DoctorInfo) o;
        return Objects.equals(password, that.password) && Arrays.equals(schedule, that.schedule) && specialization == that.specialization;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for(byte i = 0; i < password.length(); i++){
            result += password.charAt(i) + i*i;
        }

        return result;
    }
}
