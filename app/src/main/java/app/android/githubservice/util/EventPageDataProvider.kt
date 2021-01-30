package app.android.githubservice.util

import app.android.githubservice.enums.EnumEventType
import app.android.githubservice.entity.event.Events

class EventPageDataProvider {

    /**
     *provide event type for "events-adapter" in "events-fragment"
     */

    companion object {
        fun getEvent(item: Events.EventsItem): String {
            return when (item.type) {
                EnumEventType.COMMIT_COMMENT_EVENT.valueStr.toString() -> EnumEventType.COMMIT_COMMENT_EVENT.valueStr.toString()
                EnumEventType.CREATE_EVENT.valueStr.toString() -> EnumEventType.CREATE_EVENT.valueStr.toString()
                EnumEventType.DELETE_EVENT.valueStr.toString() -> EnumEventType.DELETE_EVENT.valueStr.toString()
                EnumEventType.FORK_EVENT.valueStr.toString() -> EnumEventType.FORK_EVENT.valueStr.toString()
                EnumEventType.GOLLUM_EVENT.valueStr.toString() -> EnumEventType.GOLLUM_EVENT.valueStr.toString()
                EnumEventType.ISSUE_COMMENT_EVENT.valueStr.toString() -> EnumEventType.ISSUE_COMMENT_EVENT.valueStr.toString()
                EnumEventType.ISSUES_EVENT.valueStr.toString() -> EnumEventType.ISSUES_EVENT.valueStr.toString()
                EnumEventType.MEMBER_EVENT.valueStr.toString() -> EnumEventType.MEMBER_EVENT.valueStr.toString()
                EnumEventType.PUBLIC_EVENT.valueStr.toString() -> EnumEventType.PUBLIC_EVENT.valueStr.toString()
                EnumEventType.PULL_REQUEST_EVENT.valueStr.toString() -> EnumEventType.PULL_REQUEST_EVENT.valueStr.toString()
                EnumEventType.PULL_REQUEST_REVIEW_COMMENT_EVENT.valueStr.toString() -> EnumEventType.PULL_REQUEST_REVIEW_COMMENT_EVENT.valueStr.toString()
                EnumEventType.PUSH_EVENT.valueStr.toString() -> EnumEventType.PUSH_EVENT.valueStr.toString()
                EnumEventType.RELEASE_EVENT.valueStr.toString() -> EnumEventType.RELEASE_EVENT.valueStr.toString()
                EnumEventType.SPONSORSHIP_EVENT.valueStr.toString() -> EnumEventType.SPONSORSHIP_EVENT.valueStr.toString()
                EnumEventType.WATCH_EVENT.valueStr.toString() -> EnumEventType.WATCH_EVENT.valueStr.toString()
                else -> EnumEventType.UNDEFINE_EVENT.valueStr.toString()
            }
        }
    }

}