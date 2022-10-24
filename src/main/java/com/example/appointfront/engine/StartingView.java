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

import java.time.LocalDate;

@Route(value= "start", layout = MainLayout.class)
@PageTitle("Start | Tiny Clinic")
public class StartingView extends HorizontalLayout {

<<<<<<< Updated upstream
    private Setup setup;

    public StartingView(BackendClient client, Setup setup) {
        this.setup = setup;
=======

    private InitHeader header;

    public StartingView(BackendClient client, InitHeader header) {
        this.header = header;
>>>>>>> Stashed changes
        client.setSetDay(LocalDate.of(2022, 9, 15)); // temporary value for target day in createTables
        Label label = new Label("Click one of buttons to log in as sample patient or as administrator");
        Button patientBtn = new Button("Log in as patient");
        Button admin = new Button("Log in as administrator");
        Grid<Patient> table = new Grid<>(Patient.class);
        table.setItems(client.getAllPatients());
        table.asSingleSelect().addValueChangeListener(event -> client.setPatient(event.getValue()));
        VerticalLayout loginBox = new VerticalLayout(label, patientBtn, admin);
        loginBox.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        loginBox.setMaxHeight("40%");
        loginBox.setMaxWidth("50%");
        add(loginBox, table);
        loginBox.setAlignItems(Alignment.CENTER);
        patientBtn.addClickListener(event -> {
<<<<<<< Updated upstream
            if (client.getPatient() == null) client.setPatient(client.getPatientById(17)); // unnecessarily hardcoded 17
            client.setAdmission(false);
            setup.setLabelText(client.getPatient().getFirstName() + " " + client.getPatient().getLastName());
=======
            if (client.getPatient() == null) client.setPatient(client.getPatientById(17)); //unnecessarily hardcoded 17
            client.setAdmission(false);
            this.header.updateLoggedUser();
>>>>>>> Stashed changes
            UI.getCurrent().navigate("user");
        });
        admin.addClickListener(event -> {
            client.setAdmission(true);
            UI.getCurrent().navigate("doctor");
        });
    }
}
