package com.example.appointfront.engine;

import com.example.appointfront.data.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.*;

@Data
@Component
public class BackendClient {

    private Setup setup = Setup.SINGLETON_INSTANCE;
    private final RestTemplate restTemplate;
    @Value("${endpoint.prefix}")
    private String endpointPrefix;
    private List<Appointment> doctorAppList;
    private static final Logger LOGGER = LoggerFactory.getLogger(BackendClient.class);

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

    public MedicalService createMedicalService(MedicalService service) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "medService").build().encode().toUri();
        try {
            return restTemplate.postForObject(url, service, MedicalService.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void updateMedicalService(MedicalService service) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "medService").build().encode().toUri();
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(service), MedicalService.class);
    }

    public void deleteMedicalService(Long msId) {
        String id = String.valueOf(msId);
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "medService/").path(id).build().encode().toUri();
        restTemplate.delete(url);
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

    public Doctor createDoctor(Doctor doctor) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "doctor").build().encode().toUri();
        try {
            return restTemplate.postForObject(url, doctor, Doctor.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void updateDoctor(Doctor doctor) { // fixme
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "doctor").build().encode().toUri();
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(doctor), Doctor.class);
    }

    public void deleteDoctor(Long docId) {
        String id = String.valueOf(docId);
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "doctor/")
                .path(id)
                .build().encode().toUri();
        restTemplate.delete(url);
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
                .path(String.valueOf(setup.getDoctor().getId()))
                .build().encode().toUri();
        try {
            Appointment[] response = restTemplate.getForObject(url, Appointment[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Appointment> getAppsByPatient() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment/patientApps/")
                .path(String.valueOf(setup.getPatient().getId()))
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
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment/")
                .path(id)
                .build().encode().toUri();
        restTemplate.delete(url);
    }

    public List<TimeFrame> getDocsTimeFrames() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "timeFrame/byDoc/")
                .path(String.valueOf(setup.getDoctor().getId()))
                .build().encode().toUri();
        try {
            TimeFrame[] response = restTemplate.getForObject(url, TimeFrame[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public TimeFrame createTimeFrame(TimeFrame tf) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "timeFrame").build().encode().toUri();
        try {
            return restTemplate.postForObject(url, tf, TimeFrame.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void updateTimeframe(TimeFrame tf) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "timeFrame").build().encode().toUri();
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(tf), TimeFrame.class);
    }

    public void deleteTimeFrame(Long tfId) {
        String id = String.valueOf(tfId);
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "timeFrame/")
                .path(id)
                .build().encode().toUri();
        restTemplate.delete(url);
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

    public Patient getPatientById(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "patient/")
                .path(String.valueOf(id)).build().encode().toUri();
        return restTemplate.getForObject(url, Patient.class);
    }

    public String getEv() {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "doctor/getEv").build().encode().toUri();
        return restTemplate.getForObject(url, String.class);
    }

    // (i) it is possible to make f. return void and ignore response DTO from restTemplate.postForObject() by supplying
    // third parameter as String.class and not assigning returned object from restTemplate.postForObject() to anything
    public void createEv(Appointment appointment) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "appointment/createEv").build().encode().toUri();
        restTemplate.postForObject(url, appointment, String.class);
    }

    public Patient createPatient(Patient patient) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "patient").build().encode().toUri();
        try {
            return restTemplate.postForObject(url, patient, Patient.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void updatePatient(Patient patient) {
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "patient").build().encode().toUri();
        restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(patient), Patient.class);
    }

    public void deletePatient(Long patientId) {
        String id = String.valueOf(patientId);
        URI url = UriComponentsBuilder.fromHttpUrl(endpointPrefix + "patient/")
                .path(id)
                .build().encode().toUri();
        restTemplate.delete(url);
    }
}
