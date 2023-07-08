package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@UIScope
public class InitHeader extends HorizontalLayout {

    private static final Label label = new Label("patient Name Surname");
    private static BackendClient client;

    public InitHeader(BackendClient client) {
        InitHeader.client = client;
        H1 logo = new H1("Tiny clinic app");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, label);
        logo.addClassName("logo");
        addFunctionality2(header);  // this thing to be removed later fixme
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        add(header);
    }

    public static void updateLoggedUser() {
        String patientFirstName = Setup.SINGLETON_INSTANCE.getPatient().getFirstName();
        String patientLastName = Setup.SINGLETON_INSTANCE.getPatient().getLastName();
        label.setText("patient: " + patientFirstName + " " + patientLastName);
    }

    public void addFunctionality2(HorizontalLayout header) {  // this thing to be removed later fixme
        Button b1 = new Button("tmp test button-1");
        Button b2 = new Button("tmp test TF set");
        b1.addClickListener(event -> client.getEv());
        b2.addClickListener(event -> client.createEv(new Appointment(null, "2022-09-16T10:00", 200, 3L, 14L, 0L)));
        header.add(b1, b2, add_testFunctionality3());
    }

    public Button add_testFunctionality3() {           // this thing to be removed later fixme
        Button b3 = new Button("tmp test f.- login sequence");
        b3.addClickListener(event -> makeTestBtnEvent());
        return b3;
    }

    public static void makeTestBtnEvent() {                 // this thing to be removed later fixme
        Setup setup = Setup.SINGLETON_INSTANCE;
        // equivalent of click - Button patientBtn = new Button("Log in as patient");
        if (setup.getPatient() ==null) setup.setPatient(client.getPatientById(client.getAllPatients().get(0).getId()));
        setup.setAdmission(1);
        updateLoggedUser();
        UI.getCurrent().navigate("user");
        // equivalent of click - nav to starting view;
        UI.getCurrent().navigate("");
        // equivalent of click - Button managerBtn = new Button("Log in as manager");
        setup.setAdmission(2);
        UI.getCurrent().navigate("user");
        //doctorView.enterDoctorManagement(event.getValue())
        Long id = setup.getDoctors().stream().map(Doctor::getId)
                .max(java.util.Comparator.naturalOrder()).orElseThrow(IllegalArgumentException::new);
        setup.setDoctor(setup.getDoctors().stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null));
        UI.getCurrent().navigate("doctor");
    }
}
