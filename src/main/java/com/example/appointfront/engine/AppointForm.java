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
    private DoctorView view;
    private VerticalLayout container = new VerticalLayout();
    private HorizontalLayout promptButtons = new HorizontalLayout();
    private final Label question = new Label();
    private final Button confirm = new Button("confirm");
    private final Button back2 = new Button("back");
    private final Button back1 = new Button("Back");
    private final Button btnAcceptDeny = new Button("Appoint");

    private boolean exeMode;

    public AppointForm(BackendClient client, Setup setup, DoctorView view) {
        this.client = client;
        this.setup = setup;
        this.view = view;
        addClassName("appointment-form");
        add(container);
        confirm.addClickListener(event -> pressConfirm());
        btnAcceptDeny.addClickListener(event -> processApp(setup.getEntry()));
        back1.addClickListener(event -> clearForm());
    }

    @Override
    public void activateControls() {
        if (setup.getEntry().getStatus().equals("n/a")
                || setup.getEntry().getStatus().equals("off")
                || setup.getEntry().getStatus().equals("busy")
        ) return;
        HorizontalLayout buttons = new HorizontalLayout(btnAcceptDeny, back1);
        String doctorName = setup.getDoctor().getName() + " " + setup.getDoctor().getLastName();
        String timeString = " at " + setup.getEntry().getTime();
        container.add(buttons, question);
        if (setup.getEntry().getAttributedApp() == null) {
            btnAcceptDeny.setText("Appoint");
            question.setText("Are You sure to make an appointment with doctor " + doctorName + timeString);
            exeMode = true;
        } else {
            btnAcceptDeny.setText("Call Off");
            question.setText("Are You sure to call off an appointment with doctor " + doctorName + timeString);
            exeMode = false;
        }
        setup.setTargetDay(setup.getEntry().getWeekday());
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
        view.forceRefresh();
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
