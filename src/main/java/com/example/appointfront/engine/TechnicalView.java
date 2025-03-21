package com.example.appointfront.engine;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("admin | Tiny Clinic")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // (i) thanx to @Scope the view is refreshed on navigating to it, done
// by calling again constructor. Without it statement 'if (setup.getAdmission() > 2)...' wouldn't work, since this
// view would be created only once during application startup. NOTE - this is Spring annotation, not Vaadin.
public class TechnicalView extends VerticalLayout {

    public TechnicalView(BackendClient client) {
        Setup setup = Setup.SINGLETON_INSTANCE;
        if (setup.getAdmission() > 2) add(new MedServiceForm(client));
        else add(new NativeLabel("Access Denied; You need to log in as administrator to access this page"));
    }
}
