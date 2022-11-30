package com.example.appointfront.engine;

import com.example.appointfront.data.TestDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Route(value = "test", layout = MainLayout.class)
@PageTitle("admin | Tiny Clinic")
@Component
public class TestView extends VerticalLayout { // this thing to be removed later

    private static BackendClient client;
    private static DoctorForm docForm;

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
        add(table, buttonAdd, buttonPut);
    }

    public static void addFunctionality(HorizontalLayout header) {
        Button b1 = new Button("tmp test button-1");
        Button b2 = new Button("tmp test TF set");
        b1.addClickListener(event -> client.getTestObjects().forEach(System.out::println));
        b2.addClickListener(event -> docForm.getTfProcessSet().forEach(System.out::println));
        header.add(b1, b2);
    }
}
/*

This temporary class served as exercise of creating some code to be imported to MainLayout in form of static import
(to avoid creation of instance field for BackendClient in MainLayout). The following conditions must be met for this:
printTestList() needs to be made static, instance field 'client' needs to be static,
there has to be @Component annotation for this class.

 */
