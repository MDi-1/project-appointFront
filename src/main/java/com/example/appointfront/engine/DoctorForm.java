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
import java.util.List;
import java.util.Set;

public class DoctorForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final Setup setup;
    private boolean exeMode;
    private boolean tfOpenMode;
    private TextField firstName = new TextField("first name");
    private TextField lastName = new TextField("last name");
    private HorizontalLayout buttonRow = new HorizontalLayout();
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    private Set<TimeFrame> tfProcessSet = new HashSet<>();

    public DoctorForm(List<MedicalService> medicalServices, BackendClient client, Setup setup, DoctorView view) {
        this.client = client;
        this.setup = setup;
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit Personal Data");
        Button timeBtn = new Button("Edit Timeframes");
        ComboBox<Doctor.Position> position = new ComboBox<>("position");
        ComboBox<MedicalService> services = new ComboBox<>("medical services");
        position.setItems(Doctor.Position.values());
        services.setItems(medicalServices);
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
        timeBtn.addClickListener(event -> {
            binder.setBean(setup.getDoctor());
            int i = 0;
            for (TextField field : view.getFrameStart()) {
                int x = i;
                field.setEnabled(true);
                field.addValueChangeListener(action -> {
                    if (!tfOpenMode) activateTfControls();
                    TimeFrame tfx = view.getTfBinderList().get(x).getBean();
                    for (TimeFrame tf : tfProcessSet) {
                        if (tf.getDate().equals(tfx.getDate())) {
                            ...
                        } else tfProcessSet.add(tfx);
                    }
                });
                i ++;
            }


            int j = 0;
            for (TextField item : view.getFrameEnd()) {
                item.setEnabled(true);
                int x = j;
                item.addValueChangeListener(action -> {
                    if (!tfOpenMode) activateTfControls();
                    TimeFrame tfx = view.getTfBinderList().get(x).getBean();
                });
                j++;
            }

            exeMode = false;
        });
    }

    public void activateTfControls() {
        Button saveTfBtn   = new Button("Save");
        Button delTfButton = new Button("Delete");
        Button cancelTfBtn = new Button("Cancel");
        buttonRow.add(saveTfBtn, delTfButton, cancelTfBtn);
        add(buttonRow);
        tfOpenMode = true;
        saveTfBtn.addClickListener(event -> {
            clearForm();
        });
        delTfButton.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            //client.deleteTf(doctor.getId());
            clearForm();
        });
        cancelTfBtn.addClickListener(event -> clearForm());
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
}
