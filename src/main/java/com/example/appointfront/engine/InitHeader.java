<<<<<<< Updated upstream
package com.example.appointfront.engine;public class InitHeader {
=======
package com.example.appointfront.engine;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.stereotype.Component;
import static com.example.appointfront.engine.TestView.addFunctionality;

@Component
public class InitHeader extends HorizontalLayout {

    private final BackendClient client;
    private final Label label = new Label("patient Name Surname");

    public InitHeader(BackendClient client) {
        this.client = client;
        H1 logo = new H1("Tiny clinic app");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, label);
        logo.addClassName("logo");
        addFunctionality(header);  // this thing to be removed later
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.addClassName("header");
        add(header);
    }

    public void updateLoggedUser() {
        String string = "patient: " + client.getPatient().getFirstName() + " " + client.getPatient().getLastName();
        label.setText(string);
    }
>>>>>>> Stashed changes
}
