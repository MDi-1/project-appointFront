package com.example.appointfront.engine;

import com.example.appointfront.data.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import static com.example.appointfront.data.Doctor.Position.*;


@Route(value= "start", layout = MainLayout.class)
@PageTitle("Start | Tiny Clinic")
public class StartingView extends HorizontalLayout {

    public StartingView() {
        Label label = new Label("Click one of buttons to log in as sample patient or as administrator");
        Button patient = new Button("Log in as patient");
        Button admin = new Button("Log in as administrator");
        VerticalLayout loginBox = new VerticalLayout(label, patient, admin);
        loginBox.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        loginBox.setMaxHeight("40%");
        loginBox.setMaxWidth("50%");
        add(loginBox);
        loginBox.setAlignItems(Alignment.CENTER);
    }

    public void sendSampleData() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Vincent", "Watkins", Administrator));
        doctors.add(new Doctor("Hillary", "Washington", Specialist));
        doctors.add(new Doctor("Alexander", "Franklin", Specialist));
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Jenny", "Pear"));
        patients.add(new Patient("Will", "Sith"));
        patients.add(new Patient("Bob", "Roberts"));

        List<TimeFrame> timeframes = new ArrayList<>();

        List<Appointment> appointments = new ArrayList<>();

        List<MedicalService> medServices = new ArrayList<>();
    }
}
