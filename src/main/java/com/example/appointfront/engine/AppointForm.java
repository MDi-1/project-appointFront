package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.TableEntry;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AppointForm extends FormLayout implements BaseForm{

    private BackendClient client;
    private DoctorView view;
    private final Label priceTag = new Label();
    private final Label question = new Label();
    private final Button confirm = new Button("confirm");
    private final Button back2 = new Button("back");
    private final Button back1 = new Button("Back");
    private final Button btnAcceptDeny = new Button("Appoint");
    private final VerticalLayout formContainer = new VerticalLayout();
    private final HorizontalLayout promptButtons = new HorizontalLayout();
    private final HorizontalLayout initialButtons = new HorizontalLayout(btnAcceptDeny, back1);
    private final ComboBox<MedicalService> msComboBox = new ComboBox<>("serviceName");
    private boolean exeMode;

    public AppointForm(BackendClient client, DoctorView view) {
        this.client = client;
        this.view = view;
        addClassName("appointment-form");
        add(formContainer);
        confirm.addClickListener(event -> pressConfirm());
        btnAcceptDeny.addClickListener(event -> processApp());
        back1.addClickListener(event -> clearForm());
        back2.addClickListener(event -> formContainer.remove(question, promptButtons));
        if (setup.getDoctor() == null) return;
        List<MedicalService> msList = setup.getDoctor().getMedServiceIds().stream()
                .map(e -> setup.getMsList().stream()
                        .filter(ms -> ms.getId().equals(e)).findFirst().orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());
        msComboBox.setLabel("choose service type");
        msComboBox.setItems(msList);
        msComboBox.setItemLabelGenerator(e -> e.getServiceName().toString());
        msComboBox.setValue(msList.get(0));
        formContainer.add(msComboBox, priceTag);
    }


    @Override
    public void activateControls() {
        if (setup.getEntry().getStatus().equals("n/a")
                || setup.getEntry().getStatus().equals("off")
                || setup.getEntry().getStatus().equals("busy")
        ) return;
        String doctorName = setup.getDoctor().getName() + " " + setup.getDoctor().getLastName();
        String timeString = " at " + setup.getEntry().getTime();
        formContainer.add(initialButtons, question);
        if (setup.getEntry().getAttributedApp() == null) {
            priceTag.setText("");
            btnAcceptDeny.setText("Appoint");
            question.setText("Are You sure to make an appointment with doctor " + doctorName + timeString);
            exeMode = true;
        } else {
            //msComboBox.setValue(setup.getEntry().getAttributedApp());
            priceTag.setText("p_1");
            btnAcceptDeny.setText("Call Off");
            question.setText("Are You sure to call off an appointment with doctor " + doctorName + timeString);
            exeMode = false;
        }
    }

    @Override
    public void clearForm() {
        formContainer.remove(initialButtons, question);
        setup.setEntry(null);
    }

    public void processApp(){
        formContainer.add(promptButtons);
        promptButtons.add(confirm, back2);
    }

    private void pressConfirm() {
        executeItem();
        promptButtons.removeAll();
        formContainer.remove(question, promptButtons);
        view.forceRefresh();
    }

    // mode: true - create App; false - delete App; third option - do not execute this f. (click into back2 btn)
    @Override
    public void executeItem() {
        LocalDate date = setup.getTargetDay(); // client should probably store just Entry field instead of separate date
        LocalDateTime dateTime = date.atTime(setup.getEntry().getTime());
        if (exeMode) {
            client.createAppointment(new Appointment(
                    dateTime.toString(),
                    msComboBox.getValue().getId(),
                    setup.getDoctor().getId(),
                    setup.getPatient().getId()));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm");
            client.getDoctorAppList().stream()
                    .filter(app -> LocalDateTime.parse(app.getStartDateTime(), formatter).equals(dateTime))
                    .forEach(appointment -> client.deleteAppointment(appointment.getId()));
        }
    }

    public void findAttributedAppointment() {}
}
