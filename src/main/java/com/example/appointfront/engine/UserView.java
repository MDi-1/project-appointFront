package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
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

    private final BackendClient client;
    private final DoctorView doctorView;
    private final Button delApp = new Button("Delete Appointment");
    private final Button go2App = new Button("Show in Timetable");
    private final Button confirm = new Button("Confirm");
    private final Button cancel = new Button("Cancel");
    private final HorizontalLayout appButtonRow = new HorizontalLayout();

    public UserView(BackendClient client) {
        this.client = client;
        this.doctorView = new DoctorView(client);
        HorizontalLayout mainTables = new HorizontalLayout(makeAppTab(), makeServiceTab(), makeDocTab());
        NativeLabel companyDetails = new NativeLabel("Company, Street, Postal code, City, Phone number");
        mainTables.setSizeFull();
        add(new UserForm(client), mainTables, appButtonRow, companyDetails);
    }

    VerticalLayout makeAppTab() {
        NativeLabel appHead = new NativeLabel("List of recent / incoming appointments");
        if (Setup.SINGLETON_INSTANCE.getPatient() != null) {
            Grid<Appointment> appointmentGrid = new Grid<>(Appointment.class);
            appointmentGrid.setItems(client.getAppsByPatient());
            appointmentGrid.setColumns("startDateTime", "price");
            appointmentGrid.addColumn(apt -> client.getDoctorList()
                    .stream().filter(doc -> Objects.equals(doc.getId(), apt.getDoctorId()))
                    .findAny().orElseThrow(IllegalArgumentException::new).getName()).setHeader("doctors' name");
            appointmentGrid.addColumn(apt -> client.getDoctorList()
                    .stream().filter(doc -> Objects.equals(doc.getId(), apt.getDoctorId()))
                    .findAny().orElseThrow(IllegalArgumentException::new).getLastName()).setHeader("doctors' surname");
            appointmentGrid.asSingleSelect().addValueChangeListener(event -> activateAppButtons(event.getValue()));
            return new VerticalLayout(appHead, appointmentGrid);
        } else return new VerticalLayout(appHead, new NativeLabel(
                "log in as patient to see the appointment list"));
    }

    VerticalLayout makeServiceTab() {
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        List<MedicalService> msList = client.getMedServiceList();
        Setup.SINGLETON_INSTANCE.setMsList(msList);
        serviceGrid.setColumns("serviceName");
        serviceGrid.setItems(Setup.SINGLETON_INSTANCE.getMsList());
        serviceGrid.asSingleSelect().addValueChangeListener(event -> {
            Doctor found1stDoc = event.getValue().getDoctorIds().stream().findFirst()
                    .map(e -> Setup.SINGLETON_INSTANCE.getDoctors().get(0)).orElseThrow(IllegalArgumentException::new);
            doctorView.enterDoctorManagement(found1stDoc);
        });
        VerticalLayout serviceTab = new VerticalLayout(new NativeLabel(
                "Pick service to make an appointment"), serviceGrid);
        serviceTab.setWidth("50%");
        return serviceTab;
    }

    VerticalLayout makeDocTab() {
        Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);
        List<Doctor> doctors = client.getDoctorList();
        Setup.SINGLETON_INSTANCE.setDoctors(doctors);
        doctorGrid.setColumns("name", "lastName", "position");
        doctorGrid.setItems(doctors);
        doctorGrid.asSingleSelect().addValueChangeListener(event -> doctorView.enterDoctorManagement(event.getValue()));
        VerticalLayout docTab = new VerticalLayout(new NativeLabel(
                "...or pick doctor to make an appointment"), doctorGrid);
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
            Setup.SINGLETON_INSTANCE.setTargetDay(LocalDate.parse(appointment.getStartDateTime().substring(0, 10)));
            Doctor selectedDoctor = Setup.SINGLETON_INSTANCE.getDoctors().stream()
                    .filter(d -> d.getId().equals(appointment.getDoctorId())).findAny().orElse(null);
            doctorView.enterDoctorManagement(selectedDoctor);
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
