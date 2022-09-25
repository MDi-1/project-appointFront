package com.example.appointfront.engine;

import com.example.appointfront.data.TestDto;
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

import static com.example.appointfront.engine.TestView.addFunctionality;

@Route("")
public class MainLayout extends AppLayout {

    public MainLayout() {
        constructHeader();
        constructLinks();
    }

    void constructHeader() {
        H1 logo = new H1("Tiny clinic app");
        logo.addClassName("logo");
        Label loggedUser = new Label("patient Name Surname");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, loggedUser);
        addFunctionality(header);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        addToNavbar(header);
    }

    void constructLinks() {
        RouterLink startingView = new RouterLink("start", StartingView.class);
        startingView.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink userView = new RouterLink("User", UserView.class);
        RouterLink doctorView = new RouterLink("Doctor", DoctorView.class);
        RouterLink testView = new RouterLink("Test", TestView.class); // this thing to be removed later
        addToDrawer(new VerticalLayout(startingView, userView, doctorView, testView));
    }
}
