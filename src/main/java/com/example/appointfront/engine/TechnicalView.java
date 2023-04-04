package com.example.appointfront.engine;

import com.example.appointfront.data.TestDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("admin | Tiny Clinic")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // thanx to @Scope the view is refreshed on navigating to it, done by
// calling again constructor. Without it statement 'if (setup.getAdmission() > 2)...' wouldn't work, since this view
// would be created only once during application startup. NOTE - this is Spring annotation, not Vaadin.
public class TechnicalView extends VerticalLayout {

    public TechnicalView(BackendClient client, Setup setup) {
        if (setup.getAdmission() > 2) {
            Grid<TestDto> table = new Grid<>(TestDto.class);
            Button buttonAdd = new Button("add test object");
            Button buttonPut = new Button("update test obj id = 109");
            TestDto t1 = new TestDto("final amendment");
            TestDto t2 = new TestDto(109L, "test object two: modified");
            buttonAdd.addClickListener(event -> System.out.println(client.createTestObject(t1)));
            buttonPut.addClickListener(event -> client.updateTestObject(t2));
            table.setItems(client.getTestObjects());
            table.setMinWidth("520px"); // critical to prevent squashing by buttonContainer
            table.setMaxWidth("580px");
            table.getColumnByKey("id").setAutoWidth(true);
            table.getColumnByKey("name").setAutoWidth(true);
            table.getColumnByKey("name").setFlexGrow(1);
            VerticalLayout buttonLayout = new VerticalLayout(buttonAdd, buttonPut);
            HorizontalLayout maintenanceLayout = new HorizontalLayout(table, buttonLayout);
            add(new MServiceForm(client, setup), maintenanceLayout);
        } else {
            Label txt = new Label("Access Denied; You need to log in as administrator to access this page");
            add(txt);
        }
    }
}
