package com.example.appointfront.engine;

import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value= "", layout = MainLayout.class)
@PageTitle("Start | Tiny Clinic")
public class StartingView extends HorizontalLayout {

    public StartingView(BackendClient client, InitHeader initHeader) {
        Setup setup = Setup.SINGLETON_INSTANCE;
        List<Patient> patients = client.getAllPatients();
        NativeLabel label = new NativeLabel("Click one of buttons to log in as sample patient or as administrator");
        Button patientBtn = new Button("Log in as patient");
        Button managerBtn = new Button("Log in as manager");
        Button adminButton = new Button("Log in as service administrator");
        Grid<Patient> table = new Grid<>(Patient.class);
        setup.setTargetDay(Setup.STARTING_DAY);
        setup.setPatients(patients);
        table.setItems(patients);
        table.setColumns("firstName", "lastName");
        table.asSingleSelect().addValueChangeListener(event -> setup.setPatient(event.getValue()));
        VerticalLayout loginBox = new VerticalLayout(label, patientBtn, managerBtn, adminButton);//move 2 line 28 fixme
        loginBox.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        loginBox.setMaxHeight("40%");
        loginBox.setMaxWidth("50%");
        add(loginBox, table);
        loginBox.setAlignItems(Alignment.CENTER);
        patientBtn.addClickListener(event -> {
            if (setup.getPatient() == null) setup.setPatient(client.getPatientById(patients.get(0).getId()));
            setup.setAdmission(1);
            initHeader.updateLoggedUser();
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
