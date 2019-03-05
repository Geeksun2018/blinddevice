package cn.finlab.blinddevice.model;

import java.util.List;

public class WalkRoute {

    private int status;
    private String message;
    private Result result;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public Result getResult() {
        return result;
    }

}

class Origin {
    private double lng;
    private double lat;
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLat() {
        return lat;
    }
}

class Destination {

    private double lng;
    private double lat;
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLat() {
        return lat;
    }

}

class Start_location {

    private String lng;
    private String lat;
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLat() {
        return lat;
    }

}

class End_location {

    private String lng;
    private String lat;
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getLng() {
        return lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLat() {
        return lat;
    }

}

class Steps {

    private int distance;
    private int duration;
    private int direction;
    private String instruction;
    private String path;
    private Start_location start_location;
    private End_location end_location;
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public int getDistance() {
        return distance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getDuration() {
        return duration;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    public int getDirection() {
        return direction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
    public String getInstruction() {
        return instruction;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setStart_location(Start_location start_location) {
        this.start_location = start_location;
    }
    public Start_location getStart_location() {
        return start_location;
    }

    public void setEnd_location(End_location end_location) {
        this.end_location = end_location;
    }
    public End_location getEnd_location() {
        return end_location;
    }

}

class Routes {

    private int distance;
    private int duration;
    private List<Steps> steps;
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public int getDistance() {
        return distance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getDuration() {
        return duration;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }
    public List<Steps> getSteps() {
        return steps;
    }

}

class Result {

    private Origin origin;
    private Destination destination;
    private List<Routes> routes;
    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
    public Origin getOrigin() {
        return origin;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
    public Destination getDestination() {
        return destination;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }
    public List<Routes> getRoutes() {
        return routes;
    }

}