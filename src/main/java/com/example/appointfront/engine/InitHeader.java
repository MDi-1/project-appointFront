package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class InitHeader extends HorizontalLayout {

    private final Label label = new Label("patient Name Surname");

    public InitHeader() {
        H1 logo = new H1("Tiny clinic app");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, label);
        logo.addClassName("logo");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        add(header);
    }

    public void updateLoggedUser() {
        String patientFirstName = Setup.SINGLETON_INSTANCE.getPatient().getFirstName();
        String patientLastName = Setup.SINGLETON_INSTANCE.getPatient().getLastName();
        label.setText("patient: " + patientFirstName + " " + patientLastName);
    }
}
