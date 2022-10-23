package com.example.appointfront.engine;

import org.springframework.stereotype.Service;

@Service
public class Setup {

    private String labelText;

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }
}
