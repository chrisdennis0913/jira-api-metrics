package gov.uscis.vis.api.models.best;

import gov.uscis.vis.api.utils.NumberUtils;

/**
 * Created by cedennis on 11/26/17.
 */
public class SprintMetricsDto {

    int boardId;
    long sprintId;

    double issueMetric;
    double storyPointMetric;

    public SprintMetricsDto(int boardId, long sprintId, Double issueAccuracy, Double storyPointAccuracy) {
        this.boardId = boardId;
        this.sprintId = sprintId;
        this.issueMetric = issueAccuracy.equals(Double.NaN) ? 100.0 : issueAccuracy;
        this.storyPointMetric = storyPointAccuracy.equals(Double.NaN) ? 100.0 : storyPointAccuracy;
    }

    public SprintMetricsDto(int boardId, long sprintId, int issueAccuracy, int storyPointAccuracy) {
        this.boardId = boardId;
        this.sprintId = sprintId;
        this.issueMetric = issueAccuracy;
        this.storyPointMetric = storyPointAccuracy;
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

    public double getIssueMetric() {
        return issueMetric;
    }

    public void setIssueMetric(double issueMetric) {
        this.issueMetric = issueMetric;
    }

    public double getStoryPointMetric() {
        return storyPointMetric;
    }

    public void setStoryPointMetric(double storyPointMetric) {
        this.storyPointMetric = storyPointMetric;
    }

    @Override
    public String toString() {
        return "{" +
                "boardId=" + boardId +
                ", sprintId=" + sprintId +
                ", issueRatio=" + NumberUtils.decimalFormat.format(issueMetric) +
                ", storyPointRatio=" + NumberUtils.decimalFormat.format(storyPointMetric) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SprintMetricsDto that = (SprintMetricsDto) o;

        if (boardId != that.boardId) return false;
        if (sprintId != that.sprintId) return false;
        if (Double.compare(that.issueMetric, issueMetric) != 0) return false;
        return Double.compare(that.storyPointMetric, storyPointMetric) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = boardId;
        result = 31 * result + (int) (sprintId ^ (sprintId >>> 32));
        temp = Double.doubleToLongBits(issueMetric);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(storyPointMetric);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}