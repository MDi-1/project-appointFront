package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.TableEntry;
import com.example.appointfront.data.TimeFrame;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
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
import java.util.List;
import java.util.Locale;

@Route(value = "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private BackendClient client;
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    Label formLabel = new Label();
    Button sillyButton = new Button("silly button");
    HorizontalLayout tables = new HorizontalLayout();

    public DoctorView(BackendClient client) {
        this.client = client;
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
            timetable.setItems(buildWeekDay(dayTableHeaders[i]));
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(dayTableHeaders[i]);
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            tables.add(timetable);
        }
        String[] weekdays = {"mon", "tue", "wed", "thu", "fri"};
        container.add(tables, createTimeForm(weekdays));
        container.setWidth("72%");
        String formHeaderTxt;
        if (client.getCurrentDoctor() == null) formHeaderTxt = "none selected";
        else formHeaderTxt = "selected: " + client.getCurrentDoctor().getFirstName() +
                " " + client.getCurrentDoctor().getLastName();
        formLabel.setText(formHeaderTxt);
        DoctorForm form = new DoctorForm(client.getMedServiceList());
        sillyButton.addClickListener(event -> {
            List<Appointment> list = client.getDocsAppointments();
            for (Appointment element : list) { System.out.println(element.toString()); }
        });
        VerticalLayout containerVertical = new VerticalLayout(formLabel, form, sillyButton);
        containerVertical.setSizeFull();
        add(container, containerVertical);
    }

    TableEntry[] buildWeekDay(String weekday) {
        TableEntry[] entries = new TableEntry[8];
        for(int n = 0; n < 8; n ++) {
            int hr = n + 8;
            entries[n] = new TableEntry(
                    weekday, "busy", LocalTime.of(hr, 0), 15L, client.getCurrentPatient(), client.getCurrentDoctor());
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

    public void enterDoctorManagement(Doctor doctor) {
        setDocFromTab(doctor);
        client.setCurrentDoctor(doctor);
        UI.getCurrent().navigate("doctors");
        formLabel.setText("changed txt");
    }

    public void setDocFromTab(Doctor doctor) {
        binder.setBean(doctor);
    }
}