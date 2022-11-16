package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final Setup setup;
    private TextField firstName = new TextField("first name");
    private TextField lastName = new TextField("last name");
    private ComboBox<MedicalService> services = new ComboBox<>("medical services");
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    private Button editBtn = new Button("Edit Doctor");

    public DoctorForm(List<MedicalService> medicalServices, BackendClient client, Setup setup) {
        this.client = client;
        this.setup = setup;
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        ComboBox<Doctor.Position> position = new ComboBox<>("position");
        position.setItems(Doctor.Position.values());
        services.setItems(medicalServices);
        Button addBtn = new Button("Add");
        add(firstName, lastName, position, editBtn, addBtn);
        editBtn.addClickListener(event -> {
            Doctor doctor = setup.getDoctor();
            activateControls();
            binder.setBean(doctor);
            firstName.focus();
        });
        addBtn.addClickListener(event -> {
            Doctor doctor = new Doctor();
            binder.setBean(doctor);
            firstName.focus();
        });
    }

    @Override
    public void activateControls() {
        Button saveBtn = new Button("Save");
        Button delBtn = new Button("Delete");
        Button timeBtn = new Button("Edit Timeframe");
        Button cancelBtn = new Button("Cancel");
        HorizontalLayout buttonRow1 = new HorizontalLayout();
        HorizontalLayout buttonRow2 = new HorizontalLayout(timeBtn, cancelBtn);
        firstName.setPlaceholder(setup.getDoctor().getFirstName());
        lastName.setPlaceholder(setup.getDoctor().getLastName());
        buttonRow1.remove(editBtn);
        buttonRow1.add(saveBtn, delBtn);
        add(buttonRow1, buttonRow2);
        saveBtn.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            client.saveDoctor(doctor);
        });
        delBtn.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            client.deleteDoctor(doctor);
            clearForm();
        });
        timeBtn.addClickListener(event -> {

        });
        cancelBtn.addClickListener(event -> {

        });
    }

    public void removeButtons() {

    }

    @Override
    public void executeItem() {

    }

    public void delete() {

    }

    public void clearForm() {
        // fixme
    }

    void inactivateForm() {
        // fixme
    }
}
