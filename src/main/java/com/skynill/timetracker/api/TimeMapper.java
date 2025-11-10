package com.skynill.timetracker.api;

import com.skynill.timetracker.api.dto.TimeDto;
import com.skynill.timetracker.domain.timeentry.TimeEntry;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeMapper {

    TimeDto toDto(TimeEntry timeEntry);

    List<TimeDto> toDtoList(List<TimeEntry> timeEntries);

}
