package gov.uscis.vis.api.models.bad;

import java.util.Arrays;

/**
 * Created by cedennis on 11/26/17.
 */
public class IssuesCompletedDto {

    int boardId;
    long[] sprintId;

    int[] issuesCompletedPerSprint;
    double[] storyPointsCompletedPerSprint;

    public IssuesCompletedDto(int boardId, long[] sprintId, int[] issuesCompleted, double[] storyPointsCompleted) {
        this.boardId = boardId;
        this.sprintId = sprintId;
        this.issuesCompletedPerSprint = issuesCompleted;
        this.storyPointsCompletedPerSprint = storyPointsCompleted;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public long[] getSprintId() {
        return sprintId;
    }

    public void setSprintId(long[] sprintId) {
        this.sprintId = sprintId;
    }

    public int[] getIssuesCompletedPerSprint() {
        return issuesCompletedPerSprint;
    }

    public void setIssuesCompletedPerSprint(int[] issuesCompletedPerSprint) {
        this.issuesCompletedPerSprint = issuesCompletedPerSprint;
    }

    public double[] getStoryPointsCompletedPerSprint() {
        return storyPointsCompletedPerSprint;
    }

    public void setStoryPointsCompletedPerSprint(double[] storyPointsCompletedPerSprint) {
        this.storyPointsCompletedPerSprint = storyPointsCompletedPerSprint;
    }

    @Override
    public String toString() {
        return "{" +
                "boardId=" + boardId +
            ", sprintId=" + Arrays.toString(sprintId) +
            ", storiesCompletedPerSprint=" + Arrays.toString(issuesCompletedPerSprint) +
            ", storyPointsCompletedPerSprint=" + Arrays.toString(storyPointsCompletedPerSprint) +
            '}';
    }
}