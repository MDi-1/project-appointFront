package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.stereotype.Component;

public class MainLayout extends AppLayout {

    public MainLayout() {
        constructHeader();
        constructRouterLinks();
    }

    void constructHeader() {
        H1 logo = new H1("Application Title here");
        logo.addClassName("logo");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassName("header");
        addToNavbar(header);
    }

    void constructRouterLinks() {
        RouterLink firstPage = new RouterLink("Welcome", WelcomeView.class);
        firstPage.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink staffView = new RouterLink("Doctor", DoctorView.class);
        RouterLink patientView = new RouterLink("Patient", PatientView.class);

        addToDrawer(new VerticalLayout(firstPage, staffView, patientView));
    }
}
