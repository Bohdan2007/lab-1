package org.example;

import org.example.enums.ScheduleEvent;
import org.example.enums.patientStatus;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import static org.example.Hospital.MapDoctors;

public class Doctor implements Comparable<Doctor> {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Doctor(String name) {
        this.name = name;
    }

    public boolean startShift(){

        if (MapDoctors.get(name).getSchedule() == null || MapDoctors.get(name).getSchedule().length < 3) {
            return false;
        }

        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime shiftStart = MapDoctors.get(name).getSchedule()[ScheduleEvent.SHIFT_START.ordinal()];
        LocalTime breakStart = MapDoctors.get(name).getSchedule()[ScheduleEvent.BREAK_START.ordinal()];
        LocalTime breakEnd   = MapDoctors.get(name).getSchedule()[ScheduleEvent.BREAK_END.ordinal()];

        if (now.isBefore(shiftStart)) {
            return false;
        }
        if (now.isAfter(breakStart) && now.isBefore(breakEnd)) {
            return false;
        }

        return true;
    }

    public String healsPatient(){
        Map<String, patientStatus> myPatients = MapDoctors.get(this.name).getPatients();
        for (String patientName : myPatients.keySet()) {
            if (myPatients.get(patientName) == patientStatus.SICK) {
                myPatients.remove(patientName);
                return patientName;
            }
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

        Doctor doctor = (Doctor) o;
        return Objects.equals(name, doctor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public int compareTo(Doctor o){
        String myName = this.name;
        String otherName = o.name;

        int myNameCount = 0;
        int otherNameCount = 0;

        for(int i = 0; i < myName.length(); i++){
            if(myName.charAt(i) == ' '){
                break;
            }
            myNameCount += myName.charAt(i);
        }

        for(int i = 0; i < otherName.length(); i++){
            if(myName.charAt(i) == ' '){
                break;
            }
            otherNameCount += otherName.charAt(i);
        }

        if(myNameCount > otherNameCount){
            return 1;
        } else if (myNameCount < otherNameCount) {
            return -1;
        }

        return 0;
    }
}
