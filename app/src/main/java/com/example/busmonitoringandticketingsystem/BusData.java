package com.example.busmonitoringandticketingsystem;

public class BusData {
    private String dateEdited;
    private String timeEdited;
    private String route;
    private String driver;
    private String driverNumber;
    private String schedule_600_am;
    private String schedule_1230_pm;
    private String schedule_330_pm;
    private String schedule_600_pm;
    private String schedule_900_pm;
    private String onUCLM;
    private String dateID;
    private String availability;

    public BusData(){
        /*
        dateEdited = "";
        timeEdited = "";
        route = "";
        driver = "";
        driverNumber = "";
        schedule_600_am = true;
        schedule_1230_pm = true;
        schedule_330_pm = true;
        schedule_600_pm = true;
        schedule_900_pm = true;
        onUCLM = true;
        availability = true;

         */
    }

    public BusData(String availability, String dateEdited,String driver, String driverNumber, String onUCLM , String route,
                   String schedule_1230_pm,
                   String schedule_330_pm, String schedule_600_am,String schedule_600_pm,String schedule_900_pm,
                   String timeEdited, String dateID)
            {

        this.dateEdited = dateEdited;
        this.timeEdited = timeEdited;
        this.route = route;
        this.dateID = dateID;
        this.driver = driver;
        this.driverNumber = driverNumber;
        this.schedule_600_am = schedule_600_am;
        this.schedule_1230_pm = schedule_1230_pm;
        this.schedule_330_pm = schedule_330_pm;
        this.schedule_600_pm = schedule_600_pm;
        this.schedule_900_pm = schedule_900_pm;
        this.onUCLM = onUCLM;
        this.availability = availability;

    }



    public String getDateEditedView() {
        return ""+dateEdited;
    }



    public String getTimeEditedView() {
        return ""+timeEdited;
    }

    public String getDriverNumberView() {
        return ""+driverNumber;
    }

    public String getRouteView() {
        return ""+route;
    }

    public String getDriverView() {
        return ""+driver;
    }

    public String getSchedule_600_amView() {
        return ""+schedule_600_am;
    }
    public String getSchedule_1230_pmView() {
        return ""+schedule_1230_pm;
    }
    public String getSchedule_330_pmView() {
        return ""+schedule_330_pm;
    }
    public String getSchedule_600_pmView() {
        return ""+schedule_600_pm;
    }
    public String getSchedule_900_pmView() {
        return ""+schedule_900_pm;
    }

    public String getOnUCLMView () {
        return ""+onUCLM;
    }


    public String getAvailabilityView() {
        return ""+availability;
    }

    public String getDateID(){
        return ""+dateID;
    }


    public void setDateEdited(String dateEdited) {
        this.dateEdited = dateEdited;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public void setOnUCLM(String onUCLM) {
        this.onUCLM = onUCLM;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setSchedule_330_pm(String schedule_330_pm) {
        this.schedule_330_pm = schedule_330_pm;
    }

    public void setSchedule_600_am(String schedule_600_am) {
        this.schedule_600_am = schedule_600_am;
    }

    public void setSchedule_600_pm(String schedule_600_pm) {
        this.schedule_600_pm = schedule_600_pm;
    }

    public void setSchedule_1230_pm(String schedule_1230_pm) {
        this.schedule_1230_pm = schedule_1230_pm;
    }

    public void setTimeEdited(String timeEdited) {
        this.timeEdited = timeEdited;
    }

    public void setSchedule_900_pm(String schedule_900_pm) {
        this.schedule_900_pm = schedule_900_pm;
    }
    public void setDateID(String dateID){
        this.dateID = dateID;
    }
}

