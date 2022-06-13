package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.TableEntry;
import com.example.appointfront.data.TimeFrame;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Route(value = "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private Doctor currentDoctor;
    private Patient currentPatient;
    private BackendClient backendClient;
    HorizontalLayout tables = new HorizontalLayout();

    public DoctorView(BackendClient backendClient) {
        this.backendClient = backendClient;
        Label label = new Label("this some txt");
        addClassName("doctor-view");
        setSizeFull();
        tables.setSizeFull();
        add(tables);
        createTables();
    }

    void createTables() {
        VerticalLayout container = new VerticalLayout();
        String[] weekdays = {"mon", "tue", "wed", "thu", "fri"};
        Grid<SideEntry> sideGrid = new Grid<>(SideEntry.class);  //fixme - object sideGrid is a dead code
        sideGrid.setItems(getSideGrid());
        sideGrid.setColumns("parsedTime");
        sideGrid.getColumnByKey("parsedTime").setHeader("h");
        sideGrid.getColumnByKey("parsedTime").setSortable(false);
        sideGrid.setHeightFull();
        //tables.add(sideGrid);
        for(int i = 0; i < 5; i ++) {
            Grid<TableEntry> timetable = new Grid<>(TableEntry.class);
            timetable.setItems(getEntries(weekdays[i]));
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(weekdays[i]);
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            tables.add(timetable);
        }
        container.add(tables, createTimeForm(weekdays));
        container.setWidth("74%");
        DoctorForm form = new DoctorForm(backendClient.getMedServiceList());
        form.setWidth("26%");
        add(container, form);
    }

    SideEntry[] getSideGrid() {  //fixme - dead function
        SideEntry[] sideTable = new SideEntry[16];
        for(int n = 0; n < 16; n ++) {
            int min;
            int hr = n / 2 + 8;
            if (n % 2 != 0) min = 30;
            else min = 0;
            String timeText = LocalTime.of(hr, min).format(DateTimeFormatter.ofPattern("HH:mm"));
            sideTable[n] = new SideEntry(timeText);
        }
        return sideTable;
    }

    TableEntry[] getEntries(String weekday) {
        TableEntry[] entries = new TableEntry[16];
        for(int n = 0; n < 8; n ++) {
            int min = 0;
            int hr = n + 8;
            // if(n % 2 != 0) min = 30; else min = 0;
            entries[n] = new TableEntry(weekday, "busy", LocalTime.of(hr, min), 15L, currentPatient, currentDoctor);
        }
        return entries;
    }

    FormLayout createTimeForm(String[] weekdays) {
        Binder<TimeFrame> binder = new Binder<>(TimeFrame.class);
        HorizontalLayout bottomBar = new HorizontalLayout();
        for(int i = 0; i < 5; i++) {
            TextField start = new TextField();
            TextField end = new TextField();
            start.setPlaceholder(weekdays[i] + " from");
            end.setPlaceholder(weekdays[i] + " to");
            binder.bind(start, "start" + i);
            binder.bind(end, "end" + i);
            VerticalLayout form = new VerticalLayout(start, end);
            bottomBar.add(form);
        }
        return new FormLayout(bottomBar);
    }

    TableEntry[] getEntries_old() {
        return Stream.of(
                new TableEntry("mon", "busy", LocalTime.of(8, 0),   15L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(8, 30),  15L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(9, 0),   30L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(9, 30),  15L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(10, 0),  30L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(10, 30), 15L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(11, 0),  30L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(11, 30), 30L, currentPatient, currentDoctor),
                new TableEntry("mon", "busy", LocalTime.of(12, 0),  15L, currentPatient, currentDoctor)
        ).toArray(TableEntry[]::new);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class SideEntry {
        private String parsedTime;
    }
}