package com.example.appointfront.engine;

import com.example.appointfront.data.*;
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

import static com.example.appointfront.engine.DoctorForm.getTfProcessList;

@Route(value = "doctor", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private final Setup setup;
    private final BaseForm form;
    private final BackendClient client;
    private LocalDate targetDate;
    private final TextField[] frameStart = new TextField[5];
    private final TextField[] frameEnd = new TextField[5];
    private TimeFrame[] weekTimeFrames = new TimeFrame[5];
    private final List<Binder<TimeFrame>> tfBinderList = new ArrayList<>();
    private final Binder<Doctor> binder = new Binder<>(Doctor.class);
    private List<Grid<TableEntry>> timetable = new ArrayList<>();
    private final Label lockLabel = new Label("timetable unlocked");
    private final String[] dayHeaders = new String[7];
    private LocalDate[] date4tfForm;
    private final HorizontalLayout weekTables = new HorizontalLayout();
    private final TextField navPanelField = new TextField();

    public DoctorView(BackendClient client) {
        this.client = client;
        setup = Setup.SINGLETON_INSTANCE;
        VerticalLayout container = new VerticalLayout();
        Label formLabel = new Label();
        String formHeaderTxt;
        if (setup.getAdmission() > 1) form = new DoctorForm(client, DoctorView.this);
        else form = new AppointForm(client, DoctorView.this);
        if (setup.getDoctor() == null) formHeaderTxt = "none selected";
        else formHeaderTxt = "selected: " + setup.getDoctor().getName() + " " + setup.getDoctor().getLastName();
        formLabel.setText(formHeaderTxt);
        if (setup.getDoctor() == null) {
            Optional<Doctor> firstDoc = client.getDoctorList().stream().findFirst();
            setup.setDoctor(Optional.of(firstDoc).get().orElseThrow(NotFoundException::new));
        }
        addClassName("doctor-view");
        setSizeFull();
        weekTables.setSizeFull();
        createTables();

        VerticalLayout rightContainer = new VerticalLayout(
                buildNavPanel(), lockLabel, formLabel, (Component) form);
        rightContainer.setSizeFull();
        container.add(weekTables, createTimeForm());
        container.setWidth("72%");
        add(container, rightContainer);
    }

    void createTables() {
        setup.setTimetableLock(false);
        for (int i = 0; i < 5; i ++) {
            timetable.add(new Grid<>(TableEntry.class));
            timetable.get(i).setColumns("status");
            timetable.get(i).getColumnByKey("status").setSortable(false);
            timetable.get(i).setHeightFull();
            int y = i;
            timetable.get(i).asSingleSelect().addValueChangeListener(event -> {
                for (int k = 0; k < 5; k ++) {
                    if (k != y) timetable.get(k).deselectAll();
                }
                TableEntry entry = event.getValue();
                form.clearForm();               // main purpose of creating interface BaseForm was to call
                if (entry == null) return;      // f. as clearForm() or activateControls() from within this class
                setup.setEntry(entry);
                form.activateControls();
            });
            weekTables.add(timetable.get(i));
        }
        LocalDate[] dateArray = refreshHeaders();
        refreshTables(dateArray);
    }

    private LocalDate[] refreshHeaders() { // fixme
        LocalDate[] date = new LocalDate[7];
        for (int n = 1; n < 8; n ++) {
            LocalDate day = setup.getTargetDay();
            date[n - 1] = day.minusDays(day.getDayOfWeek().getValue() - n);
            String dayOfWeek = DayOfWeek.of(n).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStamp = date[n - 1].format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            dayHeaders[n - 1] =  dayOfWeek + "; " + dateStamp;
        }
        date4tfForm = Arrays.copyOfRange(date, 0, 5);
        return date;
    }

    private void refreshTables(LocalDate[] date) { // fixme
        for (int i = 0; i < 5; i ++) {
            timetable.get(i).setItems(buildWeekDay(date[i], i)); // - this is spaghetti #1 fixme
            timetable.get(i).getColumnByKey("status").setHeader(dayHeaders[i]);  //-this is spaghetti #1 fixme
        }
    }

    void forceRefresh() {
        weekTimeFrames = new TimeFrame[5];
        refreshNavPanel();
        LocalDate[] dateArray = refreshHeaders();
        refreshTables(dateArray);
        refreshTimeForm();
    }

    private TableEntry[] buildWeekDay(LocalDate weekdayDate, int weekdayArrayPosition) { //-this f. is spaghetti fixme
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
            Patient patient = null;
            Long appId = null;
            if (tfStart == null) status = "n/a";
            else {
                if (time.isBefore(tfStart) || time.isAfter(tfEnd) || time.equals(tfEnd)) status = "off";
                else status = time.getHour() + ":00 -AVAILABLE-";
                for (Appointment singleApp : appointments) {
                    LocalDateTime appDateTime = LocalDateTime.parse(
                            singleApp.getStartDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm"));
                    LocalDate appDate = LocalDate.from(appDateTime);
                    LocalTime appTime = LocalTime.from(appDateTime);
                    if (weekdayDate.equals(appDate) && time.equals(appTime)) {
                        patient = setup.getPatients().stream().filter(e -> e.getId().equals(singleApp.getPatientId()))
                                .findFirst().orElseThrow(IllegalArgumentException::new);
                        appId = singleApp.getId();
                        if (patient.equals(setup.getPatient())) {
                            status = time.getHour() + ":00 Appointed";
                        } else status = "busy";
                    }
                }
            }
            entries[n] = new TableEntry(weekdayDate, status, time, 15L, patient, setup.getDoctor(), appId);
        }
        return entries;
    }

    FormLayout buildNavPanel() {
        Button rwd = new Button("<<");
        Button fwd = new Button(">>");
        Button go2date = new Button("go to date");
        refreshNavPanel();
        rwd.addClickListener(event -> {
            targetDate = setup.getTargetDay().minusDays(7L);
            forceRefresh();
        });
        fwd.addClickListener(event -> {
            targetDate = setup.getTargetDay().plusDays(7L);
            forceRefresh();
        });
        go2date.addClickListener(event -> {
            targetDate = LocalDate.parse(navPanelField.getValue(), DateTimeFormatter.ofPattern("dd-M-yyyy"));
            forceRefresh();
        });
        HorizontalLayout horizontal = new HorizontalLayout(rwd, navPanelField, fwd);
        VerticalLayout navPanel = new VerticalLayout(horizontal, go2date);
        horizontal.setAlignItems(Alignment.START);
        navPanel.setAlignItems(Alignment.CENTER);
        return new FormLayout(navPanel);
    }

    private void refreshNavPanel() {
        if (targetDate != null) setup.setTargetDay(targetDate);
        navPanelField.setPlaceholder(setup.getTargetDay().format(DateTimeFormatter.ofPattern("dd-M-yyyy")));
    }

    // (i) instead of writing new class for this form we can write the f. returning desired type;
    // given the desired type is already defined.
    FormLayout createTimeForm() {
        HorizontalLayout bottomBar = new HorizontalLayout();
        for (int i = 0; i < 5; i++) {
            Binder<TimeFrame> tfBinder = new Binder<>(TimeFrame.class);
            tfBinderList.add(tfBinder);
            frameStart[i] = new TextField();
            frameEnd[i] = new TextField();
            frameStart[i].setEnabled(false);
            frameEnd[i].setEnabled(false);
            int x = i;
            frameStart[i].addValueChangeListener(action -> tfProcessing(x));
            frameEnd[i].addValueChangeListener(action -> tfProcessing(x));
            bottomBar.add(new VerticalLayout(frameStart[i], frameEnd[i]));
        }
        refreshTimeForm();
        return new FormLayout(bottomBar);
    }

    private void tfProcessing(int x) {
        if (!frameStart[x].isEnabled()) return;
        TimeFrame tfx = getTfBinderList().get(x).getBean();
        TimeFrame tfSubtract = null;
        for (TimeFrame tf : getTfProcessList()) {
            if (tf.getDate().equals(tfx.getDate())) {
                tfSubtract = tf;
                System.out.println(" ]] about to exchange tf: " + tf);
            }
        } // for some reason f.remove() does not work on Set. 2 B done later - do it on Set instead of List
        getTfProcessList().remove(tfSubtract);
        getTfProcessList().add(tfx);
        System.out.println(" ]] added to set: " + tfx);
    }

    private void refreshTimeForm() {
        for (int i = 0; i < 5; i++) {
            if (weekTimeFrames[i] != null) {
                tfBinderList.get(i).setBean(weekTimeFrames[i]);
            } else {
                tfBinderList.get(i).setBean(new TimeFrame(
                        date4tfForm[i].toString(),"-","-", TimeFrame.TfStatus.Present, setup.getDoctor().getId()));
            }
            tfBinderList.get(i).bind(frameStart[i], "timeStart");
            tfBinderList.get(i).bind(frameEnd[i], "timeEnd");
        }
    }

    public void enterDoctorManagement(Doctor doctor) {
        setDocFromTab(doctor);
        setup.setDoctor(doctor);
        UI.getCurrent().navigate("doctor");
    }

    public TextField[] getFrameStart() {
        return frameStart;
    }

    public TextField[] getFrameEnd() {
        return frameEnd;
    }

    public List<Binder<TimeFrame>> getTfBinderList() {
        return tfBinderList;
    }

    public void setDocFromTab(Doctor doctor) {
        binder.setBean(doctor);
    }

    // (i) this is the way to add external control gauge in form of setup.timetableLock, but instead internal
    // functionality of grid should be utilized e.isEnabled()
    public void lockTimetables(boolean lock) {
        if (setup.isTimetableLock()) lockLabel.setText("timetable locked");
        else lockLabel.setText("timetable unlocked");
        timetable.forEach(e -> {
            e.setEnabled(!lock);
            e.deselectAll();
        });
    }
}