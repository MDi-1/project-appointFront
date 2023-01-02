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
import java.util.List;

public class DoctorForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final DoctorView view;
    private final Setup setup;
    private boolean exeMode;
    private final TextField name = new TextField("first name");
    private final TextField lastName = new TextField("last name");
    private ComboBox<Doctor.Position> position = new ComboBox<>("position");
    private final HorizontalLayout buttonRow = new HorizontalLayout();
    private final Binder<Doctor> binder = new Binder<>(Doctor.class);
    private static final List<TimeFrame> tfProcessList = new ArrayList<>();
    private final Button addBtn = new Button("Add");
    private final Button editBtn = new Button("Edit Personal Data");
    private final Button timeBtn = new Button("Edit Timeframes");
    private final Button saveTfBtn   = new Button("Save");
    private final Button delTfButton = new Button("Delete");
    private final Button cancelTfBtn = new Button("Cancel");
    private final Button setBtn = new Button("print set"); // remove this one later


    public DoctorForm(BackendClient client, Setup setup, DoctorView view) {
        this.client = client;
        this.setup = setup;
        this.view = view;
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        ComboBox<MedicalService> services = new ComboBox<>("medical services");
        position.setItems(Doctor.Position.values());
        services.setItems(client.getMedServiceList());
        add(name, lastName, position, new HorizontalLayout(addBtn, editBtn, timeBtn));
        if (setup.getDoctor() != null) {
            name.setPlaceholder(setup.getDoctor().getName());
            lastName.setPlaceholder(setup.getDoctor().getLastName());
        }
        addBtn.addClickListener(event -> {
            name.setPlaceholder("");
            lastName.setPlaceholder("");
            exeMode = true;
            activateControls();
            binder.setBean(new Doctor());
        });
        editBtn.addClickListener(event -> {
            binder.setBean(setup.getDoctor());
            exeMode = false;
            activateControls();
        });
        timeBtn.addClickListener(event -> activateTimeFrameControls());
        delTfButton.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            //client.deleteTf(doctor.getId());
            clearForm();
        });
        cancelTfBtn.addClickListener(event -> {
            Arrays.stream(view.getFrameStart()).forEach(e -> e.setEnabled(false));
            Arrays.stream(view.getFrameEnd()).forEach(e -> e.setEnabled(false));
            clearForm();
        });
        saveTfBtn.addClickListener(event -> executeTimeFrames());
        setBtn.addClickListener(event -> getTfProcessList().forEach(System.out::println));
    }

    public void activateTimeFrameControls() {
        clearForm();
        toggleLocks(true);
        binder.setBean(setup.getDoctor());
        tfProcessList.clear();
        Arrays.stream(view.getFrameStart()).forEach(e -> e.setEnabled(true));
        Arrays.stream(view.getFrameEnd()).forEach(e -> e.setEnabled(true));
        exeMode = false;
        buttonRow.add(saveTfBtn, delTfButton, cancelTfBtn, setBtn);
        add(buttonRow);
    }

    // (i) this f. WAS the example how to make toggle switch when it looked like this:
    // setup.setTimetableLock(!setup.isTimetableLock());
    public void toggleLocks(boolean lock) {
        clearForm();
        setup.setTimetableLock(lock);
        view.lockTimetables(setup.isTimetableLock());
        addBtn.setEnabled(!lock);
        editBtn.setEnabled(!lock);
        timeBtn.setEnabled(!lock);
    }

    void prepareTfSet(TextField[] array) { // 2 B deleted
        int i = 0;
        for (TextField field : array) {
            int x = i;
            field.setEnabled(true);
            i ++;
        }
    }

    @Override
    public void activateControls() {
        Button saveBtn = new Button("Save");
        Button delBtn  = new Button("Delete");
        Button cancelBtn = new Button("Cancel");
        buttonRow.add(saveBtn, delBtn, cancelBtn);
        add(buttonRow);
        name.focus();
        saveBtn.addClickListener(event -> executeItem());
        delBtn.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            client.deleteDoctor(doctor.getId());
            clearForm();
        });
        cancelBtn.addClickListener(event -> clearForm());
    }

    @Override
    public void executeItem() {
        Doctor doctor = binder.getBean();
        System.out.println(doctor);
        if (exeMode) client.createDoctor(doctor);
        else client.updateDoctor(doctor);
        clearForm();
    }

    void executeTimeFrames() {
        for (TimeFrame item : tfProcessList) {
            if (item.getId() != null) {
                client.updateTimeframe(item);
            }
            else {
                TimeFrame response = client.createTimeFrame(item);
                System.out.println(response);
            }
        }
        tfProcessList.clear();
        clearForm();
        Arrays.stream(view.getFrameStart()).forEach(e -> e.setEnabled(false));
        Arrays.stream(view.getFrameEnd()).forEach(e -> e.setEnabled(false));
        toggleLocks(false);
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
