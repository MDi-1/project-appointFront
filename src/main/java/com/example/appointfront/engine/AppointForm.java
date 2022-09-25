package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.TableEntry;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AppointForm extends FormLayout {

    public AppointForm(BackendClient client) {
        addClassName("appointment-form");
        String txt1 = "Are You sure to ";
        String txt2 = "an appointment with doctor ";
        String docName = client.getDoctor().getFirstName() + " " + client.getDoctor().getLastName();
        Button back1 = new Button("back");
        Button back2 = new Button("back");
        Button appoint = new Button("appoint");
        Button callOff = new Button("call off");
        Button confirm = new Button("confirm");
        Label question = new Label();
        HorizontalLayout buttons = new HorizontalLayout(appoint, back1, callOff);
        VerticalLayout container = new VerticalLayout(buttons);
        HorizontalLayout prompt = new HorizontalLayout();
        add(container);
        confirm.addClickListener(event -> {
            container.remove(question, prompt);
        });
        back2.addClickListener(event -> {
            container.remove(question, prompt);
        });
        appoint.addClickListener(event -> {
            question.setText(txt1 + "make " + txt2 + docName);
            container.add(question, prompt);
            prompt.add(confirm, back2);
        });
        callOff.addClickListener(event -> {
            question.setText(txt1 + "call off " + txt2 + docName);
            container.add(question, prompt);
            prompt.add(confirm, back2);
        });
    }

    public void processApp(TableEntry appointment){

    }
}
