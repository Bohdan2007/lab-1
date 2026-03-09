package org.example;

import org.example.enums.Specialization;
import org.example.enums.patientStatus;
import java.util.Objects;
import static org.example.Hospital.MapDoctors;

public class Patient {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Patient(String name) { this.name = name; }

    public String signUpForQueue(Specialization neededSpec) {
        Manager m = new Manager();
        String doctorName = m.findDoctor(neededSpec);

        if (doctorName != null) {
            MapDoctors.get(doctorName).getPatients().put(this.name, patientStatus.SICK);
            return doctorName;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Patient patient = (Patient) o;
        return Objects.equals(name, patient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}