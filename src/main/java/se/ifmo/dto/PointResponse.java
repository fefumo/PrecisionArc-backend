package se.ifmo.dto;

public class PointResponse {
    private Long id;
    private Double xCoord;
    private Double yCoord;
    private Boolean result;
    private String currentTime;
    private String elapsedTime;

    public PointResponse(Long id, Double xCoord, Double yCoord, Boolean result, String currentTime,
            String elapsedTime) {
        this.id = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.result = result;
        this.currentTime = currentTime;
        this.elapsedTime = elapsedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getxCoord() {
        return xCoord;
    }

    public void setxCoord(Double xCoord) {
        this.xCoord = xCoord;
    }

    public Double getyCoord() {
        return yCoord;
    }

    public void setyCoord(Double yCoord) {
        this.yCoord = yCoord;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
}
