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

import java.util.ArrayList;
import java.util.List;

public class DoctorForm extends FormLayout implements BaseForm{

    private final BackendClient client;
    private final DoctorView view;
    private final Setup setup;
    private boolean exeMode;
    private final TextField firstName = new TextField("first name");
    private final TextField lastName = new TextField("last name");
    private ComboBox<Doctor.Position> position = new ComboBox<>("position");
    private final HorizontalLayout buttonRow = new HorizontalLayout();
    private final Binder<Doctor> binder = new Binder<>(Doctor.class);
    private final List<TimeFrame> tfProcessList = new ArrayList<>();
    private final Button addBtn = new Button("Add");
    private final Button editBtn = new Button("Edit Personal Data");
    private final Button timeBtn = new Button("Edit Timeframes");

    public DoctorForm(BackendClient client, Setup setup, DoctorView view) {
        this.client = client;
        this.setup = setup;
        this.view = view;
        addClassName("doctor-form");
        binder.bindInstanceFields(this);
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
        toggleLocks(true);
        binder.setBean(setup.getDoctor());
        tfProcessList.clear();
        prepareTfSet(view.getFrameStart());
        prepareTfSet(view.getFrameEnd());
        saveTfBtn.addClickListener(event -> executeTimeFrames());
        delTfButton.addClickListener(event -> {
            Doctor doctor = binder.getBean();
            //client.deleteTf(doctor.getId());
            clearForm();
        });
        cancelTfBtn.addClickListener(event -> clearForm());
        setBtn.addClickListener(event -> getTfProcessList().forEach(System.out::println));
        exeMode = false;
        buttonRow.add(saveTfBtn, delTfButton, cancelTfBtn, setBtn);
        add(buttonRow);
    }

    // (i) this f. WAS the example how to make toggle switch when it looked like this:
    // setup.setTimetableLock(!setup.isTimetableLock());
    public void toggleLocks(boolean lock) {
        clearForm();
        setup.setTimetableLock(lock);
        view.lockTimetables(setup.isTimetableLock());
        addBtn.setEnabled(!lock);
        editBtn.setEnabled(!lock);
        timeBtn.setEnabled(!lock);
    }

    void prepareTfSet(TextField[] array) {
        int i = 0;
        for (TextField field : array) {
            int x = i;
            field.setEnabled(true);
            field.addValueChangeListener(action -> {
                TimeFrame tfx = view.getTfBinderList().get(x).getBean();
                TimeFrame tfSubtract = null;
                for (TimeFrame tf : tfProcessList) {
                    if (tf.getDate().equals(tfx.getDate())) {
                        tfSubtract = tf;
                        System.out.println(" ]] about to exchange tf: " + tf);
                    }
                } // for some reason f.remove() does not work on Set. 2 B done later - do it on Set instead of List
                tfProcessList.remove(tfSubtract);
                tfProcessList.add(tfx);
                System.out.println(" ]] added to set: " + tfx);
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
        if (exeMode) client.createDoctor(doctor);
        else client.updateDoctor(doctor);
        clearForm();
    }

    void executeTimeFrames() {
        for (TimeFrame item : tfProcessList) {
            if (item.getId() != null) {
                client.updateTimeframe(item);
            }
            else {
                TimeFrame response = client.createTimeFrame(item);
                System.out.println(response);
            }
        }
        tfProcessList.clear();
        clearForm();
        toggleLocks(false);
    }

    public void clearForm() {
        buttonRow.removeAll();
        remove(buttonRow);
        if (setup.getDoctor() != null) {
            firstName.setPlaceholder(setup.getDoctor().getFirstName());
            lastName.setPlaceholder(setup.getDoctor().getLastName());
        }
    }

    public List<TimeFrame> getTfProcessList() {
        return tfProcessList;
    }
}
