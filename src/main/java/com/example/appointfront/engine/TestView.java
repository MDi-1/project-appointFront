package com.example.appointfront.engine;

import com.example.appointfront.data.MedicalService;
import com.example.appointfront.data.TestDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

import java.util.List;

@Route(value = "test", layout = MainLayout.class)
@PageTitle("admin | Tiny Clinic")
@Component
public class TestView extends HorizontalLayout {

    private static BackendClient client;

    public TestView(BackendClient client) {
        TestView.client = client;
        Grid<TestDto> table = new Grid<>(TestDto.class);
        Button buttonAdd = new Button("add test object");
        Button buttonPut = new Button("update test obj id = 109");
        TestDto t1 = new TestDto("final amendment");
        TestDto t2 = new TestDto(109L, "test object two: modified");
        buttonAdd.addClickListener(event -> System.out.println(client.createTestObject(t1)));
        buttonPut.addClickListener(event -> client.updateTestObject(t2));
        table.setItems(client.getTestObjects());
        table.setMinWidth("600px");
        table.getColumnByKey("id").setAutoWidth(true);
        table.getColumnByKey("name").setAutoWidth(true);
        table.getColumnByKey("name").setFlexGrow(1);
        VerticalLayout buttonLayout = new VerticalLayout(buttonAdd, buttonPut);
        HorizontalLayout maintenanceLayout = new HorizontalLayout(table, buttonLayout);
        maintenanceLayout.setAlignItems(Alignment.END);
        add(buildServiceForm(), maintenanceLayout);
    }

    public static void addFunctionality(HorizontalLayout header) {
        Button b1 = new Button("tmp test button-1");
        Button b2 = new Button("tmp test TF set");
        b1.addClickListener(event -> client.getTestObjects().forEach(System.out::println));
        b2.addClickListener(event -> DoctorForm.getTfProcessList().forEach(System.out::println));
        header.add(b1, b2);
    }

    private HorizontalLayout buildServiceForm() {
        TextField description = new TextField("description");
        ComboBox<MedicalService.ServiceName> name = new ComboBox<>();
        Button addService = new Button("add service");
        Button modifyService = new Button("modify service");
        Button deleteService = new Button("delete service");
        Button cancel = new Button("cancel");
        List<MedicalService> services = client.getMedServiceList();
        Grid<MedicalService> serviceGrid = new Grid<>(MedicalService.class);
        serviceGrid.setItems(services);
        serviceGrid.setMinWidth("500px"); // critical to prevent squashing by buttonContainer
        serviceGrid.getColumnByKey("id").setAutoWidth(true);
        addService.addClickListener(event -> {
            MedicalService s = new MedicalService(MedicalService.ServiceName.Dermatologist);
        });
        modifyService.addClickListener(event -> {});
        deleteService.addClickListener(event -> {});
        cancel.addClickListener(event -> {});
        VerticalLayout buttonContainer = new VerticalLayout(addService, modifyService, deleteService, cancel);
        HorizontalLayout container = new HorizontalLayout(serviceGrid, buttonContainer);
        container.setAlignItems(Alignment.END);
        return container;
    }
}

/*
This class served as exercise of creating some code to be imported to MainLayout in form of static import
(to avoid creation of instance field for BackendClient in MainLayout). The following conditions must be met for this:
printTestList() needs to be made static, instance field 'client' needs to be static, there has to be @Component
annotation for this class.
 */
