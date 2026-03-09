package org.example;

import org.example.enums.patientStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorTest {
    @Test
    void testStartShiftEmptySchedule() {
        //Arrange
        String name = "Мешкова Катерина Сергієвна";

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Mockito.when(mockedInfo.getSchedule()).thenReturn(new LocalTime[0]);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(name, mockedInfo);

        //Act
        Doctor doc = new Doctor(name);
        boolean result = doc.startShift();

        //Assert
        assertFalse(result);
    }
    @Test
    void testHealsPatientSuccess() {
        //Assert
        String docName = "Богдан Лікар";
        String patientName = "Іван Іванов";

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Map<String, patientStatus> patients = new HashMap<>();
        patients.put(patientName, patientStatus.SICK);
        Mockito.when(mockedInfo.getPatients()).thenReturn(patients);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(docName, mockedInfo);

        try {
            //Act
            Doctor doc = new Doctor(docName);
            String healed = doc.healsPatient();

            // Assert
            assertEquals(patientName, healed);
        } finally {
            Hospital.MapDoctors.clear();
        }
    }
    @Test
    void testHealsPatientNoSickPatients() {
        //Assert
        String docName = "Богдан Лікар";

        DoctorInfo mockedInfo = Mockito.mock(DoctorInfo.class);
        Map<String, patientStatus> emptyPatients = new HashMap<>();
        Mockito.when(mockedInfo.getPatients()).thenReturn(emptyPatients);

        Hospital.MapDoctors.clear();
        Hospital.MapDoctors.put(docName, mockedInfo);

        try {
            //Act
            Doctor doc = new Doctor(docName);
            String result = doc.healsPatient();

            // Assert
            assertEquals(null, result);
        } finally {
            Hospital.MapDoctors.clear();
        }
    }
}
