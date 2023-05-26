package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Route(value="user", layout = MainLayout.class)
@PageTitle("User | Tiny Clinic")
@UIScope
public class UserView extends VerticalLayout {

    private final Setup setup;
    private final BackendClient client;
    private final DoctorView doctorView;
    private final Button delApp = new Button("Delete Appointment");
    private final Button go2App = new Button("Show in Timetable");
    private final Button confirm = new Button("Confirm");
    private final Button cancel = new Button("Cancel");
    private final HorizontalLayout appButtonRow = new HorizontalLayout();

    public UserView(Setup setup, BackendClient client) {
        this.client = client;
        this.setup = setup;
        doctorView = new DoctorView(setup, client);
        HorizontalLayout mainTables = new HorizontalLayout(makeAppTab(), makeServiceTab(), makeDocTab());
        Label companyDetails = new Label("Company, Street, Postal code, City, Phone number");
        mainTables.setSizeFull();
        add(new UserForm(setup, client), mainTables, appButtonRow, companyDetails);
    }

    VerticalLayout makeAppTab() {
        Label appHead = new Label("List of recent / incoming appointments");




        if (setup.getPatient() != null) {
            Grid<Appointment> appointmentGrid = new Grid<>(Appointment.class);
            appointmentGrid.setItems(client.getAppsByPatient());
            appointmentGrid.setColumns("startDateTime", "price");
            appointmentGrid.addColumn(apt -> client.getDoctorList()
                    .stream()
                    .filter(doc -> Objects.equals(doc.getId(), apt.getDoctorId()))
                    .findAny()
                    .get()
                    .getName())
                    .setHeader("doctors' name");
            appointmentGrid.addColumn(apt -> client.getDoctorList()
                    .stream().filter(doc -> Objects.equals(doc.getId(), apt.getDoctorId()))
                    .findAny().get().getLastName()).setHeader("doctors' surname");
            appointmentGrid.asSingleSelect().addValueChangeListener(event -> activateAppButtons(event.getValue()));
            return new VerticalLayout(appHead, appointmentGrid);
        } else return new VerticalLayout(appHead, new Label("log in as patient to see the appointment list"));
    }

    VerticalLayout makeServiceTab() {
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        setup.setMsList(client.getMedServiceList());
        serviceGrid.setColumns("serviceName");
        serviceGrid.setItems(setup.getMsList());
        VerticalLayout serviceTab = new VerticalLayout(new Label("Pick service to make an appointment"), serviceGrid);
        serviceTab.setWidth("50%");
        return serviceTab;
    }

    VerticalLayout makeDocTab() {
        Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);
        List<Doctor> doctors = client.getDoctorList();
        setup.setDoctors(doctors);
        doctorGrid.setColumns("name", "lastName", "position");
        doctorGrid.setItems(doctors);
        doctorGrid.asSingleSelect().addValueChangeListener(event -> doctorView.enterDoctorManagement(event.getValue()));
        // seems pointless to call enterDoctorManagement() to go to docView class and do nothing but set doc in
        // backend client
        VerticalLayout docTab = new VerticalLayout(new Label("...or pick doctor to make an appointment"), doctorGrid);
        docTab.setWidth("60%");
        return docTab;
    }

    void activateAppButtons(Appointment appointment) {
        confirm.setEnabled(false);
        appButtonRow.add(delApp, go2App, confirm, cancel);
        delApp.addClickListener(event -> {
            confirm.setEnabled(true);
            go2App.setEnabled(false);
        });
        go2App.addClickListener(event -> {
            clearAppButtons();
            LocalDate parsedDate = LocalDate.parse(appointment.getStartDateTime().substring(0, 10));
            System.out.println(parsedDate);
            setup.setTargetDay(parsedDate);
            Doctor d = null;
            for (Doctor doctor : setup.getDoctors()) {
                if (doctor.getId() == appointment.getDoctorId()) d = doctor;
            }
            doctorView.enterDoctorManagement(d);
        });
        confirm.addClickListener(event -> {
            client.deleteAppointment(appointment.getId());
            clearAppButtons();
        });
        cancel.addClickListener(event -> clearAppButtons());
    }

    void clearAppButtons() {
        go2App.setEnabled(true);
        confirm.setEnabled(false);
        appButtonRow.removeAll();
    }
}
