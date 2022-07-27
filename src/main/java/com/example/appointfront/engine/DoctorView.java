package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.TableEntry;
import com.example.appointfront.data.TimeFrame;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Route(value = "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private Doctor currentDoctor;
    private Patient currentPatient;
    private BackendClient backendClient;
    HorizontalLayout tables = new HorizontalLayout();

    public DoctorView(BackendClient backendClient) {
        this.backendClient = backendClient;
        addClassName("doctor-view");
        setSizeFull();
        tables.setSizeFull();
        add(tables);
        createTables();
    }

    void createTables() {
        VerticalLayout container = new VerticalLayout();
        LocalDate today = LocalDate.now();
        long thisDayNum = today.getDayOfWeek().getValue();
        String[] dayTableHeaders = new String[7];
        for(int n = 1; n < 8; n ++) {
            String dayName = DayOfWeek.of(n).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            dayTableHeaders[n - 1] = dayName + "; " + today.minusDays(thisDayNum - n);
        }
        for(int i = 0; i < 5; i ++) {
            Grid<TableEntry> timetable = new Grid<>(TableEntry.class);
            timetable.setItems(getEntries(dayTableHeaders[i]));
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(dayTableHeaders[i]);
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            tables.add(timetable);
        }
        String[] weekdays = {"mon", "tue", "wed", "thu", "fri"};
        container.add(tables, createTimeForm(weekdays));
        container.setWidth("72%");
        DoctorForm form = new DoctorForm(backendClient.getMedServiceList());
        form.setWidth("27%");
        add(container, form);
    }

    TableEntry[] getEntries(String weekday) {
        TableEntry[] entries = new TableEntry[8];
        for(int n = 0; n < 8; n ++) {
            int hr = n + 8;
            entries[n] = new TableEntry(weekday, "busy", LocalTime.of(hr, 0), 15L, currentPatient, currentDoctor);
        } return entries;
    }

    FormLayout createTimeForm(String[] weekdays) {
        Binder<TimeFrame> binder = new Binder<>(TimeFrame.class);
        HorizontalLayout bottomBar = new HorizontalLayout();
        for(int i = 0; i < 5; i++) {
            TextField start = new TextField();
            TextField end = new TextField();
            start.setPlaceholder(weekdays[i] + " from");
            end.setPlaceholder(weekdays[i] + " to");
            binder.bind(start, "timeStart");
            binder.bind(end, "timeEnd");
            VerticalLayout form = new VerticalLayout(start, end);
            bottomBar.add(form);
        } return new FormLayout(bottomBar);
    }
}