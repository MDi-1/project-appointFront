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

    private Setup setup;
    private BaseForm form;
    private BackendClient client;
    private LocalDate targetDate;
    private final TextField[] frameStart = new TextField[5];
    private final TextField[] frameEnd = new TextField[5];
    private final TimeFrame[] weekTimeFrames = new TimeFrame[5];
    private final List<Binder<TimeFrame>> tfBinderList = new ArrayList<>();
    private final Binder<Doctor> binder = new Binder<>(Doctor.class);
    HorizontalLayout weekTables = new HorizontalLayout();

    public DoctorView(BackendClient client, Setup setup) {
        this.client = client;
        this.setup = setup;
        if (setup.getDoctor() == null) {
            Optional<Doctor> firstDoc = client.getDoctorList().stream().findFirst();
            setup.setDoctor(Optional.of(firstDoc).get().orElseThrow(NotFoundException::new));
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
        if (setup.isAdmission()) form = new DoctorForm(client.getMedServiceList(), client, setup, DoctorView.this);
        else form = new AppointForm(client, setup);
        if (setup.getDoctor() == null) formHeaderTxt = "none selected";
        else formHeaderTxt = "selected: " + setup.getDoctor().getFirstName() + " " + setup.getDoctor().getLastName();
        formLabel.setText(formHeaderTxt);
        VerticalLayout container = new VerticalLayout();
        LocalDate[] date = new LocalDate[7];
        String[] dayHeaders = new String[7];
        for (int n = 1; n < 8; n ++) {
            LocalDate day = setup.getTargetDay();
            date[n - 1] = day.minusDays(day.getDayOfWeek().getValue() - n);
            String dayOfWeek = DayOfWeek.of(n).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStamp = date[n - 1].format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            dayHeaders[n - 1] =  dayOfWeek + "; " + dateStamp;
        }
        List<Grid<TableEntry>> timetables = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            Grid<TableEntry> timetable = new Grid<>(TableEntry.class);
            timetable.setItems(buildWeekDay(date[i], i)); // - this is spaghetti #1 fixme
            timetable.setColumns("status");
            timetable.getColumnByKey("status").setHeader(dayHeaders[i]);  //-this is spaghetti #1 fixme
            timetable.getColumnByKey("status").setSortable(false);
            timetable.setHeightFull();
            int finalI = i; // Intellij did this; I don't have better solution right now.
            timetable.asSingleSelect().addValueChangeListener(event -> {
                for (int k = 0; k < 5; k ++) {
                    if (k != finalI) timetables.get(k).deselectAll();
                }
                TableEntry entry = event.getValue();
                System.out.println(entry); // remove it later
                form.clearForm();               // main purpose of creating interface BaseForm was to call
                if (entry == null) return;      // f. as clearForm() or activateControls() from within this class
                setup.setEntry(entry);
                form.activateControls();
            });
            timetables.add(timetable);
            weekTables.add(timetables.get(i));
        }
        container.add(weekTables, createTimeForm());
        container.setWidth("72%");
        VerticalLayout containerVertical = new VerticalLayout(buildNavPanel(), formLabel, (Component) form);
        containerVertical.setSizeFull();
        add(container, containerVertical);
    }

    TableEntry[] buildWeekDay(LocalDate weekdayDate, int weekdayArrayPosition) {  // - this f. is spaghetti #1 fixme
        int workDayHrsCount  = 12;
        TableEntry[] entries = new TableEntry[workDayHrsCount];
        List<TimeFrame> timeFrames;
        List<Appointment> appointments = null;
        LocalTime tfStart = null, tfEnd = null;
        if (setup.getDoctor() != null) {
            timeFrames   = client.getDocsTimeFrames();
            appointments = client.getAppsByDoc();
            client.setDoctorAppList(appointments);
            for (TimeFrame tf : timeFrames) {
                if (weekdayDate.equals(LocalDate.parse(tf.getDate()))) {
                    tfStart = LocalTime.parse(tf.getTimeStart());
                    tfEnd   = LocalTime.parse(tf.getTimeEnd());
                    weekTimeFrames[weekdayArrayPosition] = tf;
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
            entries[n] = new TableEntry(weekdayDate, status, time, 15L, setup.getPatient(), setup.getDoctor(), appId);
        }
        return entries;
    }

    FormLayout buildNavPanel() {
        Button rwd = new Button("<<");
        Button fwd = new Button(">>");
        Button go2date = new Button("go to date");
        TextField dateField = new TextField(null, setup.getTargetDay().format(DateTimeFormatter.ofPattern("dd-M-yyyy")));
        rwd.addClickListener(event -> {
            targetDate = setup.getTargetDay().minusDays(7L);
            refresh();
        });
        fwd.addClickListener(event -> {
            targetDate = setup.getTargetDay().plusDays(7L);
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

    // (i) instead of writing new class for this form we can write the f. returning desired type;
    // given the desired type is already defined.
    FormLayout createTimeForm() {
        HorizontalLayout bottomBar = new HorizontalLayout();
        for (int i = 0; i < 5; i++) {
            Binder<TimeFrame> tfBinder = new Binder<>(TimeFrame.class);
            frameStart[i] = new TextField();
            frameEnd[i] = new TextField();
            if (weekTimeFrames[i] != null) tfBinder.setBean(weekTimeFrames[i]);
            else tfBinder.setBean(new TimeFrame("-", "-", "-", 0));
            tfBinder.bind(frameStart[i], "timeStart");
            tfBinder.bind(frameEnd[i], "timeEnd");
            frameStart[i].setEnabled(false);
            frameEnd[i].setEnabled(false);
            bottomBar.add(new VerticalLayout(frameStart[i], frameEnd[i]));
            tfBinderList.add(tfBinder);
        } return new FormLayout(bottomBar);
    }

    public void refresh() {//as soon as this project's finished refactor this f. to more civilized form to refresh view.
        setup.setTargetDay(targetDate);
        UI.getCurrent().getPage().reload();
    }

    public void enterDoctorManagement(Doctor doctor) {
        setDocFromTab(doctor);
        setup.setDoctor(doctor);
        UI.getCurrent().navigate("doctor");
    }

    public TextField[] getFrameStart() {
        return frameStart;
    }

    public List<Binder<TimeFrame>> getTfBinderList() {
        return tfBinderList;
    }

    public TextField[] getFrameEnd() {
        return frameEnd;
    }

    public void setDocFromTab(Doctor doctor) {
        binder.setBean(doctor);
    }
}