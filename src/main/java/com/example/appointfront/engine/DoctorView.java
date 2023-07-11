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

import static com.example.appointfront.data.TimeFrame.TfStatus.Present;
import static com.example.appointfront.engine.Setup.WORKDAY_HOURS_AMOUNT;

@Route(value = "doctor", layout = MainLayout.class)
@PageTitle("Doctors | Tiny Clinic")
public class DoctorView extends HorizontalLayout {

    private final Setup setup;
    private final BaseForm form;
    private final BackendClient client;
    private LocalDate targetDate;
    private LocalDate[] date4tfForm;
    private TimeFrame[] weekTimeFrames = new TimeFrame[5];
    private final TextField[] frameStart = new TextField[5];
    private final TextField[] frameEnd = new TextField[5];
    private final List<Binder<TimeFrame>> tfBinderList = new ArrayList<>();
    private final List<Grid<TableEntry>> timetable = new ArrayList<>();
    private final Label lockLabel = new Label("timetable unlocked");
    private final String[] dayHeaders = new String[7];
    private final HorizontalLayout weekTables = new HorizontalLayout();
    private final TextField navPanelField = new TextField();

    public DoctorView(BackendClient client) {
        this.client = client;
        setup = Setup.SINGLETON_INSTANCE;
        VerticalLayout container = new VerticalLayout();
        Label formLabel = new Label();
        if (setup.getAdmission() > 1) form = new DoctorForm(client, DoctorView.this);
        else form = new AppointForm(client, DoctorView.this);
        if (setup.getDoctor() != null) {
            formLabel.setText("selected: " + setup.getDoctor().getName() + " " + setup.getDoctor().getLastName());
        } else {
            formLabel.setText("none selected");
            setup.setDoctor(client.getDoctorList().stream().findFirst().orElseThrow(NotFoundException::new));
        }
        addClassName("doctor-view");
        setSizeFull();
        createTables();
        VerticalLayout rightContainer = new VerticalLayout(buildNavPanel(), lockLabel, formLabel, (Component) form);
        rightContainer.setSizeFull();
        weekTables.setSizeFull();
        container.add(weekTables, createTimeForm());
        container.setWidth("72%");
        add(container, rightContainer);
    }

    void createTables() {
        for (int i = 0; i < 5; i ++) {
            int y = i;
            timetable.add(new Grid<>(TableEntry.class));
            timetable.get(i).setColumns("status");
            timetable.get(i).getColumnByKey("status").setSortable(false);
            timetable.get(i).setHeightFull();
            timetable.get(i).asSingleSelect().addValueChangeListener(event -> {
                for (int k = 0; k < 5; k ++) {
                    if (k != y) timetable.get(k).deselectAll();
                }
                TableEntry entry = event.getValue();
                form.clearForm();               // main purpose of creating interface BaseForm was to call
                if (entry == null) return;      // f. as clearForm() or activateControls() from within this class
                setup.setEntry(entry);
                setup.setTargetDay(entry.getEntryDateTime().toLocalDate()); // this looks stupid
                form.activateControls();
            });
            weekTables.add(timetable.get(i));
        }
        LocalDate[] dateArray = refreshHeaders();
        refreshTables(dateArray);
    }

    private LocalDate[] refreshHeaders() {
        LocalDate[] date = new LocalDate[7];
        for (int n = 1; n < 8; n ++) {
            LocalDate day = setup.getTargetDay();
            while (day.getDayOfWeek().equals(DayOfWeek.SATURDAY) || day.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                day = day.plusDays(1L);
            }
            date[n - 1] = day.minusDays(day.getDayOfWeek().getValue() - n);
            String dayOfWeek = DayOfWeek.of(n).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStamp = date[n - 1].format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            dayHeaders[n - 1] =  dayOfWeek + "; " + dateStamp;
        }
        date4tfForm = Arrays.copyOfRange(date, 0, 5);
        return date;
    }

    private void refreshTables(LocalDate[] date) {
        for (int i = 0; i < 5; i ++) {
            TableEntry[] entries = new TableEntry[WORKDAY_HOURS_AMOUNT];
            LocalTime tfStart = null, tfEnd = null;
            if (setup.getDoctor() != null) {
                for (TimeFrame tf : client.getDocsTimeFrames()) {
                    if (date[i].equals(LocalDate.parse(tf.getDate()))) {
                        tfStart = LocalTime.parse(tf.getTimeStart());
                        tfEnd   = LocalTime.parse(tf.getTimeEnd());
                        weekTimeFrames[i] = tf;
                    }
                }
            }
            for (int n = 0; n < WORKDAY_HOURS_AMOUNT; n ++) {
                LocalDateTime dateTime = LocalDateTime.of(date[i], LocalTime.of(n + Setup.EARLIEST_STARTING_HOUR, 0));
                entries[n] = createTableEntry(dateTime, tfStart, tfEnd);
            }
            timetable.get(i).setItems(entries);
            timetable.get(i).getColumnByKey("status").setHeader(dayHeaders[i]);
        }
    }

    void forceRefresh() {
        weekTimeFrames = new TimeFrame[5];
        refreshNavPanel();
        LocalDate[] dateArray = refreshHeaders();
        refreshTables(dateArray);
        refreshTimeForm();
    }

    TableEntry createTableEntry(LocalDateTime entryDateTime, LocalTime tfStart, LocalTime tfEnd) {
        LocalTime time = LocalTime.of(entryDateTime.getHour(), 0);
        String status;
        Patient entryPatient = null;
        Appointment foundAppointment = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd','HH:mm");
        if (tfStart == null) return new TableEntry(entryDateTime, "n/a", null, setup.getDoctor(), null);
        if (time.isBefore(tfStart) || time.isAfter(tfEnd) || time.equals(tfEnd)) status = "off";
        else status = time.getHour() + ":00 ---AVAILABLE---";
        for (Appointment singleApp : client.getCurrentDoctorApps()) {
            for (Patient pat : setup.getPatients()) { // mapping Patient from patientId
                if (pat.getId().equals(singleApp.getPatientId())) entryPatient = pat;
            }
            LocalDateTime appDateTime = LocalDateTime.parse(singleApp.getStartDateTime(), formatter);
            if (entryDateTime.equals(appDateTime)) { // finding Appointment and is it belonging to current patient
                foundAppointment = singleApp;
                if (entryPatient.equals(setup.getPatient())) status = time.getHour() + ":00 Appointed";
                else status = "busy";
            }
        }
        return new TableEntry(entryDateTime, status, entryPatient, setup.getDoctor(), foundAppointment);
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
            int finalVar = i;
            frameStart[i].addValueChangeListener(action -> tfProcessing(finalVar));
            frameEnd[i].addValueChangeListener(action -> tfProcessing(finalVar));
            bottomBar.add(new VerticalLayout(frameStart[i], frameEnd[i]));
        }
        refreshTimeForm();
        return new FormLayout(bottomBar);
    }

    private void tfProcessing(int x) {
        if (!frameStart[x].isEnabled()) return;
        TimeFrame tfx = tfBinderList.get(x).getBean();
        TimeFrame tfSubtract = null;
        for (TimeFrame tf : DoctorForm.getTfProcessList()) {
            if (tf.getDate().equals(tfx.getDate())) tfSubtract = tf;
        }
        DoctorForm.getTfProcessList().remove(tfSubtract);
        DoctorForm.getTfProcessList().add(tfx);
    }

    private void refreshTimeForm() {
        for (int i = 0; i < 5; i++) {
            TimeFrame tfDummy = new TimeFrame(date4tfForm[i].toString(), "-", "-", Present, setup.getDoctor().getId());
            if (weekTimeFrames[i] != null) tfBinderList.get(i).setBean(weekTimeFrames[i]);
            else tfBinderList.get(i).setBean(tfDummy);
            tfBinderList.get(i).bind(frameStart[i], "timeStart");
            tfBinderList.get(i).bind(frameEnd[i], "timeEnd");
        }
    }

    public void enterDoctorManagement(Doctor doctor) {
        setup.setDoctor(doctor);
        UI.getCurrent().navigate("doctor");
    }

    public TextField[] getFrameStart() {
        return frameStart;
    }

    public TextField[] getFrameEnd() {
        return frameEnd;
    }

    // (i) this is the way to add external control gauge in form of setup.timetableLock, but instead internal
    // functionality of grid should be utilized e.isEnabled()
    public void lockTimetables(boolean lock) {
        if (lock) lockLabel.setText("timetable locked");
        else lockLabel.setText("timetable unlocked");
        timetable.forEach(e -> {
            e.setEnabled(!lock);
            e.deselectAll();
        });
    }
}