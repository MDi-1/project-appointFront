package com.example.appointfront.engine;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value= "start", layout = MainLayout.class)
@PageTitle("Start | Tiny Clinic")
public class StartingView extends HorizontalLayout {

    public StartingView() {
        Label label = new Label("Click one of buttons to log in as sample patient or as administrator");
        Button patient = new Button("Log in as patient");
        Button admin = new Button("Log in as administrator");
        VerticalLayout loginBox = new VerticalLayout(label, patient, admin);
        loginBox.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        loginBox.setMaxHeight("40%");
        loginBox.setMaxWidth("50%");
        add(loginBox);
        loginBox.setAlignItems(Alignment.CENTER);
    }
}
