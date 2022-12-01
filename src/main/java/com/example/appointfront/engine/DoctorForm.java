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

import java.util.HashSet;
import java.util.Set;

public class DoctorForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final DoctorView view;
    private final Setup setup;
    private boolean exeMode;
    private boolean tfOpenMode;
    private final TextField firstName = new TextField("first name");
    private final TextField lastName = new TextField("last name");
    private final HorizontalLayout buttonRow = new HorizontalLayout();
    private final Binder<Doctor> binder = new Binder<>(Doctor.class);
    private final Set<TimeFrame> tfProcessSet = new HashSet<>();
    private final Button addBtn = new Button("Add");
    private final Button editBtn = new Button("Edit Personal Data");
    private final Button timeBtn = new Button("Edit Timeframes");

    public DoctorForm(BackendClient client, Setup setup, DoctorView view) {
        this.client = client;
        this.setup = setup;
        this.view = view;
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        ComboBox<Doctor.Position> position = new ComboBox<>("position");
        ComboBox<MedicalService> services = new ComboBox<>("medical services");
        position.setItems(Doctor.Position.values());
        services.setItems(client.getMedServiceList());
        add(firstName, lastName, position, new HorizontalLayout(addBtn, editBtn, timeBtn));
        if (setup.getDoctor() != null) {
            firstName.setPlaceholder(setup.getDoctor().getFirstName());
            lastName.setPlaceholder(setup.getDoctor().getLastName());
        }
        addBtn.addClickListener(event -> {
            firstName.setPlaceholder("");
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
    }

    public void activateTimeFrameControls() {
        Button saveTfBtn   = new Button("Save");
        Button delTfButton = new Button("Delete");
        Button cancelTfBtn = new Button("Cancel");
        Button setBtn = new Button("print set"); // remove this one later
        clearForm();
        toggleLocks();
        binder.setBean(setup.getDoctor());
        tfProcessSet.clear();
        tfOpenMode = true;
        prepareTfSet(view.getFrameStart());
        prepareTfSet(view.getFrameEnd());
        saveTfBtn.addClickListener(event -> {
            //for (TimeFrame tf : tfProcessSet) {}
            clearForm();
        });
        delTfButton.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            //client.deleteTf(doctor.getId());
            clearForm();
        });
        cancelTfBtn.addClickListener(event -> clearForm());
        setBtn.addClickListener(event -> {
            System.out.println(" ]] set button pressed ");
            //getTfProcessSet().forEach(System.out::println);
            Set<TimeFrame> tfSet = getTfProcessSet();
            for (TimeFrame item : tfSet) {
                System.out.println(item);
            }
        });
        exeMode = false;
        buttonRow.add(saveTfBtn, delTfButton, cancelTfBtn, setBtn);
        add(buttonRow);
    }

    public void toggleLocks() { // (i) first line of this f. is the example how to make toggle switch.
        setup.setTimetableLock(!setup.isTimetableLock());
        view.lockTimetables(setup.isTimetableLock());
        addBtn.setEnabled(false);
        editBtn.setEnabled(false);
        timeBtn.setEnabled(false);
    }

    private void prepareTfSet(TextField[] array) {
        int i = 0;
        for (TextField field : array) {
            int x = i;
            field.setEnabled(true);
            field.addValueChangeListener(action -> {
                TimeFrame tfx = view.getTfBinderList().get(x).getBean();
                for (TimeFrame tf : tfProcessSet) {
                    if (tf.getDate().equals(tfx.getDate())) {
                        tfProcessSet.remove(tf);
                        System.out.println(" ]] about to exchange tf: " + tf);
                    }
                    tfProcessSet.add(tfx);
                    System.out.println(" ]] added to set: " + tfx);
                }
            });
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
        firstName.focus();
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
        if (exeMode) {
            Doctor doc = client.createDoctor(doctor);
            System.out.println(" ]] Project-Appoint [[ doctor created: " + doc);
        } else {
            Doctor doc = client.updateDoctor(doctor);
            System.out.println(" ]] Project-Appoint [[ doctor updated: " + doc);
        }
        clearForm();
    }

    public void clearForm() {
        buttonRow.removeAll();
        remove(buttonRow);
        if (setup.getDoctor() != null) {
            firstName.setPlaceholder(setup.getDoctor().getFirstName());
            lastName.setPlaceholder(setup.getDoctor().getLastName());
        }
        tfOpenMode = false;
    }

    public Set<TimeFrame> getTfProcessSet() {
        return tfProcessSet;
    }
}
