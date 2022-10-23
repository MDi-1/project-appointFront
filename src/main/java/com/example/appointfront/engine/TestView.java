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
@PageTitle("Test | Tiny Clinic")
@Component
public class TestView extends VerticalLayout { // this thing to be removed later

    private static BackendClient client;

    public TestView(BackendClient client) {
        TestView.client = client;
        Grid<TestDto> table = new Grid<>(TestDto.class);
        Button buttonAdd = new Button("add test object");
        TestDto t1 = new TestDto("final amendment");
        buttonAdd.addClickListener(event -> {
            TestDto response = client.createTestObject(t1);
            System.out.println(response);
        });
        table.setItems(client.getTestObjects());
        add(table, buttonAdd);
    }

    public static void addFunctionality(HorizontalLayout header) {
        Button b1 = new Button("tmp test button-1");
        header.add(b1);
        b1.addClickListener(event -> client.getTestObjects().forEach(System.out::println));
    }
}
/*

This temporary class served as exercise of creating some code to be imported to MainLayout in form of static import
(to avoid creation of instance field for BackendClient in MainLayout). The following conditions must be met for this:
printTestList() needs to be made static, instance field 'client' needs to be static,
there has to be @Component annotation for this class.

 */
