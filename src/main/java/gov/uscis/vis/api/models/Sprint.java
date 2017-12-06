package gov.uscis.vis.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by cedennis on 1/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprint implements Comparable<Sprint>{
    private Long id;
    private String self;
    private StateEnum stateEnum;
    private String name;
    private Date startDate;
    private Date endDate;
    private Date completeDate;
    private Long originBoardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public StateEnum getStateEnum() {
        return stateEnum;
    }

    public void setStateEnum(StateEnum stateEnum) {
        this.stateEnum = stateEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public Long getOriginBoardId() {
        return originBoardId;
    }

    public void setOriginBoardId(Long originBoardId) {
        this.originBoardId = originBoardId;
    }

    @Override
    public int compareTo(Sprint sprint) {
        return this.startDate.compareTo(sprint.startDate);
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "id=" + id +
                ", self='" + self + '\'' +
                ", stateEnum=" + stateEnum +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", completeDate=" + completeDate +
                ", originBoardId=" + originBoardId +
                '}';
    }
}
