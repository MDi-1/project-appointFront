package com.example.appointfront.engine;

import com.example.appointfront.data.Appointment;
import com.example.appointfront.data.Doctor;
import com.example.appointfront.data.TableEntry;
import com.example.appointfront.data.TimeFrame;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Route(value = "doctor", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private BackendClient client;
    private LocalDate targetDate;
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    HorizontalLayout weekTables = new HorizontalLayout();

    public DoctorView(BackendClient client) {
        this.client = client;
        if (client.getDoctor() == null) {
            Optional<Doctor> firstDoc = client.getDoctorList().stream().findFirst();
            client.setDoctor(Optional.of(firstDoc).get().orElseThrow(NotFoundException::new));
        }
        addClassName("doctor-view");
        setSizeFull();
        weekTables.setSizeFull();
        add(weekTables);
        createTables();
    }

    void createTables() { // - this f. is too long fixme
        Label formLabel = new Label();
        String formHeaderTxt;
        BaseForm form;
        if (client.isAdmission()) form = new DoctorForm(client.getMedServiceList());
        else form = new AppointForm(client);
        if (client.getDoctor() == null) formHeaderTxt = "none selected";
        else formHeaderTxt = "selected: " + client.getDoctor().getFirstName() + " " + client.getDoctor().getLastName();
        formLabel.setText(formHeaderTxt);
        VerticalLayout container = new VerticalLayout();
        LocalDate[] date = new LocalDate[7];
        String[] dayHeaders = new String[7];
        String[] shortenedDayHeaders = Arrays.copyOfRange(dayHeaders, 0, 5);
        for (int n = 1; n < 8; n ++) {
            LocalDate day = client.getSetDay();
            date[n - 1] = day.minusDays(day.getDayOfWeek().getValue() - n);
            String dayOfWeek = DayOfWeek.of(n).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStamp = date[n - 1].format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            dayHeaders[n - 1] =  dayOfWeek + "; " + dateStamp;
        }
        List<Grid<TableEntry>> timetables = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            // zrobić 5 elementową tablicę obiektów timetable wówczas będzie można za pomocą polecenia:
            // timetable[].deselect() wyłączać zaznaczenie określonej timetable.
            Grid<TableEntry> timetable = new Grid<>(TableEntry.class);
            timetable.setItems(buildWeekDay(date[i])); // - this is spaghetti #1 fixme
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(dayHeaders[i]);  //-this is spaghetti #1 fixme
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            int finalI = i; // Intellij did this; I don't have better solution right now.
            timetable.asSingleSelect().addValueChangeListener(event -> {
                TableEntry entry = event.getValue();
                System.out.println(entry);
                for (int k = 0; k < 5; k ++) {
                    if (k != finalI) timetables.get(k).deselectAll();
                }
                client.setEntry(entry);
                client.setSetDay(entry.getWeekday());
                form.setButtons();
            });
            timetables.add(timetable);
            weekTables.add(timetables.get(i));
        }
        container.add(weekTables, createTimeForm(shortenedDayHeaders));
        container.setWidth("72%");
        VerticalLayout containerVertical = new VerticalLayout(buildNavPanel(), formLabel, (Component) form);
        containerVertical.setSizeFull();
        add(container, containerVertical);
    }

    TableEntry[] buildWeekDay(LocalDate weekdayDate) {  // - this f. is spaghetti #1 fixme
        int workDayHrsCount  = 12;
        TableEntry[] entries = new TableEntry[workDayHrsCount];
        List<TimeFrame> timeFrames;
        List<Appointment> appointments = null;
        LocalTime tfStart = null, tfEnd = null;
        if (client.getDoctor() != null) {
            timeFrames   = client.getDocsTimeFrames();
            appointments = client.getDocsAppointments();
            for (TimeFrame tf : timeFrames) {
                if (weekdayDate.equals(LocalDate.parse(tf.getDate()))) {
                    tfStart = LocalTime.parse(tf.getTimeStart());
                    tfEnd   = LocalTime.parse(tf.getTimeEnd());
                }
            }
        }
        for (int n = 0; n < workDayHrsCount; n ++) {
            LocalTime time = LocalTime.of(n + 6, 0);
            String status;
            long appId = 0;
            if (tfStart == null) status = "n/a";
            else {
                if (time.isBefore(tfStart) || time.isAfter(tfEnd)) status = "off";
                else status = time.getHour() + ":00 AVAILABLE";
                for (Appointment singleApp : appointments) {
                    LocalDateTime appDateTime = LocalDateTime.parse(
                            singleApp.getStartDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm"));
                    LocalDate appDate = LocalDate.from(appDateTime);
                    LocalTime appTime = LocalTime.from(appDateTime);
                    if (weekdayDate.equals(appDate) && time.equals(appTime)) {
                        status = time.getHour() + ":00 busy";
                        appId = singleApp.getId();
                    }
                }
            }
            entries[n] = new TableEntry(weekdayDate, status, time, 15L, client.getPatient(), client.getDoctor(), appId);
        }
        return entries;
    }

    FormLayout buildNavPanel() {
        Button rwd = new Button("<<");
        Button fwd = new Button(">>");
        Button go2date = new Button("go to date");
        TextField dateField = new TextField(null, client.getSetDay().format(DateTimeFormatter.ofPattern("dd-M-yyyy")));
        rwd.addClickListener(event -> {
            targetDate = client.getSetDay().minusDays(7L);
            refresh();
        });
        fwd.addClickListener(event -> {
            targetDate = client.getSetDay().plusDays(7L);
            refresh();
        });
        go2date.addClickListener(event -> {
            targetDate = LocalDate.parse(dateField.getValue(), DateTimeFormatter.ofPattern("dd-M-yyyy"));
            refresh();
        });
        HorizontalLayout horizontal = new HorizontalLayout(rwd, dateField, fwd);
        VerticalLayout navPanel = new VerticalLayout(horizontal, go2date);
        horizontal.setAlignItems(Alignment.START);
        navPanel.setAlignItems(Alignment.CENTER);
        return new FormLayout(navPanel);
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

    // as soon as this project is finished refactor this f. into more civilized form to refresh view.
    public void refresh() {
        client.setSetDay(targetDate);
        UI.getCurrent().getPage().reload();
    }

    public void enterDoctorManagement(Doctor doctor) {
        setDocFromTab(doctor);
        client.setDoctor(doctor);
        UI.getCurrent().navigate("doctor");
    }

    public void setDocFromTab(Doctor doctor) {
        binder.setBean(doctor);
    }
}