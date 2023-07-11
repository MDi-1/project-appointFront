package com.example.appointfront.engine;

public interface BaseForm {

    Setup setup = Setup.SINGLETON_INSTANCE;

    void activateControls();
    void executeItem();
    void clearForm();
}