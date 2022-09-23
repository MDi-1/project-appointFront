package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorForm extends FormLayout {

    @Autowired
    private BackendClient backendClient;
    private TextField firstName = new TextField("first name");
    private TextField lastName = new TextField("last name");
    private ComboBox<MedicalService> services = new ComboBox<>("medical services");
    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    public DoctorForm(List<MedicalService> medicalServices) {
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        ComboBox<Doctor.Position> position = new ComboBox<>("position");
        position.setItems(Doctor.Position.values());
        services.setItems(medicalServices);
        Button saveBtn = new Button("Save");
        Button delBtn = new Button("Delete");
        Button addBtn = new Button("Add");
        HorizontalLayout buttonRow1 = new HorizontalLayout(addBtn, saveBtn, delBtn);
        Button timeBtn = new Button("Edit Timeframe");
        Button cancelBtn = new Button("Cancel");
        HorizontalLayout buttonRow2 = new HorizontalLayout(timeBtn, cancelBtn);
        add(firstName, lastName, position, buttonRow1, buttonRow2);
        addBtn.addClickListener(event -> {
            Doctor doctor = new Doctor();
            binder.setBean(doctor);
            firstName.focus();
        });
        saveBtn.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            backendClient.saveDoctor(doctor);
        });
        delBtn.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            backendClient.deleteDoctor(doctor);
            clearForm();
        });
        timeBtn.addClickListener(event -> {

        });
        cancelBtn.addClickListener(event -> {

        });
    }

    void clearForm() {
        // fixme
    }

    void inactivateForm() {
        // fixme
    }
}
