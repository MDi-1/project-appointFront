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
                ).setHeader("doctors' first name");
        appointmentGrid.addColumn(apt -> backendClient.getDoctorList()
                        .stream().filter(x -> x.getId().equals(apt.getDoctorId())).findAny().get().getLastName()
                ).setHeader("doctors' last name");
        return new VerticalLayout(new Label("List of recent / incoming appointments"), appointmentGrid);
    }
}
