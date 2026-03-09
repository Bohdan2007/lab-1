package org.example;

import org.example.enums.Specialization;
import org.example.enums.patientStatus;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientTest {
    @Test
    void testSignUpForQueueSuccess() {
        // Arrange
        String patientName = "Богдан";
        String docName = "Мешкова Катерина";
        Specialization neededSpec = Specialization.SURGEON;

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Map<String, patientStatus> patients = new HashMap<>();
        Mockito.when(mockedInfo.getPatients()).thenReturn(patients);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(docName, mockedInfo);

        try (MockedConstruction<Manager> mockedManager = Mockito.mockConstruction(Manager.class,
                (mock, context) -> {
                    Mockito.when(mock.findDoctor(neededSpec)).thenReturn(docName);
                })) {

            // Act
            Patient p = new Patient(patientName);
            String result = p.signUpForQueue(neededSpec);

            // Assert
            assertEquals(docName, result);
        } finally {
            Hospital.MapDoctors.clear();
        }
    }
    @Test
    void testSignUpForQueueNotFound() {
        // Arrange
        String patientName = "Богдан";
        Specialization neededSpec = Specialization.OCULIST;

        Hospital.MapDoctors.clear();

        try (MockedConstruction<Manager> mockedManager = Mockito.mockConstruction(Manager.class,
                (mock, context) -> {
                    Mockito.when(mock.findDoctor(neededSpec)).thenReturn(null);
                })) {

            // Act
            Patient p = new Patient(patientName);
            String result = p.signUpForQueue(neededSpec);

            // Assert
            assertEquals(null, result);
        } finally {
            Hospital.MapDoctors.clear();
        }
    }
}
