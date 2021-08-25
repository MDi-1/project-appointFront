package com.example.appointfront;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value="", layout = MainLayout.class)
public class FirstPage extends HorizontalLayout {

    private final Client client;

    public FirstPage(Client client) {
        this.client = client;
    }
}
