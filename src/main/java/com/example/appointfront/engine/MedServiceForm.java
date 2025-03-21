package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;
import java.util.stream.Collectors;

public class MedServiceForm extends FormLayout implements BaseForm{

    private Long docId;
    private final BackendClient client;
    private final IntegerField price = new IntegerField("price");
    private final TextField description = new TextField("description");
    private final ComboBox<Doctor> doctorBox = new ComboBox<>("associated doctor");
    private final ComboBox<MedicalService.ServiceName> serviceName = new ComboBox<>("serviceName");
    private final Binder<MedicalService> binder = new Binder<>(MedicalService.class);
    private final Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
    private final Button addService = new Button("add service");
    private final Button saveService = new Button("save service");
    private final Button deleteService = new Button("delete service");
    private final Button cancel = new Button("cancel");

    public MedServiceForm(BackendClient client) {
        this.client = client;
        configureFields();
        serviceGrid.asSingleSelect().addValueChangeListener(e -> {
            binder.setBean(e.getValue());
            activateControls();
        });
        addService.addClickListener(event -> {
            binder.setBean(new MedicalService());
            activateControls();
        });
        deleteService.addClickListener(event -> {
            client.deleteMedicalService(binder.getBean().getId());
            clearForm();
        });
        saveService.addClickListener(event -> executeItem());
        cancel.addClickListener(event -> clearForm());
        NativeLabel saveButtonHint = new NativeLabel("Hint: [save] button adds or removes doctor from medical " +
                "service entity whenever the one is selected from pull down menu");
        saveButtonHint.setMinWidth("1200px");
        add(new VerticalLayout(
                new HorizontalLayout(serviceGrid, new VerticalLayout(addService, saveService, deleteService, cancel)),
                saveButtonHint,
                new HorizontalLayout(serviceName, doctorBox, price, description))
        );
    }

    private void configureFields() {
        binder.bindInstanceFields(this);
        if (setup.getDoctors() == null) setup.setDoctors(client.getDoctorList());
        serviceGrid.setItems(client.getMedServiceList());
        serviceGrid.setMinWidth("1100px"); // (i) necessary on screen to prevent squashing by buttons on the side.
        serviceGrid.removeColumnByKey("doctorIds");
        serviceGrid.addColumn(ms -> ms.getDoctorIds().stream()
                .map(docId -> setup.getDoctors().stream()
                        .filter(d -> d.getId().equals(docId)).findAny().orElse(new Doctor()))
                .map(doc -> doc.getName() + " " + doc.getLastName())
                .collect(Collectors.toList())).setHeader("doctor's name").setAutoWidth(true);
        serviceGrid.getColumnByKey("id").setWidth("10%");
        serviceGrid.getColumnByKey("price").setWidth("10%");
        description.setWidthFull();
        doctorBox.setItems(client.getDoctorList());
        doctorBox.addValueChangeListener(e -> {
            if (e.getValue() != null) docId = e.getValue().getId();
        });
        price.setMin(50);
        price.setMax(990);
        serviceName.setItems(MedicalService.ServiceName.values());
        unlockMsFormControls(false);
    }

    @Override
    public void activateControls() {
        unlockMsFormControls(true);
    }

    @Override
    public void executeItem() {
        MedicalService ms = binder.getBean();
        Long msId;
        if (ms.getId() == null) {
            msId = client.createMedicalService(ms).getId();
        } else {
            msId = ms.getId();
            client.updateMedicalService(ms);
        }
        if (docId != null) {
            Doctor doctorToReceiveMs = setup.getDoctors().stream()
                    .filter(e -> e.getId().equals(docId)).findAny().orElseThrow(IllegalArgumentException::new);
            List<Long> msIdList = doctorToReceiveMs.getMedServiceIds();
            if (msIdList.stream().noneMatch(msId::equals)) msIdList.add(msId);
            else msIdList.remove(msId);
            doctorToReceiveMs.setMedServiceIds(msIdList);
            client.updateDoctor(doctorToReceiveMs);
        }
        clearForm();
    }

    @Override
    public void clearForm() {
        serviceGrid.setItems(client.getMedServiceList());
        doctorBox.setValue(null);
        binder.removeBean();
        unlockMsFormControls(false);
    }

    public void unlockMsFormControls(boolean buttonsActive) {
        addService.setEnabled(!buttonsActive);
        saveService.setEnabled(buttonsActive);
        deleteService.setEnabled(buttonsActive);
        cancel.setEnabled(buttonsActive);
    }
}
