#Java Reddit API Wrapper

###Contributing
Want to contribute? Fantasitc! Follow these steps:

1. Fork the repository
2. Create a file called `credentials.txt` in `/src/test/java/resources/`. The first line should be your testing reddit account's username, and the second should be its password.
3. Add your code. Implement an API endpoint, make the code prettier, or even just fix up some whitespace or documentation.
4. Add TestNG tests that implement your code
5. Test your code by executing `./gradlew test`
6. Send a pull request!

###Methods that still have to be implemented
####account
```
/api/clear_sessions
/api/delete_user
/api/register
/api/update
/api/update_email
/api/update_password
/api/v1/me
/api/v1/me/karma
/api/v1/me/prefs
/api/v1/me/trophies
/prefs/blocked
/prefs/friends
/prefs/where
```

####apps
```
/api/adddeveloper
/api/deleteapp
/api/removedeveloper
/api/revokeapp
/api/setappicon
/api/updateapp
```

####captcha
```
/api/needs_captcha.json
/api/new_captcha
/captcha/iden
```

####flair
```
/api/clearflairtemplates
/api/deleteflair
/api/deleteflairtemplate
/api/flair
/api/flairconfig
/api/flaircsv
/api/flairlist
/api/flairselector
/api/flairtemplate
/api/selectflair
/api/setflairenabled
```

####links & comments
```
/api/comment
/api/del
/api/editusertext
/api/hide
/api/info
/api/marknsfw
/api/morechildren
/api/report
/api/save
/api/saved_categories.json
/api/sendreplies
/api/set_contest_mode
/api/set_subreddit_sticky
/api/store_visits
/api/submit
/api/unhide
/api/unmarknsfw
/api/unsave
/api/vote
```

####listings
```
/by_id/names
/comments/article
/controversial
/hot
/new
/random
/top
/sort
```

####private messages
```
/api/block
/api/compose
/api/read_message
/api/unread_message
/message/inbox
/message/sent
/message/unread
/message/where
```

####moderation
```
/about/log
/api/accept_moderator_invite
/api/approve
/api/distinguish
/api/ignore_reports
/api/leavecontributor
/api/leavemoderator
/api/remove
/api/unignore_reports
/stylesheet
```

####multis
```
/api/multi/mine
/api/multi/multipath
/api/multi/multipath/copy
/api/multi/multipath/description
/api/multi/multipath/r/srname
/api/multi/multipath/rename
```

####search
```
/search
```

####subreddits
```
/about/banned
/about/contributors
/about/moderators
/about/wikibanned
/about/wikicontributors
/about/where
/api/delete_sr_header
/api/delete_sr_img
/api/recommend/sr/srnames
/api/search_reddit_names.json
/api/site_admin
/api/submit_text.json
/api/subreddit_stylesheet
/api/subreddits_by_topic.json
/api/subscribe
/api/upload_sr_img
/r/subreddit/about.json
/r/subreddit/about/edit.json
/subreddits/mine/contributor
/subreddits/mine/moderator
/subreddits/mine/subscriber
/subreddits/mine/where
/subreddits/new
/subreddits/popular
/subreddits/search
/subreddits/where
```

####users
```
/api/friend
/api/setpermissions
/api/unfriend
/api/username_available.json
/api/v1/user/username/trophies
/user/username/about.json
/user/username/comments
/user/username/disliked
/user/username/gilded
/user/username/hidden
/user/username/liked
/user/username/overview
/user/username/saved
/user/username/submitted
/user/username/where
```

####wiki
```
/api/wiki/alloweditor/add
/api/wiki/alloweditor/del
/api/wiki/alloweditor/act
/api/wiki/edit
/api/wiki/hide
/api/wiki/revert
/wiki/discussions/page
/wiki/pages
/wiki/revisions
/wiki/revisions/page
/wiki/settings/page
/wiki/page
```
