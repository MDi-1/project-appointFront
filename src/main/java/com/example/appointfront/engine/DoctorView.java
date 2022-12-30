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
    private List<Grid<TableEntry>> timetable = new ArrayList<>();
    private Label lockLabel = new Label("timetable unlocked");
    private String[] dayHeaders = new String[7];
    private LocalDate[] date4tfForm;
    private HorizontalLayout weekTables = new HorizontalLayout();
    private TextField navPanelField = new TextField();

    public DoctorView(BackendClient client, Setup setup) {
        this.client = client;
        this.setup = setup;
        VerticalLayout container = new VerticalLayout();
        Label formLabel = new Label();
        String formHeaderTxt;
        if (setup.isAdmission()) form = new DoctorForm(client, setup, DoctorView.this);
        else form = new AppointForm(client, setup);
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

        // additional test button later 2B removed; fixme
        Button forcedBtn = new Button("forced ref");
        forcedBtn.addClickListener(event -> forceRefresh());

        VerticalLayout rightContainer = new VerticalLayout(
                buildNavPanel(), lockLabel, forcedBtn, formLabel, (Component) form);
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
            long appId = 0;
            if (tfStart == null) status = "n/a";
            else {
                if (time.isBefore(tfStart) || time.isAfter(tfEnd) || time.equals(tfEnd)) status = "off";
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
        // w tej funkcji trzeba odświeżyć tylko placeholder dla TextField i Setup.targetDay (może nawet z tym ostatnim
        // nie trzeba nic robić, bo wystarczy lambda pozostawiona w tej funkcji.
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
        setup.setTargetDay(targetDate);
        String placeholder = setup.getTargetDay().format(DateTimeFormatter.ofPattern("dd-M-yyyy"));
        navPanelField.setPlaceholder(placeholder);
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
            tfBinderList.get(i).bind(frameStart[i], "timeStart");
            tfBinderList.get(i).bind(frameEnd[i], "timeEnd");
            frameStart[i].setEnabled(false);
            frameEnd[i].setEnabled(false);
            bottomBar.add(new VerticalLayout(frameStart[i], frameEnd[i]));
        }
        refreshTimeForm();
        return new FormLayout(bottomBar);
    }

    private void refreshTimeForm() {
        for (int i = 0; i < 5; i++) {
            if (weekTimeFrames[i] != null) {
                tfBinderList.get(i).setBean(weekTimeFrames[i]);
            } else {
                tfBinderList.get(i).setBean(new TimeFrame(date4tfForm[i].toString(),"-","-",setup.getDoctor().getId()));
            }
        }
    }

    //as soon as this project's finished refactor this f. to more civilized form to refresh view.
    public void refreshPage() {
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