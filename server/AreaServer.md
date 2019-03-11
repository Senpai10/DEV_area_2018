# Area

Area is a mobile and web service that allows you to configure triggers. Those triggers are made of two things :

* Actions *(ex : A new youtube video is realeased)*
* Reactions *(ex : the user post a tweet)*

## Installation

### Requirement

* Docker CE

### Usage

Clone the Area repository :

`git clone git@git.epitech.eu:/bryan.medica@epitech.eu:/DEV_area_2018`

In order to run all the application elements, run at the root of the repository :

`docker-compose build && docker-compose up`

***

## Database

There is only one collection stored in our Firebase database : User's informations.

### Users : Basic informations

Each user is automatically associated to a unique token given by Firebase.

Here is the '**Users**' model :

Field|Data type|Exemple
|:---:|:---:|:---:|
email|String|area@gmail.com
firstName|String|John
lastName|String|Codeur
password|String|password/2019
tokens|Array of tokens|*Token model*
triggers|Array of triggers|*Triggers model*

### Users : Tokens model

All the user tokens are stored in this model.

Field|Data type|Exemple
|:---:|:---:|:---:|
area|String|-LZVMRTNLvZFNUzztVS9
googleId|String|114330141921517518662
googleToken|String|ya29.GlvFBnH_NRgUZ...
twitterToken|String|thvxy2fcVy7VzQRpsY...
twitterSecret|String|3066469913-qGT5Fr...

'area' token is a copy of the user firebase's token.

### Users : Triggers model

All the user triggers are stored in this model.

Field|Data type|Exemple
|:---:|:---:|:---:|
jobId|String|-L_NuVKM2vzfq7te-Mha15520586257172
action|Dict|*Action's data and id*
reaction|Dict|*Reaction's data and id*

Thanks to NoSQL firebase's format, actions and reactions datas depending on the action.

A jobId is used to clearly identify a job. It's created adding the user token and the timestamp.

***

## Requests

All the requests' responses are formated with :

- a **msg** field explicitly explaining the reason of the error if there is one.
- a **data** field containing data when needed.

Status codes are really simple, 400 for errors and 200 for success.

Here will be listed all requests handled by the server :

### Authentication requests

---

#### Register
---

**Description** : Registering a new user and saving his informations in the database.

**URL** : `/register`

**Method** : `POST`

**Header** : No header

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
firstName|String|John
lastName|String|Codeur
email|String|email.area@gmail.com
password|String|password/2019

**Responses** :

Response | Success of the request
|:---:|:---:|
User added | ✅
Missing parameters | ❌
Email already in use | ❌

#### Login
---

**Description** : Loging in a new user, validating his informations from the database.

**URL** : `/login`

**Method** : `POST`

**Header** : No header

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
email|String|email.area@gmail.com
password|String|password/2019

**Responses** :

Response|Success of the request
|:---:|:---:|
*userToken**| ✅
Missing parameters|❌
Bad password|❌
Unknown user|❌

**Has to be replaced by the user token*

### Tokens management requests
---

#### Add Twitter tokens
---

**Description** : Adding Twitter's tokens to user's database.

**URL** : `/addTwitterToken`

**Method** : `POST`

**Header** :

Field|Exemple
|:---:|:---:|
|Authorization|Bearer -LXPnsKCa2DN_BeONM-G|

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
twitterToken|String|x5DQhR9RY4dKlFI9FYXArfo...
twitterSecret|String|1065621705882783744-15kC...

**Responses** :

Response|Success of the request
|:---:|:---:|
Tokens added| ✅
Missing parameters|❌
Unknown user|❌

#### Add Google tokens
---

**Description** : Adding Google's tokens to user's database.

**URL** : `/addGoogleToken`

**Method** : `POST`

**Header** :

Field|Exemple
|:---:|:---:|
|Authorization|Bearer -LXPnsKCa2DN_BeONM-G|

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
googleId|String|x5DQhR9RY4dKlFI9FYXArfo...
googleToken|String|1065621705882783744-15kC...

**Responses** :

Response|Success of the request
|:---:|:---:|
Tokens added| ✅
Missing parameters|❌
Unknown user|❌

### Triggers management requests
---

#### Add trigger
---

**Description** : Adding a trigger to user's database.

**URL** : `/addTrigger`

**Method** : `POST`

**Header** :

Field|Exemple
|:---:|:---:|
|Authorization|Bearer -LXPnsKCa2DN_BeONM-G|

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
action_id|String|1
reaction_id|String|0
action_data|Dict|*depends on the action*
reaction_data|Dict|*depends on the reaction*

**Responses** :

Response|Success of the request
|:---:|:---:|
Here you are| ✅
Missing parameters|❌
Unknown user|❌

#### Get triggers
---

**Description** : Getting all the triggers from the user's database.

**URL** : `/getTriggers`

**Method** : `GET`

**Header** :

Field|Exemple
|:---:|:---:|
|Authorization|Bearer -LXPnsKCa2DN_BeONM-G|

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
triggerId|String|1

**Responses** :

Response|Success of the request
|:---:|:---:|
Here are the triggers| ✅
Unknown user|❌

#### Remove trigger
---

**Description** : Removing a trigger from the user's database.

**URL** : `/rmvTriggers`

**Method** : `POST`

**Header** :

Field|Exemple
|:---:|:---:|
|Authorization|Bearer -LXPnsKCa2DN_BeONM-G|

**Body** :

Field|Data type|Exemple
|:---:|:---:|:---:|
triggerId|String|1

**Responses** :

Response|Success of the request
|:---:|:---:|
Trigger deleted| ✅
Invalid trigger id|❌
Unknown user|❌

### Others requests
---
#### About
---

**Description** : Sending back a file describing services, actions and reactions.

**URL** : `/about.json`

**Method** : `GET`

**Header** : No header

**Body** : No body

**Responses** :

Response|Success of the request
|:---:|:---:|
*about.json file*| ✅

***

## Triggers data

### Actions
---

#### Clash Royale : Trophies
---

**Action id** : 0

**Authentication needed** : None

**Description** : Triggers when you reach a defined number of trophies.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
tag|String|8P8P8UOG8
record|String|4900

#### Clash Royale : Above friend
---

**Action id** : 1

**Authentication needed** : None

**Description** : Triggers when you pass beyond your friend trophies.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
tag|String|8P8P8UOG8
friend_tag|String|YQU2RQQ

#### Clash Royale : Clan score
---

**Action id** : 2

**Authentication needed** : None

**Description** : Triggers when your clan reach a defined score.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
clan_tag|String|898G0P2P
goal|String|50000

#### CryptoService : Cryptocurrency value
---

**Action id** : 3

**Authentication needed** : None

**Description** : Triggers when the currency pass beyond defined value.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
symbol|String|BTC
value_cap|String|5000

#### Github : New commit
---

**Action id** : 4

**Authentication needed** : None

**Description** : Triggers when a new commit is made on a repository.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
repo|String|loungevibes
owner|String|Leom3

#### Twitch : New stream
---

**Action id** : 5

**Authentication needed** : None

**Description** : Triggers when the defined channel starts a new stream.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
channel|String|solaryfortnite

#### Weather : Alert hot
---

**Action id** : 6

**Authentication needed** : None

**Description** : Triggers when a the temperature in a city pass beyond a defined value.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
temperature|String|36
city|String|toulouse

#### Weather : Alert cold
---

**Action id** : 7

**Authentication needed** : None

**Description** : Triggers when a the temperature in a city pass below a defined value.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
temperature|String|-10
city|String|laval

#### Twitter : Followers
---

**Action id** : 8

**Authentication needed** : Twitter OAuth1 tokens

**Description** : Triggers when the user has reached a defined number of followers.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
followers|String|36

#### Twitter : Friend followers
---

**Action id** : 9

**Authentication needed** : Twitter OAuth1 tokens

**Description** : Triggers when the user has now more followers than another twitto.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
friend_name|String|mzuckerberg

#### Youtube : Subscribers
---

**Action id** : 10

**Authentication needed** : Google OAuth2 tokens

**Description** : Triggers when the channel has reached a defined number of subscribers.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
subscribers|String|36
channel|String|pewdiepie

#### Youtube : Above channel subscribers
---

**Action id** : 11

**Authentication needed** : Google OAuth2 tokens

**Description** : Triggers when the channel has more subscribers than another one.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
channel|String|pewdiepie
friend_channel|String|tseries

#### Youtube : New video
---

**Action id** : 12

**Authentication needed** : Google OAuth2 tokens

**Description** : Triggers when the channel release another video.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
channel|String|pewdiepie

#### Github : Repository stars
---

**Action id** : 13

**Authentication needed** : None

**Description** : Triggers when a repository has reached a defined number of stars.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
repo|String|loungevibes
owner|String|Leom3
stars|String|3

#### League of Legends : Level up
---

**Action id** : 14

**Authentication needed** : None

**Description** : Triggers when the player reach a defined level.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
summoner|String|shbeark
region|String|euw

#### League of Legends : Bestof
---

**Action id** : 15

**Authentication needed** : None

**Description** : Triggers when the player is in a bestof for a rank up.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
summoner|String|shbeark
region|String|euw

### Reactions
---

#### Twitter : Tweet
---

**Reaction id** : 0

**Authentication needed** : Twitter OAuth1 tokens

**Description** : Update user twitter's status

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
msg|String|'Updating my status'

#### Twitter : Send dm
---

**Reaction id** : 1

**Authentication needed** : Twitter OAuth1 tokens

**Description** : Send a direct message to a defined user

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
msg|String|'Hi, do you know someone who could offer me an internship ?'
dest|String|mzuckerberg

#### MailService : Receive a mail
---

**Reaction id** : 2

**Authentication needed** : None

**Description** : Send a mail with *areaepitech31@gmail.com* to a defined recipient.

**Data** :

Field|Data type|Exemple
|:---:|:---:|:---:|
recipient|String|mzuck@facebook.com
message|String|Hello world !

***

## AppScheduler

AppScheduler is the external library that allows us to verify if an action has been done. It run jobs at a predefined interval.

In order to stop a job, we created jobIds. It's created adding the user token and the Epoch timestamp.


## License
[MIT](https://choosealicense.com/licenses/mit/)
