package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
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
    private static BackendClient client;

    public InitHeader(BackendClient client) {
        InitHeader.client = client;
        H1 logo = new H1("Tiny clinic app");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, label);
        logo.addClassName("logo");
        addFunctionality2(header);  // this thing to be removed later fixme
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        add(header);
    }

    public void updateLoggedUser() {
        String patientFirstName = Setup.SINGLETON_INSTANCE.getPatient().getFirstName();
        String patientLastName = Setup.SINGLETON_INSTANCE.getPatient().getLastName();
        label.setText("patient: " + patientFirstName + " " + patientLastName);
    }

    public void addFunctionality2(HorizontalLayout header) {  // this thing to be removed later fixme
        Button b1 = new Button("tmp test button-1");
        Button b2 = new Button("tmp test TF set");
        b1.addClickListener(event -> client.getEv());
        b2.addClickListener(event -> client.createEv(new Appointment(null, "2022-09-16T10:00", 200, 3L, 14L)));
        header.add(b1, b2);
    }
/*
This f. served as exercise of creating some code to be imported to MainLayout in form of static import
(to avoid creation of instance field for BackendClient in MainLayout). The following conditions must be met for this:
printTestList() needs to be made static, instance field 'client' needs to be static, there has to be @Component
annotation for this class.
 */

}
