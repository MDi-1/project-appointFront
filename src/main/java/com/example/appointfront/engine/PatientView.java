package com.example.appointfront.engine;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value= "patients", layout = MainLayout.class)
@PageTitle("Patients | Tiny Clinic")
public class PatientView extends HorizontalLayout {
}
