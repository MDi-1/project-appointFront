package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Route(value="welcome", layout = MainLayout.class)
@PageTitle("Welcome | Tiny Clinic")
@Service
public class WelcomeView extends HorizontalLayout {

    private Client client = new Client();

    public WelcomeView() {
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);

        serviceGrid.setColumns("id", "description", "doctorId");
        doctorGrid.setColumns("id", "firstName", "lastName", "position", "timeframeId");

        serviceGrid.setItems(client.getSTestList1());
        doctorGrid.setItems(client.getDTestList2());

        add(serviceGrid, doctorGrid, new Label("Street,\nZip code, City,\nPhone Number"));
    }
}
