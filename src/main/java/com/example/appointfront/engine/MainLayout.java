package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
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
        RouterLink patientView = new RouterLink("Patient", PatientView.class);
        RouterLink doctorView = new RouterLink("Doctor", DoctorView.class);

        addToDrawer(new VerticalLayout(firstPage, patientView, doctorView));
    }
}
