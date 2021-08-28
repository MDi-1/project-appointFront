package com.example.appointfront.engine;

import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.Patient;
import com.example.appointfront.data.SideEntry;
import com.example.appointfront.data.TableEntry;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Route(value = "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends VerticalLayout {

    private Doctor currentDoctor;
    private Patient currentPatient;
    private BackendClient backendClient;

    public DoctorView() {
        addClassName("doctor-view");
        setHeightFull();
        DoctorForm form = new DoctorForm(backendClient.getMedServiceList()); // to nie jest jak w tutorialach / fixme
        processTables();
    }

    void processTables() {
        String[] weekdays = {"mon", "tue", "wed", "thu", "fri"};
        Grid<SideEntry> sideGrid = new Grid<>(SideEntry.class);
        sideGrid.setItems(getSideGrid());
        sideGrid.setColumns("parsedTime");
        sideGrid.getColumnByKey("parsedTime").setHeader("h");
        sideGrid.getColumnByKey("parsedTime").setSortable(false);
        sideGrid.setHeightFull();
        add(sideGrid);
        for(int i = 0; i < 5; i ++) {
            Grid<TableEntry> timetable = new Grid<>(TableEntry.class);
            timetable.setItems(getEntries(weekdays[i]));
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(weekdays[i]);
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            add(timetable);
        }
    }

    SideEntry[] getSideGrid() {
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
        for(int n = 0; n < 16; n ++) {
            int min;
            int hr = n / 2 + 8;
            if(n % 2 != 0) min = 30;
            else min = 0;
            entries[n] = new TableEntry(weekday, "busy", LocalTime.of(hr, min), 15L, currentPatient, currentDoctor);
        }
        return entries;
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
}
