package com.example.appointfront.engine;

import com.example.appointfront.data.TableEntry;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class Setup {

    private TableEntry entrySelected;
}
