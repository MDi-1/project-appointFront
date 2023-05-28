package com.example.appointfront.engine;

public interface BaseForm {

    Setup setup = Setup.SINGLETON_INSTANCE;

    void activateControls();
    void executeItem();
    void clearForm();   
    // void getId(); // some return value to be invented here instead of "void"
}
// > maybe, some day, when there is enough time - refactor executeItem() that this f. accepts form specific class from
// data package. Use generics.
