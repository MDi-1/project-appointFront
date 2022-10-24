package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.AppLayout;
<<<<<<< Updated upstream
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
=======
>>>>>>> Stashed changes
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainLayout extends AppLayout {

<<<<<<< Updated upstream
    private Setup setup;

    public MainLayout(Setup setup) {
        this.setup = setup;
        H1 logo = new H1("Tiny clinic app");
        Label loggedUser = new Label("patient Name Surname");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, loggedUser);
        logo.addClassName("logo");
        addFunctionality(header);  // this thing to be removed later
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        addToNavbar(header);

=======
    private InitHeader header;

    public MainLayout(InitHeader header) {
        this.header = header;
        addToNavbar(this.header);
>>>>>>> Stashed changes
        RouterLink startingView = new RouterLink("start", StartingView.class);
        startingView.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink userView = new RouterLink("User", UserView.class);
        RouterLink doctorView = new RouterLink("Doctor", DoctorView.class);
        RouterLink testView = new RouterLink("Test", TestView.class); // this thing to be removed later
        addToDrawer(new VerticalLayout(startingView, userView, doctorView, testView));
    }
}
