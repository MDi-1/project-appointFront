package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.TableEntry;
import com.google.common.base.Objects;
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
    private final VerticalLayout formContainer = new VerticalLayout();
    private final HorizontalLayout promptButtons = new HorizontalLayout();
    private final Label question = new Label();
    private final Button confirm = new Button("confirm");
    private final Button back2 = new Button("back");
    private final Button back1 = new Button("Back");
    private final Button btnAcceptDeny = new Button("Appoint");
    private final ComboBox<MedicalService.ServiceName> mServiceNameBox = new ComboBox<>("serviceName");
    private boolean exeMode;

    public AppointForm(BackendClient client, DoctorView view) {
        this.client = client;
        this.view = view;
        addClassName("appointment-form");
        add(formContainer);
        confirm.addClickListener(event -> pressConfirm());
        btnAcceptDeny.addClickListener(event -> processApp(setup.getEntry()));
        back1.addClickListener(event -> clearForm());
        back2.addClickListener(event -> formContainer.remove(question, promptButtons));
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
        formContainer.add(buttons, question);
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
        formContainer.removeAll();
        setup.setEntry(null);
        setup.setEntryProcessed(null);
    }

    public void processApp(TableEntry entry){
        setup.setEntryProcessed(entry);
        formContainer.add(promptButtons);
        promptButtons.add(confirm, back2);
    }

    private void pressConfirm() {
        executeItem();
        mServiceNameBox.setItems();

        List<MedicalService> msListOfDoc1 = setup.getMsList()
                .stream()
                .filter(ms -> ms.getId()
                        .equals(
                                setup.getDoctor().getMedServiceIds().stream()
                                        .filter(docMsId -> docMsId.equals(ms.getId()))
                                        .findFirst()
                                        .orElseThrow(IllegalArgumentException::new)
                        )
                ).collect(Collectors.toList());


        List<Long> msIdList = setup.getMsList().stream().map(MedicalService::getId).collect(Collectors.toList());
        msIdList.removeAll(setup.getDoctor().getMedServiceIds());


        List<MedicalService> msListOfDoc2 = setup.getDoctor().getMedServiceIds().stream()
                .map(docMsId -> setup.getMsList().stream()
                        .filter(ms -> ms.getId().equals(docMsId))
                        .findFirst().orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());


        Long ms = 0L;
        MedicalService medicalServiceAll = setup.getMsList().stream()
                .filter(e -> e.getId().equals(ms))
                .findFirst().orElseThrow(IllegalArgumentException::new);






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
                    dateTime.toString(), setup.getDoctor().getId(), setup.getPatient().getId()));
        } else {
            client.getDoctorAppList().stream()
                    .filter(e -> LocalDateTime.parse(
                            e.getStartDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm")).equals(dateTime))
                    .forEach(e -> client.deleteAppointment(e.getId()));
        }
    }
}
