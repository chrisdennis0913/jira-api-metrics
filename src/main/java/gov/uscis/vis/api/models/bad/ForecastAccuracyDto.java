package gov.uscis.vis.api.models.bad;

import gov.uscis.vis.api.utils.NumberUtils;

/**
 * Created by cedennis on 11/26/17.
 */
public class ForecastAccuracyDto {

    int boardId;
    long sprintId;

    double issueForecastAccuracy;
    double storyPointForecastAccuracy;

    public ForecastAccuracyDto(int boardId, long sprintId, Double issueAccuracy, Double storyPointAccuracy) {
        this.boardId = boardId;
        this.sprintId = sprintId;
        this.issueForecastAccuracy = issueAccuracy.equals(Double.NaN) ? 100.0 : issueAccuracy;
        this.storyPointForecastAccuracy = storyPointAccuracy.equals(Double.NaN) ? 100.0 : storyPointAccuracy;
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

    public double getIssueForecastAccuracy() {
        return issueForecastAccuracy;
    }

    public void setIssueForecastAccuracy(double issueForecastAccuracy) {
        this.issueForecastAccuracy = issueForecastAccuracy;
    }

    public double getStoryPointForecastAccuracy() {
        return storyPointForecastAccuracy;
    }

    public void setStoryPointForecastAccuracy(double storyPointForecastAccuracy) {
        this.storyPointForecastAccuracy = storyPointForecastAccuracy;
    }

    @Override
    public String toString() {
        return "{" +
                "boardId=" + boardId +
                ", sprintId=" + sprintId +
                ", issueRatio=" + NumberUtils.decimalFormat.format(issueForecastAccuracy) + "%" +
                ", storyPointRatio=" + NumberUtils.decimalFormat.format(storyPointForecastAccuracy) + "%" +
                '}';
    }
}