package com.example.appointfront.engine;

import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class UserForm extends FormLayout implements BaseForm{

    private final Setup setup;
    private final BackendClient client;
    private boolean exeMode;
    private TextField firstName = new TextField("first name");
    private TextField lastName = new TextField("last name");
    private Button editBtn = new Button("Edit");
    private Button addBtn = new Button("Add");
    private Button saveBtn = new Button("Save");
    private Button deleteBtn = new Button("Delete");
    private Button canceBtn = new Button("Cancel");
    private Binder<Patient> binder = new Binder<>(Patient.class);

    public UserForm(Setup setup, BackendClient client) {
        this.client = client;
        this.setup = setup;
        binder.bindInstanceFields(this);
        if (setup.getPatient() != null) {
            firstName.setPlaceholder(setup.getPatient().getFirstName());
            lastName.setPlaceholder(setup.getPatient().getLastName());
        }
        if (setup.isAdmission()) {
            add(firstName, lastName, new HorizontalLayout(addBtn, editBtn, saveBtn, deleteBtn, canceBtn));
        } else {
            add(firstName, lastName, new HorizontalLayout(editBtn, saveBtn, deleteBtn, canceBtn));
        }
        addBtn.addClickListener(event -> {
            exeMode = true;
            activateControls();
        });
        editBtn.addClickListener(event -> {
            exeMode = true;
            activateControls();

        });
    }

    @Override
    public void activateControls() {
        switchControls(exeMode);
        saveBtn.addClickListener(event -> executeItem());
        deleteBtn.addClickListener(event -> delete());
        canceBtn.addClickListener(event -> {
            binder.setBean(setup.getPatient());
            clearForm();
        });
    }

    @Override
    public void executeItem() {
        client.createPatient(binder.getBean());
    }

    public void delete() {
        client.deletePatient(binder.getBean().getId());
    }

    @Override
    public void clearForm() {
        switchControls(exeMode);
        exeMode = false;
    }

    void switchControls(boolean mode) {
        firstName.setEnabled(mode);
        lastName.setEnabled(mode);
        addBtn.setEnabled(!mode);
        editBtn.setEnabled(!mode);
        saveBtn.setEnabled(mode);
        deleteBtn.setEnabled(mode);
        canceBtn.setEnabled(mode);
    }
}
