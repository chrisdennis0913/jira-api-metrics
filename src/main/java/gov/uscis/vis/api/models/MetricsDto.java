package gov.uscis.vis.api.models;

import gov.uscis.vis.api.utils.NumberUtils;

import java.util.Arrays;

/**
 * Created by cedennis on 2/3/17.
 */
public class MetricsDto {

    int boardId;

    // 1) Story Point Forecast Accuracy
    double issueForecastAccuracy;
    double storyPointForecastAccuracy;

    // 2) Story Point Completion Rate
    int[] storiesCompletedPerSprint;
    double[] storyPointsCompletedPerSprint;

    // 5) Sprint work breakdown: (New User Stories + product enhancement user stories) / (technical debt user stories + production incidents + other user stories)
    // story totals + tasks + spikes / bugs + preview defects + production defects
    double sprintWorkBreakdownIssues;
    double sprintWorkBreakdownPoints;

    // 6) Defect Fix Rate: Defects fixed / Defects in backlog
    Double bugIssueForecastAccuracy;
    Double bugStoryPointForecastAccuracy;

    public MetricsDto(int boardId) {
        this.boardId = boardId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
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

    public int[] getStoriesCompletedPerSprint() {
        return storiesCompletedPerSprint;
    }

    public void setStoriesCompletedPerSprint(int[] storiesCompletedPerSprint) {
        this.storiesCompletedPerSprint = storiesCompletedPerSprint;
    }

    public double[] getStoryPointsCompletedPerSprint() {
        return storyPointsCompletedPerSprint;
    }

    public void setStoryPointsCompletedPerSprint(double[] storyPointsCompletedPerSprint) {
        this.storyPointsCompletedPerSprint = storyPointsCompletedPerSprint;
    }

    public double getSprintWorkBreakdownIssues() {
        return sprintWorkBreakdownIssues;
    }

    public void setSprintWorkBreakdownIssues(double sprintWorkBreakdownIssues) {
        this.sprintWorkBreakdownIssues = sprintWorkBreakdownIssues;
    }

    public double getSprintWorkBreakdownPoints() {
        return sprintWorkBreakdownPoints;
    }

    public void setSprintWorkBreakdownPoints(double sprintWorkBreakdownPoints) {
        this.sprintWorkBreakdownPoints = sprintWorkBreakdownPoints;
    }

    public Double getBugIssueForecastAccuracy() {
        return bugIssueForecastAccuracy;
    }

    public void setBugIssueForecastAccuracy(Double bugIssueForecastAccuracy) {
        this.bugIssueForecastAccuracy = bugIssueForecastAccuracy.equals(Double.NaN) ? 100.0 : bugIssueForecastAccuracy;
    }

    public Double getBugStoryPointForecastAccuracy() {
        return bugStoryPointForecastAccuracy;
    }

    public void setBugStoryPointForecastAccuracy(Double bugStoryPointForecastAccuracy) {
        this.bugStoryPointForecastAccuracy = bugStoryPointForecastAccuracy.equals(Double.NaN) ? 100.0 : bugStoryPointForecastAccuracy;
    }

    @Override
    public String toString() {
        return "{" +
                "boardId=" + boardId +
                ", issueForecastAccuracy=" + NumberUtils.decimalFormat.format(issueForecastAccuracy) + "%" +
                ", storyPointForecastAccuracy=" + NumberUtils.decimalFormat.format(storyPointForecastAccuracy) + "%" +
                ", storiesCompletedPerSprint=" + Arrays.toString(storiesCompletedPerSprint) +
                ", storyPointsCompletedPerSprint=" + Arrays.toString(storyPointsCompletedPerSprint) +
                ", sprintWorkBreakdownIssues=" + NumberUtils.decimalFormat.format(sprintWorkBreakdownIssues) +
                ", sprintWorkBreakdownPoints=" + NumberUtils.decimalFormat.format(sprintWorkBreakdownPoints) +
                ", bugIssueForecastAccuracy=" + NumberUtils.decimalFormat.format(bugIssueForecastAccuracy) + "%" +
                ", bugStoryPointForecastAccuracy=" + NumberUtils.decimalFormat.format(bugStoryPointForecastAccuracy) + "%" +
                '}';
    }
}