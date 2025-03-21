package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.TableEntry;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class AppointForm extends FormLayout implements BaseForm{

    private BackendClient client;
    private DoctorView view;
    private final NativeLabel priceTag = new NativeLabel();
    private final NativeLabel question = new NativeLabel();
    private final Button confirm = new Button("confirm");
    private final Button back2 = new Button("back");
    private final Button back1 = new Button("Back");
    private final Button btnAcceptDeny = new Button("Appoint");
    private final VerticalLayout formContainer = new VerticalLayout();
    private final HorizontalLayout promptButtons = new HorizontalLayout(confirm, back2);
    private final HorizontalLayout initialButtons = new HorizontalLayout(btnAcceptDeny, back1);
    private final ComboBox<MedicalService> msComboBox = new ComboBox<>("serviceName");
    private boolean exeMode;

    public AppointForm(BackendClient client, DoctorView view) {
        this.client = client;
        this.view = view;
        addClassName("appointment-form");
        add(formContainer);
        btnAcceptDeny.addClickListener(event -> formContainer.add(promptButtons));
        confirm.addClickListener(event -> executeItem());
        back1.addClickListener(event -> clearForm());
        back2.addClickListener(event -> formContainer.remove(question, promptButtons));
        if (setup.getDoctor() == null) return;
        msComboBox.setLabel("choose service type");
        msComboBox.setItems(setup.getCurrentDoctorMsList());
        msComboBox.setItemLabelGenerator(e -> e.getServiceName().toString());
        msComboBox.setValue(setup.getCurrentDoctorMsList().get(0));
        msComboBox.addValueChangeListener(e -> priceTag.setText("cost: " + msComboBox.getValue().getPrice()));
    }


    @Override
    public void activateControls() {
        TableEntry ent = setup.getEntry();
        if (ent.getStatus().equals("n/a") || ent.getStatus().equals("off") || ent.getStatus().equals("busy")) return;
        String doctorName = setup.getDoctor().getName() + " " + setup.getDoctor().getLastName();
        String timeString = " at " + ent.getEntryDateTime().toLocalTime();
        priceTag.setText("cost: " + msComboBox.getValue().getPrice());
        msComboBox.setEnabled(true);
        formContainer.add(msComboBox, priceTag, initialButtons, question);
        if (ent.getAttributedApp() == null) {
            btnAcceptDeny.setText("Appoint");
            question.setText("Are You sure to make an appointment with doctor " + doctorName + timeString);
            exeMode = true;
        } else {
            Long msIdFromEntry = ent.getAttributedApp().getMedicalServiceId();
            msComboBox.setValue(setup.getMsList().stream()
                    .filter(e -> e.getId().equals(msIdFromEntry)).findFirst().orElse(null));
            msComboBox.setEnabled(false);
            btnAcceptDeny.setText("Call Off");
            question.setText("Are You sure to call off an appointment with doctor " + doctorName + timeString);
            exeMode = false;
        }
    }

    @Override
    public void clearForm() {
        formContainer.removeAll();
        setup.setEntry(null);
        msComboBox.setEnabled(true);
    }

    @Override
    public void executeItem() {
        LocalDate date = setup.getTargetDay();
        LocalDateTime dateTime = date.atTime(setup.getEntry().getEntryDateTime().toLocalTime());
        if (exeMode) {
            client.createAppointment(new Appointment(
                    dateTime.toString(),
                    msComboBox.getValue().getId(),
                    setup.getDoctor().getId(),
                    setup.getPatient().getId()));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm");
            client.getCurrentDoctorApps().stream()
                    .filter(app -> LocalDateTime.parse(app.getStartDateTime(), formatter).equals(dateTime))
                    .forEach(appointment -> client.deleteAppointment(appointment.getId()));
        }
        formContainer.removeAll();
        view.forceRefresh();
    }
}
