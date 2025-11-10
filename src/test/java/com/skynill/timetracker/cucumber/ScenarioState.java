package com.skynill.timetracker.cucumber;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public enum ScenarioState {

    STATE;

    private final ThreadLocal<Map<String, Object>> testContext = ThreadLocal.withInitial(HashMap::new);

    public <T> T get(String name) {
        return (T) testContext.get().get(name);
    }

    public <T> void set(String name, T object) {
        testContext.get().put(name, object);
    }

    public <T> void setResponse(ResponseEntity<T> response) {
        set(Name.RESPONSE, response);
    }

    public <T> ResponseEntity<T> getResponse() {
        return get(Name.RESPONSE);
    }

    public <T> T getResponseBody(Class<T> classType) {
        return classType.cast(getResponse().getBody());
    }

    public void reset() {
        testContext.get().clear();
    }

    public static class Name {

        public static final String RESPONSE = "RESPONSE";
    }

}
