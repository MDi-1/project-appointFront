package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Service;

@Route(value="welcome", layout = MainLayout.class)
@PageTitle("Welcome | Tiny Clinic")
@Service
public class WelcomeView extends VerticalLayout {

    private final BackendClient backendClient;
    Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
    Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);
    HorizontalLayout grids = new HorizontalLayout(serviceGrid, doctorGrid);

    public WelcomeView(BackendClient backendClient) {
        this.backendClient = backendClient;
        constructGrids();
        Label address = new Label("Company, Street, Postal code, City, Phone number");
        add(grids, address);
    }

    private void constructGrids() {
        serviceGrid.setColumns("description");
        doctorGrid.setColumns("firstName", "lastName", "position");
        serviceGrid.setItems(backendClient.getMedServiceList());
        doctorGrid.setItems(backendClient.getDoctorList());
        grids.setSizeFull();
    }
}
