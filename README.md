Runs on localhost://4000
Swagger docs at http://localhost:4000/jira-metrics-api-docs/index.html

{
  "722": {
    "boardId": 722,
    "issueRatio": 100,
    "storyPointRatio": 100,
    "storiesCompletedPerSprint": [
      7,
      13,
      12
    ],
    "storyPointsCompletedPerSprint": [
      34,
      57,
      76
    ],
    "sprintWorkBreakdownIssues": "Infinity",
    "sprintWorkBreakdownPoints": "Infinity",
    "bugIssueForecastAccuracy": 100,
    "bugStoryPointForecastAccuracy": 100
  },
  "1332": {
    "boardId": 1332,
    "issueRatio": 100,
    "storyPointRatio": 100,
    "storiesCompletedPerSprint": [
      7,
      13,
      12
    ],
    "storyPointsCompletedPerSprint": [
      34,
      57,
      76
    ],
    "sprintWorkBreakdownIssues": "Infinity",
    "sprintWorkBreakdownPoints": "Infinity",
    "bugIssueForecastAccuracy": 100,
    "bugStoryPointForecastAccuracy": 100
  }
}





ID = key
Epic = fields.epic.summary
Issue type = fields.issuetype.id
Sprint = should pass down sprint number
Size = customfield_10002
Labels = fields.labels array
Creation Date = fields.created 2017-08-02T14:39:58.000-0400 => MM/dd/YYYY
Status = status.id or status.statusCategory.id

"/Users/cedennis/Documents/SaveDashboard.xlsx"