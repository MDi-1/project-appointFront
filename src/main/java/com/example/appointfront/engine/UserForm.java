package com.example.appointfront.engine;

import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class UserForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final Setup setup;
    private boolean lockMode;
    private boolean saveMode;
    private final TextField firstName = new TextField("first name");
    private final TextField lastName = new TextField("last name");
    private Button editBtn = new Button("Edit");
    private Button addBtn = new Button("Add");
    private Button saveBtn = new Button("Save");
    private Button deleteBtn = new Button("Delete");
    private Button canceBtn = new Button("Cancel");
    private Binder<Patient> binder = new Binder<>(Patient.class);

    public UserForm(BackendClient client, Setup setup) {
        this.client = client;
        this.setup = setup;
        addClassName("user-form");
        binder.bindInstanceFields(this);
        if (setup.getAdmission() > 1) {
            add(firstName, lastName, new HorizontalLayout(addBtn, editBtn, saveBtn, deleteBtn, canceBtn));
        } else {
            add(firstName, lastName, new HorizontalLayout(editBtn, saveBtn, deleteBtn, canceBtn));
        }
        if (setup.getPatient() != null) {
            firstName.setPlaceholder(setup.getPatient().getFirstName());
            lastName.setPlaceholder(setup.getPatient().getLastName());
        }
        addBtn.addClickListener(event -> {
            saveMode = false;
            activateControls();
        });
        editBtn.addClickListener(event -> {
            saveMode = true;
            activateControls();
        });
        saveBtn.addClickListener(event -> executeItem());
        deleteBtn.addClickListener(event -> delete());
        canceBtn.addClickListener(event -> {
            binder.setBean(setup.getPatient());
            clearForm();
        });
        switchControls(lockMode);
    }

    @Override
    public void activateControls() {
        lockMode = true;
        if (saveMode) binder.setBean(setup.getPatient());
        else binder.setBean(new Patient());
        switchControls(lockMode);
    }

    @Override
    public void executeItem() {
        Patient patient = binder.getBean();
        System.out.println(patient);
        if (saveMode) client.updatePatient(patient);
        else client.createPatient(patient);
        clearForm();
    }

    public void delete() {
        client.deletePatient(binder.getBean().getId());
        clearForm();
    }

    @Override
    public void clearForm() {
        lockMode = false;
        saveMode = false;
        switchControls(lockMode);
    }

    void switchControls(boolean mode) {
        addBtn.setEnabled(!mode);
        editBtn.setEnabled(!mode);
        saveBtn.setEnabled(mode);
        deleteBtn.setEnabled(mode);
        canceBtn.setEnabled(mode);
    }
}
