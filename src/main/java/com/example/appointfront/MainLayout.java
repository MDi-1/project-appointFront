package com.example.appointfront;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.stereotype.Component;

@Component
public class MainLayout extends AppLayout {

    public MainLayout(Client client) {
        RouterLink firstPage = new RouterLink("FirstPage", FirstPage.class);
        firstPage.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink staffView = new RouterLink("DoctorView", StaffView.class);
        RouterLink patientView = new RouterLink("PatientView", PatientView.class);


        addToDrawer(new VerticalLayout(firstPage, staffView, patientView));
    }
}
