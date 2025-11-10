package com.skynill.timetracker.api;

import com.skynill.timetracker.api.dto.TimeDto;
import com.skynill.timetracker.domain.timeentry.TimeService;
import com.skynill.timetracker.domain.timeentry.buffer.TimeQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping(TimeController.BASE_PATH)
public class TimeController {

    public static final String BASE_PATH = "/api/v1/time";

    public static final String TIME_ENTRIES = "/entries";
    public static final String BUFFER_CLEAN = "/buffer/clean";

    private final TimeService timeService;
    private final TimeQueue timeBuffer;
    private final TimeMapper mapper;


    public TimeController(TimeMapper mapper, TimeService timeService, TimeQueue timeBuffer) {
        this.mapper = mapper;
        this.timeService = timeService;
        this.timeBuffer = timeBuffer;
    }


    @GetMapping(TimeController.TIME_ENTRIES)
    public List<TimeDto> getAllEntries() {
        return mapper.toDtoList(timeService.getAllTimeEntries());
    }

    @PostMapping(TimeController.BUFFER_CLEAN)
    public void bufferClean() {
        timeBuffer.bufferClean();
    }

}
