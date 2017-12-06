package gov.uscis.vis.api;

/**
 * Created by cedennis on 2/2/17.
 */
public enum IssueTypeEnum {
    //issuetype: 1=Bug, 3=Task, 7=Story, 11200=Spike, 12102=Preview Defect, 12103=Production Defect
    BUG(1L), TASK(3L), STORY(7L), SPIKE(11200L), PREVIEW_DEFECT(12102L), PRODUCTION_DEFECT(12103L);

    private final long id;

    IssueTypeEnum(long id) {
        this.id = id;
    }

    public long getId(){return id;}

}
