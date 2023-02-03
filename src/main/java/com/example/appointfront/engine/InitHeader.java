package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import static com.example.appointfront.engine.TechnicalView.addFunctionality;

@Component
@UIScope
public class InitHeader extends HorizontalLayout {

    private final Setup setup;
    private final Label label = new Label("patient Name Surname");

    public InitHeader(Setup setup) {
        this.setup = setup;
        H1 logo = new H1("Tiny clinic app");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, label);
        logo.addClassName("logo");
        addFunctionality(header);  // this thing to be removed later
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        add(header);
    }

    public void updateLoggedUser() { // is this supposed to be here? in this nothing uses this f.
        String string = "patient: " + setup.getPatient().getFirstName() + " " + setup.getPatient().getLastName();
        label.setText(string);
    }
}
