package gov.uscis.vis.api.service;

import gov.uscis.vis.api.models.IssueList;
import gov.uscis.vis.api.models.SprintList;
import gov.uscis.vis.api.models.StateEnum;

/**
 * Created by cedennis on 2/9/17.
 */
public interface StoryService {
    public SprintList getSprintList(Integer boardId);
    public SprintList getSprintListWithState(Integer boardId, StateEnum state);
    public IssueList getIssueListForSprint(Long latestCompletedSprintId);
}
