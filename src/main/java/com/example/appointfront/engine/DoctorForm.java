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
    private Button addBtn = new Button("Add");
    private Button saveBtn = new Button("Save");
    private Button delBtn = new Button("Delete");
    private ComboBox<Doctor.Position> position = new ComboBox<>("position");
    private ComboBox<MedicalService> services = new ComboBox<>("medical services");
    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    public DoctorForm(List<MedicalService> medicalServices) {
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        position.setItems(Doctor.Position.values());
        services.setItems(medicalServices);
        HorizontalLayout buttons = new HorizontalLayout(addBtn, saveBtn, delBtn);
        add(firstName, lastName, position, buttons);
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
    }

    void clearForm() {
        //fixme
    }

    void inactivateForm() {
        //fixme
    }
}
