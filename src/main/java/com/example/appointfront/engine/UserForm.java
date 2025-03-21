package com.example.appointfront.engine;

import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class UserForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private boolean lockMode;
    private boolean saveMode;
    private final Button editBtn = new Button("Edit");
    private final Button addBtn = new Button("Add");
    private final Button saveBtn = new Button("Save");
    private final Button deleteBtn = new Button("Delete");
    private final Button cancelBtn = new Button("Cancel");
    private final Binder<Patient> binder = new Binder<>(Patient.class);
    // (i) binder .bindInstanceFields(this) does not allow to make these two fields "firstName" and "lastName"
    // as local ones, thus Intellij suggestion is improper
    private TextField firstName = new TextField("first name");
    private TextField lastName = new TextField("last name");

    public UserForm(BackendClient client) {
        this.client = client;
        addClassName("user-form");
        binder.bindInstanceFields(this);
        if (setup.getAdmission() > 1) {
            add(firstName, lastName, new HorizontalLayout(addBtn, editBtn, saveBtn, deleteBtn, cancelBtn));
        } else {
            add(firstName, lastName, new HorizontalLayout(editBtn, saveBtn, deleteBtn, cancelBtn));
        }
        if (setup.getPatient() != null) {
            firstName.setPlaceholder(setup.getPatient().getFirstName());
            lastName.setPlaceholder(setup.getPatient().getLastName());
        }
        setupButtons();
        lockControls(lockMode);
    }

    private void setupButtons() {
        addBtn.addClickListener(event -> {
            saveMode = false;
            activateControls();
        });
        editBtn.addClickListener(event -> {
            saveMode = true;
            activateControls();
        });
        saveBtn.addClickListener(event -> executeItem());
        deleteBtn.addClickListener(event -> {
            client.deletePatient(binder.getBean().getId());
            clearForm();
        });
        cancelBtn.addClickListener(event -> {
            binder.setBean(setup.getPatient());
            clearForm();
        });
    }

    @Override
    public void activateControls() {
        lockMode = true;
        if (saveMode) binder.setBean(setup.getPatient());
        else binder.setBean(new Patient());
        lockControls(lockMode);
    }

    @Override
    public void executeItem() {
        Patient patient = binder.getBean();
        if (saveMode) client.updatePatient(patient);
        else client.createPatient(patient);
        clearForm();
    }

    @Override
    public void clearForm() {
        lockMode = false;
        saveMode = false;
        lockControls(lockMode);
    }

    void lockControls(boolean mode) {
        addBtn.setEnabled(!mode);
        editBtn.setEnabled(!mode);
        saveBtn.setEnabled(mode);
        deleteBtn.setEnabled(mode);
        cancelBtn.setEnabled(mode);
    }
}
