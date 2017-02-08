package net.dean.jraw.managers;

import net.dean.jraw.ApiException;
import net.dean.jraw.*;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.AuthenticationMethod;
import net.dean.jraw.http.MediaTypes;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.RequestBody;
import net.dean.jraw.http.RestResponse;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.DistinguishedStatus;
import net.dean.jraw.models.FlairTemplate;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Thing;
import net.dean.jraw.models.UserRecord;
import net.dean.jraw.models.attr.Votable;
import net.dean.jraw.util.JrawUtils;

import java.util.Map;

/**
 * This class manages actions most commonly reserved for moderators (although some of them can be used on yourself
 * without moderator permissions).
 */
public class ModerationManager extends AbstractManager {
    /**
     * Instantiates a new AbstractManager
     *
     * @param reddit The RedditClient to use
     */
    public ModerationManager(RedditClient reddit) {
        super(reddit);
    }
    
    @EndpointImplementation(Endpoints.SUBREDDIT_ABOUT_EDIT)
    public String getSubredditConfig(String subreddit) {
        HttpRequest request = reddit.request()
                .path(".json")
                .build();
        System.out.println(request.getUrl());
        return reddit.execute(request).getRaw();
    }
    
    @EndpointImplementation(Endpoints.SITE_ADMIN)
    public void editSidebar(
            String fullname,
            boolean allow_images,
            boolean allow_top,
            boolean collapse_deleted_comments,
            int comment_score_hide_mins,
            String description,
            boolean exclude_banned_modqueue,
            boolean hide_ads,
            String lang,
            String link_type,
            String name,
            boolean over_18,
            String public_description,
            boolean public_traffic,
            boolean show_media,
            boolean show_media_preview,
            String spam_comments,
            String spam_links,
            String spam_selfposts,
            boolean spoilers_enabled,
            String submit_link_label,
            String submit_text,
            String submit_text_label,
            String suggested_comment_sort,
            String title,
            String type,
            int wiki_edit_age,
            int wiki_edit_karma,
            String wikimode) throws NetworkException, ApiException {

        Map<String, String> args = JrawUtils.mapOf(
                "allow_images", allow_images,
                "allow_top", allow_top,
                "collapse_deleted_comments", collapse_deleted_comments,
                "comment_score_hide_mins", comment_score_hide_mins,
                "description", description,
                "exclude_banned_modqueue", exclude_banned_modqueue,
                "hide_ads", hide_ads,
                "lang", lang,
                "link_type", link_type,
                "name", name,
                "over_18", over_18,
                "public_description", public_description,
                "public_traffic", public_traffic,
                "show_media", show_media,
                "show_media_preview", show_media_preview,
                "spam_comments", spam_comments,
                "spam_links", spam_links,
                "spam_selfposts", spam_selfposts,
                "spoilers_enabled", spoilers_enabled,
                "submit_link_label", submit_link_label,
                "submit_text", submit_text,
                "submit_text_label", submit_text_label,
                "suggested_comment_sort", suggested_comment_sort,
                "title", title,
                "type", type,
                "wiki_edit_age", wiki_edit_age,
                "wiki_edit_karma", wiki_edit_karma,
                "wikimode", wikimode,
                "sr", fullname
        );

        genericPost(reddit.request()
                .path("/api/site_admin")
                .post(args)
                .build());
    }

    /**
     * Sets whether or not this submission should be marked as not safe for work
     *
     * @param s    The submission to modify
     * @param nsfw Whether or not this submission is not safe for work
     * @throws net.dean.jraw.http.NetworkException If the request was not successful
     * @throws net.dean.jraw.ApiException          If the API returned an error
     */
    @EndpointImplementation({Endpoints.MARKNSFW, Endpoints.UNMARKNSFW})
    public void setNsfw(Submission s, boolean nsfw) throws NetworkException,
            ApiException {
        // "/api/marknsfw" if nsfw == true, "/api/unmarknsfw" if nsfw == false
        genericPost(reddit.request()
                .endpoint(nsfw ? Endpoints.MARKNSFW : Endpoints.UNMARKNSFW)
                .post(JrawUtils.mapOf(
                        "id", s.getFullName()
                )).build());
    }

    /**
     * Deletes a submission that you posted
     *
     * @param thing The submission to delete
     * @param <T>   The Votable Thing to delete
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    public <T extends Thing & Votable> void delete(T thing)
            throws NetworkException, ApiException {
        delete(thing.getFullName());
    }
    
    /**
     * Mutes a user from the specified subreddit
     *
     * @param name   The user to mute
     * @param subreddit   The subreddit to mute from
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    @EndpointImplementation(Endpoints.OAUTH_ME_FRIENDS_USERNAME_PUT)
    public void muteUser(String subreddit, String name) throws NetworkException, ApiException {
        Map<String, String> args = JrawUtils.mapOf(
                "name", name,
                "type", "muted"
        );

        genericPost(reddit.request()
                .path("/r/" + subreddit + "/api/friend")
                .post(args)
                .build());
    }

    /**
     * Bans a user for a set amount of days with a reason, message, and ability to PM the user
     *
     * @param name   The user to ban
     * @param subreddit   The subreddit to ban from
     * @param reason Why the user is being banned
     * @param note Note (not required)
     * @param message Message to send to the user (not required)
     * @param days Number of days the ban is active
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    @EndpointImplementation(Endpoints.OAUTH_ME_FRIENDS_USERNAME_PUT)
    public void banUser(String subreddit, String name, String reason, String note, String message, int days) throws NetworkException, ApiException {
        Map<String, String> args = JrawUtils.mapOf(
                "name", name,
                "type", "banned",
                "ban_reason", reason,
                "duration", days
        );

        if (message != null)
            args.put("ban_message", message);
        if (note != null)
            args.put("note", note);

        genericPost(reddit.request()
                .path("/r/" + subreddit + "/api/friend")
                .post(args)
                .build());
    }

    /**
     * Bans a user permanently with a reason, message, and ability to PM the user
     *
     * @param name   The user to ban
     * @param subreddit   The subreddit to ban from
     * @param reason Why the user is being banned
     * @param note Note (not required)
     * @param message Message to send to the user (not required)
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    @EndpointImplementation(Endpoints.OAUTH_ME_FRIENDS_USERNAME_PUT)
    public void banUserPermanently(String subreddit, String name, String reason, String note, String message) throws NetworkException, ApiException {
        Map<String, String> args = JrawUtils.mapOf(
                "name", name,
                "type", "banned",
                "ban_reason", reason
        );

        if (message != null)
            args.put("ban_message", message);
        if (note != null)
            args.put("note", note);

        genericPost(reddit.request()
                .path("/r/" + subreddit + "/api/friend")
                .post(args)
                .build());
    }

    /**
     * Deletes a comment or submission that the authenticated user posted. Note that this call will never fail, even if
     * the given fullname does not exist.
     *
     * @param fullname The fullname of the submission or comment to delete
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    @EndpointImplementation(Endpoints.DEL)
    public void delete(String fullname) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.DEL)
                .post(JrawUtils.mapOf(
                        "id", fullname
                )).build());
    }

    /**
     * Distinguishes a comment or submission that a user posted.
     *
     * @param s      The thing to distinguish
     * @param status How to distinguish 's'
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    @EndpointImplementation(Endpoints.DISTINGUISH)
    public void setDistinguishedStatus(Thing s, DistinguishedStatus status) throws NetworkException, ApiException {
        String distinguish = status.getJsonValue();
        if (distinguish.equals("null")) distinguish = "no";
        if (distinguish.equals("moderator")) distinguish = "yes";
        genericPost(reddit.request()
                .endpoint(Endpoints.DISTINGUISH)
                .post(JrawUtils.mapOf(
                        "id", s.getFullName(),
                        "api_type", "json",
                        "how", distinguish
                )).build());
    }

    /**
     * Stickies a top-level comment that a user posted. Not that this ONLY applies to top-level
     * comments and will not return an error if run on a non top-level comment
     *
     * @param s      The top-level comment to sticky
     * @param sticky Whether to sticky or unsticky the comment
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    @EndpointImplementation(Endpoints.DISTINGUISH)
    public void setSticky(Comment s, boolean sticky) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.DISTINGUISH)
                .post(JrawUtils.mapOf(
                        "id", s.getFullName(),
                        "api_type", "json",
                        "how", "yes",
                        "sticky", sticky
                )).build());
    }

    /**
     * Set or unset a self post as a sticky. You must be a moderator of the subreddit the submission was posted in for
     * this request to complete successfully.
     *
     * @param s      The submission to set as a sticky. Must be a self post
     * @param sticky Whether or not to set the submission as a stickied post
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the Reddit API returned an error
     */
    @EndpointImplementation(Endpoints.SET_SUBREDDIT_STICKY)
    public void setSticky(Submission s, boolean sticky) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.SET_SUBREDDIT_STICKY)
                .post(JrawUtils.mapOf(
                        "api_type", "json",
                        "id", s.getFullName(),
                        "state", sticky
                )).build());
    }

    /**
     * Set or unset a self post to Contest Mode. You must be a moderator of the subreddit the submission was posted in for
     * this request to complete successfully.
     *
     * @param s      The submission to set as a sticky. Must be a self post
     * @param enabled Whether or not to set the submission to contest mode
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the Reddit API returned an error
     */
    @EndpointImplementation(Endpoints.SET_CONTEST_MODE)
    public void setContestMode(Submission s, boolean enabled) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.SET_CONTEST_MODE)
                .post(JrawUtils.mapOf(
                        "api_type", "json",
                        "id", s.getFullName(),
                        "state", enabled
                )).build());
    }

    /**
     * Locks a submission. You must be a moderator of the subreddit the submission was posted in for
     * this request to complete successfully.
     *
     * @param s      The submission to set as a sticky. Must be a self post
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the Reddit API returned an error
     */
    @EndpointImplementation(Endpoints.LOCK)
    public void setLocked(Submission s) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.LOCK)
                .post(JrawUtils.mapOf(
                        "api_type", "json",
                        "id", s.getFullName()
                )).build());
    }

    /**
     *Unlocks a submission. You must be a moderator of the subreddit the submission was posted in for
     * this request to complete successfully.
     *
     * @param s      The submission to set as a sticky. Must be a self post
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the Reddit API returned an error
     */
    @EndpointImplementation(Endpoints.UNLOCK)
    public void setUnlocked(Submission s) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.UNLOCK)
                .post(JrawUtils.mapOf(
                        "api_type", "json",
                        "id", s.getFullName()
                )).build());
    }

    @EndpointImplementation(Endpoints.APPROVE)
    public void approve(Thing s) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.APPROVE)
                .post(JrawUtils.mapOf(
                        "api_type", "json",
                        "id", s.getFullName()
                )).build());
    }

    @EndpointImplementation(Endpoints.REMOVE)
    public void remove(Thing s, boolean spam) throws NetworkException, ApiException {
        genericPost(reddit.request()
                .endpoint(Endpoints.REMOVE)
                .post(JrawUtils.mapOf(
                        "api_type", "json",
                        "id", s.getFullName(),
                        "spam", spam

                )).build());
    }

    /**
     * Sets the flair for the currently authenticated user
     *
     * @param subreddit The subreddit to set the flair on
     * @param template  The template to use
     * @param text      Optional text that will be used if the FlairTemplate's text is editable. If this is null and the
     *                  template is editable, the template's default text will be used.
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    public void setFlair(String subreddit, FlairTemplate template, String text) throws NetworkException, ApiException {
        setFlair(subreddit, template, text, (String) null);
    }

    /**
     * Sets the flair for a certain user. Must be a moderator of the subreddit if the user is not the currently
     * authenticated user.
     *
     * @param subreddit The subreddit to set the flair on
     * @param template  The template to use
     * @param text      Optional text that will be used if the FlairTemplate's text is editable. If this is null and the
     *                  template is editable, the template's default text will be used.
     * @param username  The name of the user to set the flair for. If this is null the authenticated user's name will be
     *                  used.
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    public void setFlair(String subreddit, FlairTemplate template, String text, String username) throws NetworkException, ApiException {
        setFlair(subreddit, template, text, null, username);
    }

    /**
     * Sets the flair for a certain submission. If the currently authenticated user is <em>not</em> a moderator of the
     * subreddit where the submission was posted, then the user must have posted the submission.
     *
     * @param subreddit  The subreddit to set the flair on
     * @param template   The template to use
     * @param text       Optional text that will be used if the FlairTemplate's text is editable. If this is null and the
     *                   template is editable, the template's default text will be used.
     * @param submission The submission to set the flair for
     * @throws NetworkException If the request was not successful
     * @throws ApiException     If the API returned an error
     */
    public void setFlair(String subreddit, FlairTemplate template, String text, Submission submission) throws NetworkException, ApiException {
        setFlair(subreddit, template, text, submission, null);
    }

    /**
     * Sets either a user's flair or a submission's flair. If the submission and username are both non-null, then the
     * submission will be used in the request. If they are both null and there is no authenticated user, then an
     * IllegalArgumentException will be thrown.
     *
     * @param subreddit  The subreddit where the flair will take effect
     * @param template   The template to use
     * @param text       Optional text that will be used if the FlairTemplate's text is editable. If this is null and the
     *                   template is editable, the template's default text will be used.
     * @param submission The submission to set the flair for
     * @param username   The name of the user to set the flair for. If this is null the authenticated user's name will be
     *                   used.
     * @throws IllegalArgumentException If both the submission and the username are null
     * @throws NetworkException         If the request was not successful
     */
    @EndpointImplementation(Endpoints.SELECTFLAIR)
    private void setFlair(String subreddit, FlairTemplate template, String text, Submission submission, String username)
            throws IllegalArgumentException, NetworkException, ApiException {
        if (subreddit == null) {
            throw new IllegalArgumentException("subreddit cannot be null");
        }
        Map<String, String> args = JrawUtils.mapOf(
                "api_type", "json",
                "flair_template_id", template.getId()
        );

        if (submission != null) {
            args.put("link", submission.getFullName());
        } else {
            if (username == null) {
                if (reddit.getAuthenticationMethod() == AuthenticationMethod.NOT_YET) {
                    throw new IllegalArgumentException("Not logged in and both submission and username were null");
                }
                if (!reddit.hasActiveUserContext())
                    throw new IllegalStateException("Cannot set the flair for self because there is no active user context");
                username = reddit.getAuthenticatedUser();
            }
            args.put("name", username);
        }

        if (template.isTextEditable()) {
            if (text == null) {
                // Set default text flair if none is provided
                text = template.getText();
            }
            args.put("text", text);
        }

        RestResponse response = reddit.execute(reddit.request()
                .post(args)
                .path("/r/" + subreddit + Endpoints.SELECTFLAIR.getEndpoint().getUri())
                .build());
        if (response.hasErrors()) {
            throw response.getError();
        }
    }
}
