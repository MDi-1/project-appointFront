package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Route(value="user", layout = MainLayout.class)
@PageTitle("User | Tiny Clinic")
@Service
@UIScope
public class UserView extends VerticalLayout {

    private final BackendClient backendClient;

    public UserView(BackendClient backendClient) {
        this.backendClient = backendClient;
        UserForm userForm = new UserForm();
        Label tableTitle = new Label("Pick service or doctor to make an appointment");
        add(userForm, tableTitle, constructGrids(), new Label("Company, Street, Postal code, City, Phone number"));
    }

    HorizontalLayout constructGrids() {
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);
        HorizontalLayout grids = new HorizontalLayout(createGrid1(), serviceGrid, doctorGrid);
        serviceGrid.setColumns("description");
        doctorGrid.setColumns("firstName", "lastName", "position");
        serviceGrid.setItems(backendClient.getMedServiceList());
        doctorGrid.setItems(backendClient.getDoctorList());
        grids.setSizeFull();
        return grids;
    }

    VerticalLayout createGrid1() {
        Grid<Appointment> appointmentGrid = new Grid<>(Appointment.class);
        appointmentGrid.setItems(backendClient.getAppointmentList());
        appointmentGrid.removeColumnByKey("doctorId");
        appointmentGrid.setColumns("startDate", "duration");
        appointmentGrid.addColumn(apt -> backendClient.getDoctorList()
                        .stream().filter(x -> x.getId().equals(apt.getDoctorId())).findAny().get().getFirstName()
                ).setHeader("doctors' name");

/*
        appointmentGrid.addColumn(apt ->
                {
                    Long id = apt.getDoctorId();
                    String result = null;
                    List<Doctor> docList = backendClient.getDoctorList();
                    for(Doctor doc : docList) {
                        Long id_from_list = doc.getId();
                        if(id_from_list == id) result = doc.getLastName();
                    } return result;
                }).setHeader("doctors' name");
*/

        //appointmentGrid.addColumn(appointment -> backendClient.getDoctorList()
        //        .get(Math.toIntExact(appointment.getDoctorId())).getLastName()).setHeader("doctors' last name");
        return new VerticalLayout(new Label("List of recent / incoming appointments"), appointmentGrid);
    }

    String function(Appointment apt) {
        String result = null;
        List<Doctor> docList = backendClient.getDoctorList();
        Long aid = apt.getDoctorId();
        for(Doctor doc : docList) {
            if(doc.getId() == aid) result = doc.getLastName();
            else result = "no such person found";
        }
        return result;
    }

    String function2() {
        return "abc";
    }
}
