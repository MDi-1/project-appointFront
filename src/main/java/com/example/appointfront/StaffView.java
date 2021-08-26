package com.example.appointfront;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value= "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class StaffView extends HorizontalLayout {

    StaffView () {
        addClassName("staff-view");
    }
}
