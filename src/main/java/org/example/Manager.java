package org.example;

import org.example.enums.Specialization;
import org.example.enums.patientStatus;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

import static org.example.Hospital.*;

public class Manager {
    private final String SET = "|";
    private final String SET_PATIENT = "/";

    public boolean verification(String name, String password){
        for(Map.Entry<String, DoctorInfo> i : MapDoctors.entrySet()){
            if(name.equals(i.getKey()) && password.equals(i.getValue().getPassword())){
                return true;
            }
        }
        return false;
    }

    public String findDoctor(Specialization needSpecialization) {
        for (Map.Entry<String, DoctorInfo> item : MapDoctors.entrySet()) {
            if(item.getValue().getSpecialization() == needSpecialization){
                Doctor doctor = new Doctor(item.getKey());
                if(doctor.startShift()){
                    return item.getKey();
                }
            }
        }

        return null;
    }

    public boolean createDoctor(String name, String password, LocalTime[] schedule, Specialization specialization) {
        if (MapDoctors.containsKey(name)) {
            return false;
        }

        MapDoctors.put(name, new DoctorInfo(password, schedule, specialization));
        return true;
    }

    public String readAllDoctors() {
        if (MapDoctors.isEmpty()) {
            return "База лікарів порожня.";
        }

        String allDoctors = "";
        for(Map.Entry<String, DoctorInfo> doctor : MapDoctors.entrySet()){
            allDoctors += doctor.getKey() + "\n";
        }

        return allDoctors;
    }

    public boolean updateDoctor(String name, String newPassword, LocalTime[] newSchedule, Specialization newSpec) {
        if (!MapDoctors.containsKey(name)) {
            return false;
        }

        DoctorInfo updatedDoctor = new DoctorInfo(newPassword, newSchedule, newSpec);
        updatedDoctor.getPatients().putAll(MapDoctors.get(name).getPatients());

        MapDoctors.put(name, updatedDoctor);
        return true;
    }

    public boolean deleteDoctor(String name) {
        if (!MapDoctors.containsKey(name)) {
            return false;
        }

        MapDoctors.remove(name);
        return true;
    }

    public boolean createPatient(String patientName, String doctorName) {
        if (!MapDoctors.containsKey(doctorName)) {
            return false;
        }

        MapDoctors.get(doctorName).getPatients().put(patientName, patientStatus.SICK);
        return true;
    }

    public String readPatient(String patientName) {
        for (Map.Entry<String, DoctorInfo> entry : MapDoctors.entrySet()) {
            if (entry.getValue().getPatients().containsKey(patientName)) {
                return "Пацієнт: " + patientName + "\nЗаписаний до: " + entry.getKey();
            }
        }

        return "Пацієнта з таким іменем не знайдено";
    }

    public boolean updatePatient(String patientName, String newDoctorName) {
        if (!MapDoctors.containsKey(newDoctorName)) {
            return false;
        }

        String oldDoctorName = null;
        for (Map.Entry<String, DoctorInfo> entry : MapDoctors.entrySet()) {
            if (entry.getValue().getPatients().containsKey(patientName)) {
                oldDoctorName = entry.getKey();
                break;
            }
        }

        if(oldDoctorName == null){
            return false;
        }

        MapDoctors.get(oldDoctorName).getPatients().remove(patientName);
        MapDoctors.get(newDoctorName).getPatients().put(patientName, patientStatus.SICK);
        return true;
    }

    public boolean deletePatient(String patientName, String doctorName) {
        if (!MapDoctors.containsKey(doctorName) || !MapDoctors.get(doctorName).getPatients().containsKey(patientName)) {
            return false;
        }

        MapDoctors.get(doctorName).getPatients().remove(patientName);
        return true;
    }

    public void saveData(Map<String, DoctorInfo> doctors, String fileName) {
        List<String> listDoctors = new ArrayList<>();
        for(Map.Entry<String, DoctorInfo> item: doctors.entrySet()){
            listDoctors.add(item.getKey());
        }

        listDoctors.sort((e1, e2) -> e1.compareTo(e2));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for(String item : listDoctors){
                String allPatient = "";
                for(Map.Entry<String, patientStatus> patient : doctors.get(item).getPatients().entrySet()){
                    allPatient += patient.getKey() + SET_PATIENT;
                }
                String timeShift = "";
                if(doctors.get(item).getSchedule().equals(standardShift)){
                    timeShift = "STANDARD_SHIFT";
                }else if(doctors.get(item).getSchedule().equals(earlyShift)){
                    timeShift = "EARLY_SHIFRT";
                }else{
                    timeShift = "LONG_BREAK_SHIFT";
                }

                String result = item + SET + doctors.get(item).getPassword()+ SET + doctors.get(item).getSpecialization() + SET + timeShift + SET +allPatient + "\n";
                writer.write(result);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void loadData(String fileName) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null){
                String nameDoctor = null;
                String passwordDoctor = null;
                Specialization specialization = null;
                LocalTime[] schedule = null;
                List<String> pasientDoctor = new ArrayList<>();
                String name = "";
                for(int i = 0; i < line.length(); i++){
                    if(line.charAt(i) != SET.charAt(0) && line.charAt(i) != SET_PATIENT.charAt(0)){
                        name += line.charAt(i);
                    }else {
                        if(nameDoctor == null){
                            nameDoctor = name;
                        } else if (passwordDoctor == null) {
                            passwordDoctor = name;
                        } else if (specialization == null) {
                            switch (name){
                                case "OCULIST":
                                    specialization = Specialization.OCULIST;
                                    break;
                                case "TRAUMATOLOGIST":
                                    specialization = Specialization.TRAUMATOLOGIST;
                                    break;
                                case "SURGEON":
                                    specialization = Specialization.SURGEON;
                                    break;
                            }
                        } else if (schedule == null) {
                            switch (name){
                                case "STANDARD_SHIFT":
                                    schedule = standardShift;
                                    break;
                                case "EARLY_SHIFRT":
                                    schedule = earlyShift;
                                    break;
                                case "LONG_BREAK_SHIFT":
                                    schedule = longBreakShift;
                                    break;
                            }
                        }else{
                            pasientDoctor.add(name);
                        }
                        name = "";
                    }
                }

                MapDoctors.put(nameDoctor, new DoctorInfo(passwordDoctor, schedule, specialization));
                for(int i = 0; i < pasientDoctor.size(); i++){
                    MapDoctors.get(nameDoctor).getPatients().put(pasientDoctor.get(i), patientStatus.SICK);
                }
            }

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}