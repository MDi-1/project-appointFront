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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Route(value = "doctors", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private BackendClient client;
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    HorizontalLayout tables = new HorizontalLayout();

    public DoctorView(BackendClient client) {
        this.client = client;
        addClassName("doctor-view");
        setSizeFull();
        tables.setSizeFull();
        add(tables);
        createTables();
    }

    void createTables() { // - this f. is too long fixme
        LocalDate setDay = LocalDate.of(2022, 8, 17); // temporary value for target day in createTables
        Button sillyButton = new Button("silly button"); // temporary button for test purposes
        Label formLabel = new Label();
        VerticalLayout container = new VerticalLayout();
        LocalDate[] date = new LocalDate[7];
        String[] dayHeaders = new String[7];
        for (int n = 1; n < 8; n ++) {
            date[n - 1] = setDay.minusDays(setDay.getDayOfWeek().getValue() - n);
            dayHeaders[n - 1] = DayOfWeek.of(n).getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + "; " + date[n - 1];
        }
        for (int i = 0; i < 5; i ++) {
            Grid<TableEntry> timetable = new Grid<>(TableEntry.class);
            timetable.setItems(buildWeekDay(date[i])); // - this is spaghetti #1 fixme
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(dayHeaders[i]);  //-this is spaghetti #1 fixme
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            tables.add(timetable);
        }
        String[] weekdays = {"mon", "tue", "wed", "thu", "fri"};
        container.add(tables, createTimeForm(weekdays));
        container.setWidth("72%");
        String formHeaderTxt;
        if (client.getDoctor() == null) formHeaderTxt = "none selected";
        else formHeaderTxt = "selected: " + client.getDoctor().getFirstName() + " " + client.getDoctor().getLastName();
        formLabel.setText(formHeaderTxt);
        DoctorForm form = new DoctorForm(client.getMedServiceList());
        sillyButton.addClickListener(event -> {
            List<Appointment> list = client.getDocsAppointments();
            for (Appointment element : list) { System.out.println(element.toString()); } // temporary print
            List<TimeFrame> tfList = client.getDocsTimeFrames();
            for (TimeFrame element : tfList) { System.out.println(element.toString()); }
        });
        VerticalLayout containerVertical = new VerticalLayout(formLabel, form, sillyButton);
        containerVertical.setSizeFull();
        add(container, containerVertical);
    }

    TableEntry[] buildWeekDay(LocalDate weekdayDate) {  // - this f. is spaghetti #1 fixme
        int workDayHrsCount  = 12;
        TableEntry[] entries = new TableEntry[workDayHrsCount];
        List<TimeFrame> timeFrames;
        List<Appointment> appointments = null;
        LocalDate tfDate  = null;
        LocalTime tfStart = null;
        LocalTime tfEnd   = null;
        if (client.getDoctor() != null) {
            timeFrames   = client.getDocsTimeFrames();
            appointments = client.getDocsAppointments();
            int foundTf = 0;
            for (TimeFrame tf : timeFrames) {
                tfDate  = LocalDate.parse(tf.getDate());
                if (weekdayDate.equals(tfDate)) {
                    tfStart = LocalTime.parse(tf.getTimeStart());
                    tfEnd   = LocalTime.parse(tf.getTimeEnd());
                    foundTf ++;
                }
            }
        }
        for (int n = 0; n < workDayHrsCount; n ++) {
            LocalTime time = LocalTime.of(n + 6, 0);
            String status;
            if (tfDate == null) break;
            if (time.isBefore(tfStart) || time.isAfter(tfEnd)) status = "off";
            else status = time.getHour() + ":00 AVAILABLE";
            for (Appointment singleApp : appointments) {
                LocalDateTime appDateTime = LocalDateTime.parse(
                        singleApp.getStartDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm"));
                LocalDate appDate = LocalDate.from(appDateTime);
                LocalTime appTime = LocalTime.from(appDateTime);
                if (weekdayDate.equals(appDate) && time.equals(appTime)) status = time.getHour() + ":00 busy";
            }
            entries[n] = new TableEntry(weekdayDate, status, time, 15L, client.getPatient(), client.getDoctor());
        } return entries;
    }

    FormLayout createTimeForm(String[] weekdays) {
        Binder<TimeFrame> binder = new Binder<>(TimeFrame.class);
        HorizontalLayout bottomBar = new HorizontalLayout();
        for (int i = 0; i < 5; i++) {
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
        client.setDoctor(doctor);
        UI.getCurrent().navigate("doctors");
    }

    public void setDocFromTab(Doctor doctor) {
        binder.setBean(doctor);
    }
}