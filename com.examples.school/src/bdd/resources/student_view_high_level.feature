Feature: Student View High Level
  Specifications of the behavior of the Student View

  Background: 
    Given The database contains a few students
    And The Student View is shown

  Scenario: Add a new student
    Given The user provides student data in the text fields
    When The user clicks the "Add" button
    Then The list contains the new student
    
  Scenario: Add a new student with an existing id
    Given The user provides student data in the text fields, specifying an existing id
    When The user clicks the "Add" button
    Then An error is shown containing the name of the existing student