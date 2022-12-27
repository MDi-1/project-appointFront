package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.TableEntry;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
public class AppointForm extends FormLayout implements BaseForm{

    private Setup setup;
    private BackendClient client;
    VerticalLayout container = new VerticalLayout();
    HorizontalLayout promptButtons = new HorizontalLayout();
    Label question = new Label();
    Button confirm = new Button("confirm");
    Button back2 = new Button("back");
    private boolean exeMode;

    public AppointForm(BackendClient client, Setup setup) {
        this.client = client;
        this.setup = setup;
        addClassName("appointment-form");
        add(container);
        confirm.addClickListener(event -> pressConfirm());
    }

    @Override
    public void activateControls() {
        if (setup.getEntry().getStatus().equals("n/a") || setup.getEntry().getStatus().equals("off")) return;
        Button back1 = new Button("Back");
        Button btnAcceptDeny = new Button("Appoint");
        HorizontalLayout buttons = new HorizontalLayout(btnAcceptDeny, back1);
        btnAcceptDeny.addClickListener(event -> processApp(setup.getEntry()));
        String doctorName = setup.getDoctor().getName() + " " + setup.getDoctor().getLastName();
        String timeString = " at " + setup.getEntry().getTime();
        container.add(buttons, question);
        if (setup.getEntry().getAttributedApp() == 0) {
            btnAcceptDeny.setText("Appoint");
            question.setText("Are You sure to make an appointment with doctor " + doctorName + timeString);
            exeMode = true;
        } else {
            btnAcceptDeny.setText("Call Off");
            question.setText("Are You sure to call off an appointment with doctor " + doctorName + timeString);
            exeMode = false;
        }
        setup.setTargetDay(setup.getEntry().getWeekday());
        back1.addClickListener(event -> clearForm());
    }

    @Override
    public void clearForm() {
        container.removeAll();
        setup.setEntry(null);
        setup.setEntryProcessed(null);
    }

    public void processApp(TableEntry entry){
        setup.setEntryProcessed(entry);
        container.add(promptButtons);
        promptButtons.add(confirm, back2);
        back2.addClickListener(event -> container.remove(question, promptButtons));
    }

    private void pressConfirm() {
        executeItem();
        promptButtons.removeAll();
        container.remove(question, promptButtons);
        // statement to update tables - to be added
    }

    // mode: true - create App; false - delete App; third option - do not execute this f. (click into back2 btn)
    @Override
    public void executeItem() {
        LocalDate date = setup.getTargetDay(); // client should probably store just Entry field instead of separate date
        LocalDateTime dateTime = date.atTime(setup.getEntry().getTime());
        if (exeMode) {
            Appointment newApp = new Appointment(
                    dateTime.toString(), setup.getDoctor().getId(), setup.getPatient().getId());
            Appointment response = client.createAppointment(newApp);
            System.out.println(response); // fixme
        } else {
            List<Appointment> appList = client.getDoctorAppList();
            for (Appointment item : appList) {
                LocalDateTime parsedTime = LocalDateTime.parse(
                        item.getStartDateTime(),DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm"));
                if (parsedTime.equals(dateTime)) client.deleteAppointment(item.getId());
            }
            /* Futile attempts - do it later
            LocalDateTime parsedTime1 = appList.stream()
                    .filter(i -> LocalDateTime.parse(i, DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm")).equals(dateTime))
                    .findFirst()
                    .orElseThrow(NullPointerException::new);
            System.out.println("]] Project Appoint: " + parsedTime1 + "____" + dateTime);
            */
        }
    }
}
