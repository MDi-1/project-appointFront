package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.MedicalService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.ArrayList;
import java.util.List;

public class MServiceForm extends FormLayout {

    private Long docId;
    private final TextField description = new TextField("description");
    private final ComboBox<Doctor> doctorBox = new ComboBox<>("associated doctor");
    private final ComboBox<MedicalService.ServiceName> serviceName = new ComboBox<>("serviceName");
    private final Binder<MedicalService> binder = new Binder<>(MedicalService.class);

    public MServiceForm(BackendClient client) {
        Setup setup = Setup.SINGLETON_INSTANCE;
        configureFields(client);
        Button addService = new Button("add service");
        Button saveService = new Button("save service");
        Button deleteService = new Button("delete service");
        Button cancel = new Button("cancel");
        List<MedicalService> msList = client.getMedServiceList();
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        serviceGrid.setItems(msList);
        serviceGrid.setMinWidth("520px"); // critical to prevent squashing by buttons on the side.
        serviceGrid.removeColumnByKey("doctorIds");
        serviceGrid.getColumnByKey("id").setAutoWidth(true);
        serviceGrid.asSingleSelect().addValueChangeListener(e -> binder.setBean(e.getValue()));
        /*
        serviceGrid.addColumn(row -> {
            setup.getDoctors().stream().filter(e -> e.getId().equals(
                    msList.stream().map(MedicalService::getDoctorIds).anyMatch()
            ))
            return null;
                }).setHeader("Doctor");

         */

        addService.addClickListener(event -> {
            binder.setBean(new MedicalService());
            serviceGrid.setItems(client.getMedServiceList());
        });
        saveService.addClickListener(event -> {
            MedicalService ms = binder.getBean();
            if (ms.getDoctorIds() != null) ms.getDoctorIds().add(docId);
            else {
                ms.setDoctorIds(new ArrayList<>());
                ms.getDoctorIds().add(docId);
            }
            System.out.println(ms);
            MedicalService response = null;
            if (ms.getId() == null) response = client.createMedicalService(ms);
            else client.updateMedicalService(ms);
            System.out.println(" ]] response = " + response);
            serviceGrid.setItems(client.getMedServiceList()); // refresh
            binder.removeBean();
        });
        deleteService.addClickListener(event -> {
            client.deleteMedicalService(binder.getBean().getId());
            serviceGrid.setItems(client.getMedServiceList());
            binder.removeBean();
        });
        cancel.addClickListener(event -> binder.removeBean());
        add(new VerticalLayout(
                new HorizontalLayout(serviceGrid, new VerticalLayout(addService, saveService, deleteService, cancel)),
                new HorizontalLayout(serviceName, doctorBox, description))
        );
    }

    private void configureFields(BackendClient client) {
        binder.bindInstanceFields(this);
        description.setWidthFull();
        doctorBox.setItems(client.getDoctorList());
        doctorBox.addValueChangeListener(e -> {
            docId = e.getValue().getId();
            System.out.println(docId);
        });
        serviceName.setItems(MedicalService.ServiceName.values());
    }
}
