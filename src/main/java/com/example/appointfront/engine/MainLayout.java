package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout(InitHeader header) {
        addToNavbar(header);
        RouterLink startingView = new RouterLink("start", StartingView.class);
        startingView.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink userView = new RouterLink("User", UserView.class);
        RouterLink doctorView = new RouterLink("Doctor", DoctorView.class);
        RouterLink testView = new RouterLink("admin", TechnicalView.class);
        addToDrawer(new VerticalLayout(startingView, userView, doctorView, testView));
    }
}
