package com.example.appointfront.engine;

import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value= "", layout = MainLayout.class)
@PageTitle("Start | Tiny Clinic")
public class StartingView extends HorizontalLayout {

    private final InitHeader header;

    public StartingView(BackendClient client, Setup setup, InitHeader header) {
        this.header = header;
        List<Patient> patients = client.getAllPatients();
        Label label = new Label("Click one of buttons to log in as sample patient or as administrator");
        Button patientBtn = new Button("Log in as patient");
        Button managerBtn = new Button("Log in as manager");
        Button adminButton = new Button("Log in as service administrator");
        Grid<Patient> table = new Grid<>(Patient.class);
        setup.setTargetDay(setup.getStartingDay());
        setup.setPatients(patients);
        table.setItems(patients);
        table.setColumns("firstName", "lastName");
        table.asSingleSelect().addValueChangeListener(event -> setup.setPatient(event.getValue()));
        VerticalLayout loginBox = new VerticalLayout(label, patientBtn, managerBtn, adminButton);
        loginBox.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        loginBox.setMaxHeight("40%");
        loginBox.setMaxWidth("50%");
        add(loginBox, table);
        loginBox.setAlignItems(Alignment.CENTER);
        patientBtn.addClickListener(event -> {
            if (setup.getPatient() == null) setup.setPatient(client.getPatientById(17L)); //unnecessarily hardcoded 17
            setup.setAdmission(1);
            this.header.updateLoggedUser();
            UI.getCurrent().navigate("user");
        });
        managerBtn.addClickListener(event -> {
            setup.setAdmission(2);
            UI.getCurrent().navigate("user");
        });
        adminButton.addClickListener(event -> {
            setup.setAdmission(3);
            UI.getCurrent().navigate("admin");
        });
    }
}
