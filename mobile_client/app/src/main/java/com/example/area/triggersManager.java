package com.example.area;

import android.util.Log;

import org.json.JSONObject;

import static android.support.constraint.Constraints.TAG;

/**
 * The type Triggers manager.
 */
public class triggersManager {
    /**
     * Handle clash royal record string.
     *
     * @param item the item
     * @return the string
     */
    public String handleClashRoyalRecord(JSONObject item) {
        String record;
        String tag;
        try {
            record = item.getJSONObject("action").getJSONObject("data").getString("record");
            tag = item.getJSONObject("action").getJSONObject("data").getString("tag");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If your tag " + tag + " surpass your record " + record);
    }

    /**
     * Handle clash royal above friend string.
     *
     * @param item the item
     * @return the string
     */
    public String handleClashRoyalAboveFriend(JSONObject item) {
        String tagFriend;
        String tag;
        try {
            tagFriend = item.getJSONObject("action").getJSONObject("data").getString("friend_tag");
            tag = item.getJSONObject("action").getJSONObject("data").getString("tag");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If your tag " + tag + " surpass the tag " + tagFriend);
    }

    /**
     * Handle clash royal clan score string.
     *
     * @param item the item
     * @return the string
     */
    public String handleClashRoyalClanScore(JSONObject item) {
        String clan_tag;
        String score;
        try {
            clan_tag = item.getJSONObject("action").getJSONObject("data").getString("clan_tag");
            score = item.getJSONObject("action").getJSONObject("data").getString("score");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If your clan tag " + clan_tag + " surpass your record " + score);
    }

    /**
     * Handle crypto string.
     *
     * @param item the item
     * @return the string
     */
    public String handleCrypto(JSONObject item) {
        String symbol;
        String valueCap;
        try {
            symbol = item.getJSONObject("action").getJSONObject("data").getString("symbol");
            valueCap = item.getJSONObject("action").getJSONObject("data").getString("value_cap");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the " + symbol + " surpass the value " + valueCap);
    }

    /**
     * Handle github commit string.
     *
     * @param item the item
     * @return the string
     */
    public String handleGithubCommit(JSONObject item) {
        String repo;
        String owner;
        try {
            repo = item.getJSONObject("action").getJSONObject("data").getString("repo");
            owner = item.getJSONObject("action").getJSONObject("data").getString("owner");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the repository " + repo + " of " + owner + " have a new commit");
    }

    /**
     * Handle twitch string.
     *
     * @param item the item
     * @return the string
     */
    public String handleTwitch(JSONObject item) {
        String channel;
        try {
            channel = item.getJSONObject("action").getJSONObject("data").getString("channel");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the " + channel + " is on Live");
    }

    /**
     * Handle weather hot string.
     *
     * @param item the item
     * @return the string
     */
    public String handleWeatherHot(JSONObject item) {
        String city;
        String temperature;
        try {
            city = item.getJSONObject("action").getJSONObject("data").getString("city");
            temperature = item.getJSONObject("action").getJSONObject("data").getString("temperature");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the " + city + " reach " + temperature + "°C (Hot)");
    }

    /**
     * Handle weather cold string.
     *
     * @param item the item
     * @return the string
     */
    public String handleWeatherCold(JSONObject item) {
        String city;
        String temperature;
        try {
            city = item.getJSONObject("action").getJSONObject("data").getString("city");
            temperature = item.getJSONObject("action").getJSONObject("data").getString("temperature");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the " + city + " reach " + temperature + "°C (Cold)");
    }

    /**
     * Handle twitter followers string.
     *
     * @param item the item
     * @return the string
     */
    public String handleTwitterFollowers(JSONObject item) {
        String followers;
        try {
            followers = item.getJSONObject("action").getJSONObject("data").getString("followers");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If you reached " + followers + " followers");
    }

    /**
     * Handle twitter friend followers string.
     *
     * @param item the item
     * @return the string
     */
    public String handleTwitterFriendFollowers(JSONObject item) {
        String friend_name;
        try {
            friend_name = item.getJSONObject("action").getJSONObject("data").getString("friendName");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If you reached the number of follower of " + friend_name);
    }

    /**
     * Handle youtube record string.
     *
     * @param item the item
     * @return the string
     */
    public String handleYoutubeRecord(JSONObject item) {
        String channel;
        String subs;
        try {
            channel = item.getJSONObject("action").getJSONObject("data").getString("channel");
            subs = item.getJSONObject("action").getJSONObject("data").getString("subscribers");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the channel " + channel + " reach " + subs + " subscribers");
    }

    /**
     * Handle youtube above channel string.
     *
     * @param item the item
     * @return the string
     */
    public String handleYoutubeAboveChannel(JSONObject item) {
        String channel;
        String friend_subs;
        try {
            channel = item.getJSONObject("action").getJSONObject("data").getString("channel");
            friend_subs = item.getJSONObject("action").getJSONObject("data").getString("friend_channel");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the channel " + channel + " reach the number of subscribers of " + friend_subs);
    }

    /**
     * Handle youtube new video string.
     *
     * @param item the item
     * @return the string
     */
    public String handleYoutubeNewVideo(JSONObject item) {
        String channel;
        try {
            channel = item.getJSONObject("action").getJSONObject("data").getString("channel");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the channel " + channel + " release a new video" );
    }

    /**
     * Handle github stars string.
     *
     * @param item the item
     * @return the string
     */
    public String handleGithubStars(JSONObject item) {
        String repo;
        String owner;
        String stars;
        try {
            repo = item.getJSONObject("action").getJSONObject("data").getString("repo");
            owner = item.getJSONObject("action").getJSONObject("data").getString("owner");
            stars = item.getJSONObject("action").getJSONObject("data").getString("stars");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the repository " + repo + " of " + owner + " reach " + stars + " stars");
    }

    /**
     * Handle lol level up string.
     *
     * @param item the item
     * @return the string
     */
    public String handleLolLevelUp(JSONObject item) {
        String summoner;
        String region;
        try {
            summoner = item.getJSONObject("action").getJSONObject("data").getString("summoner");
            region = item.getJSONObject("action").getJSONObject("data").getString("region");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the summoner " + summoner + " from " + region + " have level up");
    }

    /**
     * Handle lol best of string.
     *
     * @param item the item
     * @return the string
     */
    public String handleLolBestOf(JSONObject item) {
        String summoner;
        String region;
        try {
            summoner = item.getJSONObject("action").getJSONObject("data").getString("summoner");
            region = item.getJSONObject("action").getJSONObject("data").getString("region");
        } catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Action: If the summoner " + summoner + " from " + region + " is on best of");
    }

    /**
     * Handle tweet string.
     *
     * @param item the item
     * @return the string
     */
    public String handleTweet(JSONObject item) {
        String msg;
        try {
            msg = item.getJSONObject("reaction").getJSONObject("data").getString("msg");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Reaction: Send a tweet: " + msg);
    }

    /**
     * Handle tweet dest string.
     *
     * @param item the item
     * @return the string
     */
    public String handleTweetDest(JSONObject item) {
        String msg;
        String dest;
        try {
            msg = item.getJSONObject("reaction").getJSONObject("data").getString("msg");
            dest = item.getJSONObject("reaction").getJSONObject("data").getString("dest");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Reaction: Send a tweet: " + msg + " to -> " + dest);
    }

    /**
     * Handle send mail string.
     *
     * @param item the item
     * @return the string
     */
    public String handleSendMail(JSONObject item) {
        String recipient;
        String message;
        try {
            recipient = item.getJSONObject("reaction").getJSONObject("data").getString("recipient");
            message = item.getJSONObject("reaction").getJSONObject("data").getString("message");
        }
        catch (Exception e) {
            Log.e(TAG, "An error as occured " + e.toString());
            return (null);
        }
        return ("Reaction: Send a mail: " + message + " to -> " + recipient);
    }
}
