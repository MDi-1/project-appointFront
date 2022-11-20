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
    private boolean exeMode;
    private TextField firstName = new TextField("first name");
    private TextField lastName = new TextField("last name");
    private HorizontalLayout buttonRow = new HorizontalLayout();
    private ComboBox<Doctor.Position> position = new ComboBox<>("position");
    private ComboBox<MedicalService> services = new ComboBox<>("medical services");
    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    public DoctorForm(List<MedicalService> medicalServices, BackendClient client, Setup setup) {
        this.client = client;
        this.setup = setup;
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit Personal Data");
        Button timeBtn = new Button("Edit Timeframes");
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
            exeMode = false;
        });
    }

    @Override
    public void activateControls() {
        Button saveBtn = new Button("Save");
        Button delBtn = new Button("Delete");
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
            System.out.println(" ]] Project-Appoint [[ doc created: " + doc);
        } else {
            Doctor doc = client.updateDoctor(doctor);
            System.out.println(" ]] Project-Appoint [[ doc updated: " + doc);
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
    }
}
