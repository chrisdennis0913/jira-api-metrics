package gov.uscis.vis.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by cedennis on 1/30/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SprintList {
    private int maxResults;
    private int startAt;
    private Integer total;
    private boolean isLast;
    private List<Sprint> values;

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public List<Sprint> getValues() {
        return values;
    }

    public void setValues(List<Sprint> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SprintList{" +
                "maxResults=" + maxResults +
                ", startAt=" + startAt +
                ", total=" + total +
                ", isLast=" + isLast +
                ", values=" + values +
                '}';
    }
}
