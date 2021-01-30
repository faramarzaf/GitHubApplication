package app.android.githubservice.enums

enum class EnumEventType constructor(var valueStr: String?, val value: Int) {

    UNDEFINE_EVENT("UNDEFINE", -1),
    COMMIT_COMMENT_EVENT("CommitCommentEvent", 0),
    CREATE_EVENT("CreateEvent", 1),
    DELETE_EVENT("DeleteEvent", 2),
    FORK_EVENT("ForkEvent", 3),
    GOLLUM_EVENT("GollumEvent", 4),
    ISSUE_COMMENT_EVENT("IssueCommentEvent", 5),
    ISSUES_EVENT("IssuesEvent", 6),
    MEMBER_EVENT("MemberEvent", 7),
    PUBLIC_EVENT("PublicEvent", 8),
    PULL_REQUEST_EVENT("PullRequestEvent", 9),
    PULL_REQUEST_REVIEW_COMMENT_EVENT("PullRequestReviewCommentEvent", 10),
    PUSH_EVENT("PushEvent", 11),
    RELEASE_EVENT("ReleaseEvent", 12),
    SPONSORSHIP_EVENT("SponsorshipEvent", 13),
    WATCH_EVENT("WatchEvent", 14)
    ;

    companion object {


        operator fun get(value: String?): EnumEventType {
            if (value == null) {
                return UNDEFINE_EVENT
            }

            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.valueStr!!.equals(value.trim { it <= ' ' }, ignoreCase = true)) {
                    return `val`
                }
            }

            return UNDEFINE_EVENT
        }

        operator fun get(value: Int): EnumEventType {

            if (value == null) {
                return UNDEFINE_EVENT
            }

            val `arr$` = values()
            for (`val` in `arr$`) {
                if (`val`.value === value) {
                    return `val`
                }
            }

            return UNDEFINE_EVENT
        }
    }
}