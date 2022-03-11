package br.com.api.meetingroom.domain.entity;

import javax.persistence.Column;

public class Employee {

    @Column(name = "employee_name")
    private String name;

    @Column(name = "employee_email")
    private String email;

    public Employee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Employee() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static EmployeeBuilder newEmployeeBuilder() {
        return new EmployeeBuilder();
    }

    public static final class EmployeeBuilder {
        private String name;
        private String email;

        private EmployeeBuilder() {
        }


        public EmployeeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EmployeeBuilder email(String email) {
            this.email = email;
            return this;
        }

        public Employee build() {
            return new Employee(name, email);
        }
    }
}
