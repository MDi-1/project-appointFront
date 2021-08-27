package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.TestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;


@Getter
//@RequiredArgsConstructor
//@NoArgsConstructor
//@Component
public class Client {
/*
    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public List<TestDto> getResponse() {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/test/getAll").build().encode().toUri();
        try {
            TestDto[] response = restTemplate.getForObject(url, TestDto[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<MedicalService> getMedServiceList() {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/medService/getAll")
                .build().encode().toUri();
        try {
            MedicalService[] response = restTemplate.getForObject(url, MedicalService[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Doctor> getDoctorList() {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/doctor/getAll")
                .build().encode().toUri();
        try {
            Doctor[] response = restTemplate.getForObject(url, Doctor[].class);
            return Optional.ofNullable(response).map(Arrays::asList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

 */

    public List<MedicalService> getSTestList1() {
        List<MedicalService> list = new ArrayList<>();
        list.add(new MedicalService(1L, "service desc1", 1L));
        list.add(new MedicalService(2L, "service desc2", 2L));
        list.add(new MedicalService(3L, "service desc3", 3L));
        return list;
    }

    public List<Doctor> getDTestList2() {
        List<Doctor> list = new ArrayList<>();
        list.add(new Doctor(1L, "name1", "lName1", "internist", 1L, Arrays.asList(35L, 61L), Arrays.asList(1L, 2L)));
        list.add(new Doctor(2L, "name2", "lName2", "surgeon", 2L, Arrays.asList(11L, 22L), Arrays.asList(4L, 5L)));
        list.add(new Doctor(3L, "name3", "lName3", "ginex", 3L, Arrays.asList(77L, 99L), Arrays.asList(6L, 7L)));
        return list;
    }
}
