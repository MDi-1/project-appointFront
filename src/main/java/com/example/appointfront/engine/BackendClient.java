package com.example.appointfront.engine;

import com.example.appointfront.data.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@Data
@Component
public class BackendClient {

    private final RestTemplate restTemplate;
    @Value("${endpoint.prefix}")
    private String endpointPrefix;
    private boolean admission;
    private Doctor doctor;
    private Patient patient;
    private LocalDate setDay;
    private TableEntry entry;
<<<<<<< Updated upstream
=======
    private List<Appointment> doctorAppList;
>>>>>>> Stashed changes
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendClient.class);

    public List<TestDto> getTestObjects() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "test/getAll").build().encode().toUri();
        try {
            TestDto[] response = restTemplate.getForObject(url, TestDto[].class);
            List<TestDto> testList = Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
            int i = 1;
            for (TestDto item : testList) {
                item.setTestId(i);
                i++;
            }
            return testList;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    public TestDto createTestObject(TestDto dto) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "test").build().encode().toUri();
        return restTemplate.postForObject(url, dto, TestDto.class);
    }

    public List<MedicalService> getMedServiceList() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "medService/getAll").build().encode().toUri();
        try {
            MedicalService[] response = restTemplate.getForObject(url, MedicalService[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Doctor> getDoctorList() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "doctor/getAll").build().encode().toUri();
        try {
            Doctor[] response = restTemplate.getForObject(url, Doctor[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Appointment> getAllAppointments() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment/getAll").build().encode().toUri();
        try {
            Appointment[] response = restTemplate.getForObject(url, Appointment[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Appointment> getAppsByDoc() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment/doctorApps/")
                .path(doctor.getId().toString()) // path parameter comes here
                .build().encode().toUri();
        try {
            Appointment[] response = restTemplate.getForObject(url, Appointment[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Appointment createAppointment(Appointment app) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment").build().encode().toUri();
        try {
            return restTemplate.postForObject(url, app, Appointment.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void deleteAppointment(Long appId) {
        String id = String.valueOf(appId);
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment")
                .path(id)
                .build().encode().toUri();
        restTemplate.delete(url);
    }



    public List<TimeFrame> getDocsTimeFrames() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "timeFrame/byDoc/")
                .path(doctor.getId().toString())
                .build().encode().toUri();
        try {
            TimeFrame[] response = restTemplate.getForObject(url, TimeFrame[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Patient> getAllPatients() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "patient/getAll").build().encode().toUri();
        try {
            Patient[] response = restTemplate.getForObject(url, Patient[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Patient getPatientById(int id) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "patient/")
                .path(String.valueOf(id)).build().encode().toUri();
        return restTemplate.getForObject(url, Patient.class);
    }

    public void setSetDay(LocalDate setDay) {
        this.setDay = setDay;
    }

    public void setAdmission(boolean admission) {
        this.admission = admission;
    }

    public void getAppointmentsForADay() {
    }

    Doctor saveDoctor(Doctor doctor) {
        return null;
    }

    void deleteDoctor(Doctor doctor) {
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
