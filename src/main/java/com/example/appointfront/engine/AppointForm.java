package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
<<<<<<< Updated upstream
=======
import java.util.List;
>>>>>>> Stashed changes

@NoArgsConstructor
public class AppointForm extends FormLayout implements BaseForm{

    private BackendClient client;
    VerticalLayout container = new VerticalLayout();
    HorizontalLayout promptPanel = new HorizontalLayout();
    Label question = new Label();
    Button btnAcceptDeny = new Button("appoint");
    Button confirm = new Button("confirm");
    Button back2 = new Button("back");
    boolean appointMode;

    public AppointForm(BackendClient client) {
        this.client = client;
        addClassName("appointment-form");
        Button back1 = new Button("back");

        HorizontalLayout buttons = new HorizontalLayout(btnAcceptDeny, back1);
        add(container);
        container.add(buttons);
        btnAcceptDeny.addClickListener(event -> processApp());
    }

    @Override
    public void setButtons() {
        String doctorName = client.getDoctor().getFirstName() + " " + client.getDoctor().getLastName();
        String timeString = " at " + client.getEntry().getTime();
        container.add(question);
        if (client.getEntry().getAttributedApp() == 0) {
            btnAcceptDeny.setText("Appoint");
            question.setText("Are You sure to make an appointment with doctor " + doctorName + timeString);
            appointMode = true;
        } else {
            btnAcceptDeny.setText("Call Off");
            question.setText("Are You sure to call off an appointment with doctor " + doctorName + timeString);
            appointMode = false;
        }
    }

    @Override
    public void clearForm() {

    }

    public void processApp(){
        container.add(promptPanel);
        promptPanel.add(confirm, back2);
        confirm.addClickListener(event -> {
            executeItem();
            container.remove(question, promptPanel);
            // statement to update tables
        });
        back2.addClickListener(event -> container.remove(question, promptPanel));
    }

    // mode: true - create App; false - delete App; third option - do not execute this f. (click into back2 btn)
    public void executeItem() {
        LocalDate date = client.getSetDay(); // client should probably store just Entry field instead of separate date
        LocalDateTime dateTime = date.atTime(client.getEntry().getTime());
        if (appointMode) {
            Appointment newApp = new Appointment(
                    dateTime.toString(), client.getDoctor().getId(), client.getPatient().getId());
            Appointment response = client.createAppointment(newApp);
            System.out.println(response); // fixme
        } else {
<<<<<<< Updated upstream
=======
            List<Appointment> appList = client.getDoctorAppList();
            //appList.stream().filter()



>>>>>>> Stashed changes
            System.out.println(" >>??? deleted ???<<");
        }
    }
}
