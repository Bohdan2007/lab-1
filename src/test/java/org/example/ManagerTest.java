package org.example;

import org.example.enums.Specialization;
import org.example.enums.patientStatus;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    @Test
    void testVerificationSuccess() {
        // Arrange
        String name = "Богдан";
        String pass = "admin123";
        Manager m = new Manager();

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Mockito.when(mockedInfo.getPassword()).thenReturn(pass);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(name, mockedInfo);

        try {
            // Act
            boolean result = m.verification(name, pass);

            // Assert
            assertTrue(result);
        } finally {
            Hospital.MapDoctors.clear();
        }
    }
    @Test
    void testFindDoctorSuccess() {
        // Arrange
        Specialization spec = Specialization.SURGEON;
        String docName = "Мешкова Катерина";
        Manager m = new Manager();

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Mockito.when(mockedInfo.getSpecialization()).thenReturn(spec);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(docName, mockedInfo);

        try (MockedConstruction<Doctor> mockedDoctor = Mockito.mockConstruction(Doctor.class,
                (mock, context) -> {
                    Mockito.when(mock.startShift()).thenReturn(true);
                })) {

            // Act
            String result = m.findDoctor(spec);

            // Assert
            assertEquals(docName, result);
        } finally {
            Hospital.MapDoctors.clear();
        }
    }
    @Test
    void testCreateDoctorDuplicate() {
        // Arrange
        String name = "Богдан";
        Manager m = new Manager();
        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(name, Mockito.mock(DoctorInfo.class));

        // Act
        boolean result = m.createDoctor(name, "pass", null, Specialization.OCULIST);

        // Assert
        assertFalse(result);
    }
    @Test
    void testCreatePatientSuccess() {
        // Arrange
        String docName = "Богдан";
        String patName = "Пацієнт";
        Manager m = new Manager();

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Map<String, patientStatus> patients = new HashMap<>();
        Mockito.when(mockedInfo.getPatients()).thenReturn(patients);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(docName, mockedInfo);

        // Act
        boolean result = m.createPatient(patName, docName);

        // Assert
        assertTrue(result);
    }
    @Test
    void testDeleteDoctorSuccess() {
        // Arrange
        String name = "Богдан";
        Manager m = new Manager();
        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(name, Mockito.mock(DoctorInfo.class));

        // Act
        boolean result = m.deleteDoctor(name);

        // Assert
        assertTrue(result);
        assertFalse(Hospital.MapDoctors.containsKey(name));
    }
    @Test
    void testDeletePatientSuccess() {
        // Arrange
        String docName = "Мешкова Катерина";
        String patName = "Іван Іванов";
        Manager m = new Manager();

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Map<String, patientStatus> patients = new HashMap<>();
        patients.put(patName, patientStatus.SICK);
        Mockito.when(mockedInfo.getPatients()).thenReturn(patients);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(docName, mockedInfo);

        // Act
        boolean result = m.deletePatient(patName, docName);

        // Assert
        assertTrue(result);
    }
    @Test
    void testSaveData() throws IOException {
        // Arrange
        String fileName = "test_save.txt";
        Manager m = new Manager();
        Hospital.MapDoctors.clear();

        LocalTime[] schedule = {LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0)};
        DoctorInfo info = new DoctorInfo("pass123", schedule, Specialization.SURGEON);
        info.getPatients().put("Богдан", patientStatus.SICK);

        Hospital.MapDoctors.put("Лікар Тест", info);

        try {
            // Act
            m.saveData(Hospital.MapDoctors, fileName);

            // Assert
            File file = new File(fileName);
            assertTrue(file.exists());
        } finally {
            new File(fileName).delete();
            Hospital.MapDoctors.clear();
        }
    }
    @Test
    void testLoadData() throws IOException {
        // Arrange
        String fileName = "test_load.txt";
        Manager m = new Manager();
        Hospital.MapDoctors.clear();

        String testLine = "Мешкова|1234|TRAUMATOLOGIST|STANDARD_SHIFT|Богдан/NONE\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(testLine);
        }

        try {
            // Act
            m.loadData(fileName);

            // Assert
            assertTrue(Hospital.MapDoctors.containsKey("Мешкова"));
            DoctorInfo info = Hospital.MapDoctors.get("Мешкова");
            assertEquals("1234", info.getPassword());
            assertEquals(Specialization.TRAUMATOLOGIST, info.getSpecialization());
            assertTrue(info.getPatients().containsKey("Богдан"));

        } finally {
            new File(fileName).delete();
            Hospital.MapDoctors.clear();
        }
    }
}
