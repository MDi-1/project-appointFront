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
        HorizontalLayout mainTables = new HorizontalLayout(makeAppTab(), makeSrvTab(), makeDocTab());
        mainTables.setSizeFull();
        add(new UserForm(), mainTables, new Label("Company, Street, Postal code, City, Phone number"));
    }

    VerticalLayout makeAppTab() {
        Grid<Appointment> appointmentGrid = new Grid<>(Appointment.class);
        appointmentGrid.setItems(backendClient.getAppointmentList());
        appointmentGrid.setColumns("startDate", "duration");
        appointmentGrid.addColumn(apt -> backendClient.getDoctorList()
                .stream().filter(doc -> doc.getId().equals(apt.getDoctorId())).findAny().get().getFirstName()
        ).setHeader("doctors' name");
        appointmentGrid.addColumn(apt -> backendClient.getDoctorList()
                .stream().filter(doc -> doc.getId().equals(apt.getDoctorId())).findAny().get().getLastName()
        ).setHeader("doctors' surname");
        return new VerticalLayout(new Label("List of recent / incoming appointments"), appointmentGrid);
    }

    VerticalLayout makeSrvTab() {
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        serviceGrid.setColumns("description");
        serviceGrid.setItems(backendClient.getMedServiceList());
        VerticalLayout serviceTab = new VerticalLayout(new Label("Pick service to make an appointment"), serviceGrid);
        serviceTab.setWidth("50%");
        return serviceTab;
    }

    VerticalLayout makeDocTab() {
        Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);
        doctorGrid.setColumns("firstName", "lastName", "position");
        doctorGrid.setItems(backendClient.getDoctorList());
        VerticalLayout docTab = new VerticalLayout(new Label("...or pick doctor to make an appointment"), doctorGrid);
        docTab.setWidth("60%");
        return docTab;
    }
}
