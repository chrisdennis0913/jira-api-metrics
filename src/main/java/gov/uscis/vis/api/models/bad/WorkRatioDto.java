package gov.uscis.vis.api.models.bad;

import gov.uscis.vis.api.utils.NumberUtils;

/**
 * Created by cedennis on 11/26/17.
 */
public class WorkRatioDto {

    int boardId;
    long sprintId;

    double issueRatio;
    double storyPointRatio;

    public WorkRatioDto(int boardId, long sprintId, double issueAccuracy, double storyPointAccuracy) {
        this.boardId = boardId;
        this.sprintId = sprintId;
        this.issueRatio = issueAccuracy;
        this.storyPointRatio = storyPointAccuracy;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public long getSprintId() {
        return sprintId;
    }

    public void setSprintId(long sprintId) {
        this.sprintId = sprintId;
    }

    public double getIssueRatio() {
        return issueRatio;
    }

    public void setIssueRatio(double issueRatio) {
        this.issueRatio = issueRatio;
    }

    public double getStoryPointRatio() {
        return storyPointRatio;
    }

    public void setStoryPointRatio(double storyPointRatio) {
        this.storyPointRatio = storyPointRatio;
    }

    @Override
    public String toString() {
        return "{" +
                "boardId=" + boardId +
                ", sprintId=" + sprintId +
                ", issueRatio=" + NumberUtils.decimalFormat.format(issueRatio) + "%" +
                ", storyPointRatio=" + NumberUtils.decimalFormat.format(storyPointRatio) + "%" +
                '}';
    }
}