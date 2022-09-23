package com.example.appointfront.engine;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AppointForm extends FormLayout {

    public AppointForm() {
        addClassName("appointment-form");
        Button back1 = new Button("back");
        Button back2 = new Button("back");
        Button appoint = new Button("appoint");
        Button callOff = new Button("call off");
        Button confirm = new Button("confirm");
        Label question = new Label("really?");
        HorizontalLayout buttons = new HorizontalLayout(appoint, back1, callOff);
        VerticalLayout container = new VerticalLayout(buttons);
        HorizontalLayout prompt  = new HorizontalLayout();
        add(container);
        back2.addClickListener(event -> {
            container.remove(question, prompt);
        });
        appoint.addClickListener(event -> {
            container.add(question, prompt);
            prompt.add(confirm, back2);
        });
    }
}
