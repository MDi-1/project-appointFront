package com.example.appointfront.engine;

import com.example.appointfront.data.Patient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class UserForm extends FormLayout implements BaseForm{

    TextField firstName = new TextField("first name");
    TextField lastName = new TextField("last name");
    Button addBtn = new Button("Add");
    Button saveBtn = new Button("Save");
    Button deleteBtn = new Button("Delete");
    Button canceBtn = new Button("Cancel");
    Binder<Patient> binder = new Binder<>(Patient.class);

    public UserForm() {
        binder.bindInstanceFields(this);
        add(firstName, lastName, new HorizontalLayout(addBtn, saveBtn, deleteBtn, canceBtn));
    }

    @Override
    public void setButtons() {

    }

    @Override
    public void executeItem() {

    }

    public void delete() {

    }

    @Override
    public void clearForm() {

    }
}

