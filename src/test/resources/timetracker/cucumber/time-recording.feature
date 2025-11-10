Feature: Time Tracker Service

  @Integration
  Scenario: Successful time recording and retrieval via API
    Given the time tracker is initialized
    And async db writer is running
    When time generator is running during 5 sec
    Then time generator generated 5 time entries
    And 5 records were pushed to buffer
    And db contains 5 records in asc order
    When user requests all time entries
    And response code is 200
    Then response contains 5 records in asc order

  @Integration
  Scenario: Async buffer consumer doesn't work
    Given the time tracker is initialized
    When time generator is running during 5 sec
    Then time generator generated 5 time entries
    And 5 records were pushed to buffer
    And buffer contains 5 records
    And db is empty
    When user requests all time entries
    Then response code is 200
    And response contains 0 records in asc order


  @DownDatabase
  Scenario: Temporary lost connection with db
    Given the time tracker is initialized
    When time generator is running
    And async db writer is running
    When database is down
    Then queue in the buffer is growing up in 5 sec
    And database again is in normal mode
    And time generator is stopped
    And waiting for the buffer to empty
    Then db contains all records generated records in asc order
    When user requests all time entries
    And response code is 200
    Then response contains all records generated records in asc order

  @DownDatabase
  Scenario: Temporary lost connection with db and buffer overloaded
    Given the time tracker is initialized
    When time generator is running
    And async db writer is running
    When database is down
    Then queue in the buffer is growing up in 5 sec
    And waiting buffer overload
    And 2 seconds passed
    Then new records will be lost
    When database again is in normal mode
    And 3 seconds passed
    Then new records are stored

  @SlowDatabase
  Scenario: Database is slowed
    Given the time tracker is initialized
    When time generator is running
    And async db writer is running
    When database is artificially slowed by 2000 ms
    Then queue in the buffer is growing up in 5 sec
    And database again is in normal mode
