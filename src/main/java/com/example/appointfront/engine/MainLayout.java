package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
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
        constructLinks();
    }

    void constructHeader() {
        Button b1 = new Button("tmp test button-1");
        H1 logo = new H1("Tiny clinic app");
        logo.addClassName("logo");
        Label loggedUser = new Label("patient Name Surname");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, loggedUser, b1);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        addToNavbar(header);
    }

    void constructLinks() {
        RouterLink startingView = new RouterLink("start", StartingView.class);
        startingView.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink userView = new RouterLink("User", UserView.class);
        RouterLink doctorView = new RouterLink("Doctor", DoctorView.class);
        addToDrawer(new VerticalLayout(startingView, userView, doctorView));
    }
}
