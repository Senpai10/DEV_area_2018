var express = require('express')
var app = express()
var cors = require('cors')
var path = require('path')
var router = express.Router()
const passport = require('passport');
const TwitterStrategy = require('passport-twitter').Strategy
const GoogleStrategy = require('passport-google-oauth20');
const keys = require('./keys');
var request = require('request')
var userInfo = {
	email: "",
	token: ""
};

var twitterTokens = {
	twitterToken: "",
	twitterSecret: ""
};

var googleTokens = {
	googleToken: "",
	googleId: "38126387"
};

var smConnections = {
	twitterCo: false,
	googleCo: false
};

app.use(require('morgan')('combined'));
app.use(require('cookie-parser')());
app.use(require('body-parser').urlencoded({ extended: true }));
app.use(require('express-session')({ secret: 'keyboard cat', resave: true, saveUninitialized: true, secure: false}));
app.use(passport.initialize());
app.use(passport.session());

app.use(cors({
  'allowedHeaders': ['sessionId', 'Content-Type'],
  'exposedHeaders': ['sessionId'],
  'origin': '*',
  'methods': 'GET,HEAD,PUT,PATCH,POST,DELETE',
  'preflightContinue': false
}));

passport.use(
	new GoogleStrategy({
		callbackURL: '/auth/google/redirect',
		clientID: keys.google.clientID,
		clientSecret: keys.google.clientSecret
	}, (accessToken, refreshToken, profile, done) => {
		googleTokens.googleToken = accessToken;
		return done(null, {
			profile: profile,
			accessToken: accessToken
		});
	})
)

router.get('/auth/google', passport.authenticate('google', {
	scope: ['profile', 'https://www.googleapis.com/auth/youtubepartner-channel-audit']
}));

router.get('/auth/google/redirect', passport.authenticate('google'), (req, res) => {
	res.redirect('/profile');
});

router.get('/', function(req, res) {
	res.redirect('/login');
});

router.get('/login', function(req, res) {
	res.sendFile(path.join(__dirname + '/login.html'))
});

router.get('/register', function(req, res) {
	res.sendFile(path.join(__dirname + '/register.html'))
});

router.get('/home_page', function(req, res) {
	res.sendFile(path.join(__dirname + '/home_page.html'))
});

router.get('/profile', function(req, res) {
	res.sendFile(path.join(__dirname + '/profile.html'))
});

router.get('/action', function(req, res) {
	res.sendFile(path.join(__dirname + '/action.html'))
})

router.get('/btc-action', function(req, res) {
	res.sendFile(path.join(__dirname + '/Bitcoin_action.html'))
})

router.get('/clash-solo', function(req, res) {
	res.sendFile(path.join(__dirname + '/Clash_solo.html'))
})

router.get('/clash-friend', function(req, res) {
	res.sendFile(path.join(__dirname + '/Clash_friend.html'))
})

router.get('/clash-record', function(req, res) {
	res.sendFile(path.join(__dirname + '/Clan_record.html'))
})

router.get('/reaction', function(req, res) {
	res.sendFile(path.join(__dirname + '/reaction.html'))
})

router.get('/tweet', function(req, res) {
	res.sendFile(path.join(__dirname + '/tweet.html'))
})

router.get('/tweet-dm', function(req, res) {
	res.sendFile(path.join(__dirname + '/tweet_dm.html'))
})

router.get('/mail', function(req, res) {
	res.sendFile(path.join(__dirname + '/mail.html'))
})

router.get('/github-commit', function(req, res) {
	res.sendFile(path.join(__dirname + '/github_commit.html'))
})

router.get('/github-stars', function(req, res) {
	res.sendFile(path.join(__dirname + '/github_stars.html'))
})

router.get('/twitch-status', function(req, res) {
	res.sendFile(path.join(__dirname + '/twitch_status.html'))
})

router.get('/wheather-hot', function(req, res) {
	res.sendFile(path.join(__dirname + '/weather_hot.html'))
})

router.get('/wheather-cold', function(req, res) {
	res.sendFile(path.join(__dirname + '/weather_cold.html'))
})

router.get('/twitter-follower', function(req, res) {
	res.sendFile(path.join(__dirname + '/twitter_follower.html'))
})

router.get('/twitter-friend-follower', function(req, res) {
	res.sendFile(path.join(__dirname + '/twitter_friend_followers.html'))
})

router.get('/youtube-subscribers', function(req, res) {
	res.sendFile(path.join(__dirname + '/youtube_subscribers.html'))
})

router.get('/youtube-above-channel', function(req, res) {
	res.sendFile(path.join(__dirname + '/youtube_above_channel.html'))
})

router.get('/youtube-new-vid', function(req, res) {
	res.sendFile(path.join(__dirname + '/youtube_new_vid.html'))
})

router.get('/githubstars', function(req, res) {
	res.sendFile(path.join(__dirname + '/github_stars.html'))
})

router.get('/league-level', function(req, res) {
	res.sendFile(path.join(__dirname + '/league_level.html'))
})

router.get('/league-bo', function(req, res) {
	res.sendFile(path.join(__dirname + '/league_bo.html'))
})

passport.use(new TwitterStrategy({
    consumerKey: "KvnqhWlkxcjv8rqG8SHrbgb40",
    consumerSecret: "95MpkSC5A8fcngPtclWppwtxmZpWGJEasGt6BPRpr0K9efqeGo",
	callbackURL: "http://127.0.0.1:8081/auth/twitter/callback"
  },
  function(token, tokenSecret, profile, cb) {
  	twitterTokens.twitterToken = token;
  	twitterTokens.twitterSecret = tokenSecret;
	return cb(null, profile);
  }
));

passport.serializeUser(function(user, cb) {
	cb(null, user);
});
passport.deserializeUser(function(obj, cb) {
	cb(null, obj);
});

app.get('/auth/twitter', passport.authenticate('twitter'));

app.get('/auth/twitter/callback',
	passport.authenticate('twitter', {failureRedirect: '/profile'}),
	function(req, res) {
		res.redirect('/profile');
});

app.post('/userToken', function (req, res) {
	userInfo.email = req.body.email;
	userInfo.token = req.body.token;
});

app.get('/userToken', function (req, res) {
	res.send(userInfo);
});

app.get('/twitterTokens', function (req, res) {
	res.send(twitterTokens);
});

app.get('/googleTokens', function (req, res) {
	res.send(googleTokens);
});

app.post('/tConnection', function (req, res) {
	smConnections.twitterCo = req.body.connected;
});

app.post('/gConnection', function (req, res) {
	smConnections.googleCo = req.body.connected;
});

app.get('/smConnections', function (req, res) {
	res.send(smConnections);
});

app.get('/client.apk', function (req, res) {
	var file = __dirname + '/apk/app/build/outputs/apk/release/app-release.apk';
	res.download(file);
});

app.use(express.static(__dirname + '/frontend'))
app.use('/', router)
app.listen(8081)