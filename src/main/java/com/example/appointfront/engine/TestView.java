package com.example.appointfront.engine;

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
        this.client = client;
    }

    public static void printTestList() { // target exercise f.
        client.getTestObjects().forEach(System.out::println);
    }
}
/*

This temporary class served as exercise of creating some code to be imported to MainLayout in form of static import
(to avoid creation of instance field for BackendClient in MainLayout). The following conditions must be met for this:
printTestList() needs to be made static, instance field 'client' needs to be static,
there has to be @Component annotation for this class.

 */
