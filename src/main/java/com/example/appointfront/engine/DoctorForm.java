package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.TimeFrame;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DoctorForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final DoctorView view;
    private boolean exeMode;
    private final ComboBox<Doctor.Position> position = new ComboBox<>("position");
    private final ComboBox<MedicalService> allMsComboBox = new ComboBox<>("serviceName");
    private final TextField name = new TextField("first name");
    private final TextField lastName = new TextField("last name");
    private final HorizontalLayout buttonRow = new HorizontalLayout();
    private final Binder<Doctor> binder = new Binder<>(Doctor.class);
    private static final List<TimeFrame> tfProcessList = new ArrayList<>();
    private final Button addBtn  = new Button("Add");
    private final Button editBtn = new Button("Edit Personal Data");
    private final Button saveBtn = new Button("Save");
    private final Button delBtn  = new Button("Delete");
    private final Button cancelBtn = new Button("Cancel");
    private final Button timeBtn = new Button("Edit Timeframes");
    private final Button saveTfBtn   = new Button("Save");
    private final Button cancelTfBtn = new Button("Cancel");


    public DoctorForm(BackendClient client, DoctorView view) {
        this.client = client;
        this.view = view;
        addClassName("doctor-form");
        add(
                new HorizontalLayout(name, lastName),
                new HorizontalLayout(position, allMsComboBox),
                new HorizontalLayout(addBtn, editBtn, timeBtn)
        );
        saveBtn.addClickListener(event -> executeItem());
        delBtn.addClickListener(event -> {
            client.deleteDoctor(binder.getBean().getId());
            clearForm();
        });
        binder.bindInstanceFields(this);
        cancelBtn.addClickListener(event -> clearForm());
        if (setup.getDoctor() != null) setDocFormPlaceholders();
        addBtn.addClickListener(event -> configActionAdd());
        editBtn.addClickListener(event -> configActionEdit());
        timeBtn.addClickListener(event -> activateTimeFrameControls());
        cancelTfBtn.addClickListener(event -> configActionCancel());
        saveTfBtn.addClickListener(event -> executeTimeFrames());
        allMsComboBox.setItemLabelGenerator(e -> String.valueOf(e.getServiceName()));
    }

    private void setDocFormPlaceholders() {
        Long msId = setup.getDoctor().getMedServiceIds().stream().findFirst().orElse(null);
        name.setPlaceholder(setup.getDoctor().getName());
        lastName.setPlaceholder(setup.getDoctor().getLastName());
        if (setup.getDoctor().getPosition() != null) position.setPlaceholder(setup.getDoctor().getPosition().name());
        if (setup.getMsList() == null) return;
        allMsComboBox.setPlaceholder(setup.getCurrentDoctorMsList().get(0).getServiceName().toString());
    }

    private void configActionAdd() {
        buttonRow.add(saveBtn, delBtn, cancelBtn);
        name.setPlaceholder("");
        lastName.setPlaceholder("");
        exeMode = true;
        activateControls();
        binder.setBean(new Doctor());
    }

    private void configActionEdit() {
        buttonRow.add(saveBtn, delBtn, cancelBtn);
        binder.setBean(setup.getDoctor());
        exeMode = false;
        activateControls();
    }

    private void configActionCancel() {
        Arrays.stream(view.getFrameStart()).forEach(e -> e.setEnabled(false));
        Arrays.stream(view.getFrameEnd()).forEach(e -> e.setEnabled(false));
        toggleLocks(false);
    }

    public void activateTimeFrameControls() {
        clearForm();
        toggleLocks(true);
        binder.setBean(setup.getDoctor());
        tfProcessList.clear();
        Arrays.stream(view.getFrameStart()).forEach(e -> e.setEnabled(true));
        Arrays.stream(view.getFrameEnd()).forEach(e -> e.setEnabled(true));
        exeMode = false;
        buttonRow.add(saveTfBtn, cancelTfBtn);
        add(buttonRow);
    }

    // (i) this f. WAS the example how to make toggle switch when it looked like this:
    // setup.setTimetableLock(!setup.isTimetableLock());
    public void toggleLocks(boolean lock) {
        clearForm();
        view.lockTimetables(lock);
        addBtn.setEnabled(!lock);
        editBtn.setEnabled(!lock);
        timeBtn.setEnabled(!lock);
    }

    @Override
    public void activateControls() {
        name.focus();
        position.setItems(Doctor.Position.values());
        allMsComboBox.setItems(setup.getMsList());
        add(buttonRow);
    }

    @Override
    public void executeItem() {
        Doctor doctor = binder.getBean();
        MedicalService ms = allMsComboBox.getValue();
        if (doctor.getMedServiceIds() != null) doctor.getMedServiceIds().add(ms.getId());
        else doctor.setMedServiceIds(new ArrayList<>(Collections.singletonList(ms.getId())));
        if (exeMode) client.createDoctor(doctor);
        else client.updateDoctor(doctor);
        clearForm();
    }

    void executeTimeFrames() {
        for (TimeFrame timeFrame : tfProcessList) {
            if (timeFrame.getTimeStart().equals("x") && timeFrame.getTimeEnd().equals("x")) {
                client.deleteTimeFrame(timeFrame.getId());
            } else {
                if (timeFrame.getTimeStart().equals("off") && timeFrame.getTimeEnd().equals("off")) {
                    timeFrame.setTfStatus(TimeFrame.TfStatus.Day_Off);
                }
                if (timeFrame.getId() != null) {
                    client.updateTimeframe(timeFrame);
                } else {
                    client.createTimeFrame(timeFrame);
                }
            }
        }
        tfProcessList.clear();
        clearForm();
        Arrays.stream(view.getFrameStart()).forEach(e -> e.setEnabled(false));
        Arrays.stream(view.getFrameEnd()).forEach(e -> e.setEnabled(false));
        toggleLocks(false);
        view.forceRefresh();
    }

    public void clearForm() {
        buttonRow.removeAll();
        remove(buttonRow);
        if (setup.getDoctor() != null) {
            name.setPlaceholder(setup.getDoctor().getName());
            lastName.setPlaceholder(setup.getDoctor().getLastName());
        }
    }

    public static List<TimeFrame> getTfProcessList() {
        return tfProcessList;
    }
}
