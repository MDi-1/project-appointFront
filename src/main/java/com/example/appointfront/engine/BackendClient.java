package com.example.appointfront.engine;

import com.example.appointfront.data.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Getter
@RequiredArgsConstructor
@Component
public class BackendClient {

    private final RestTemplate restTemplate;
    @Value("${endpoint.prefix}")
    private String endpointPrefix;
    private Doctor doctor;
    private Patient patient;
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendClient.class);

    public List<TestDto> getResponse() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "test/getAll").build().encode().toUri();
        try {
            TestDto[] response = restTemplate.getForObject(url, TestDto[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
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

    public List<Appointment> getDocsAppointments() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment/doctorApps/")
                .path(doctor.getId().toString()) // path parameter come here
                .build().encode().toUri();
        try {
            Appointment[] response = restTemplate.getForObject(url, Appointment[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
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
