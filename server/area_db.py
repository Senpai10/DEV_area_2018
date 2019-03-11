import pyrebase

config = {
    "apiKey": "AIzaSyCZmQ5YkymzSV3ec_ZyNGS__vFsZm1nfzw",
    "authDomain": "areaepitech-e12e6.firebaseapp.com",
    "databaseURL": "https://areaepitech-e12e6.firebaseio.com",
    "projectId": "areaepitech-e12e6",
    "storageBucket": "areaepitech-e12e6.appspot.com",
    "messagingSenderId": "864404009142"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()
auth = firebase.auth()
fire_user = auth.sign_in_with_email_and_password("medica.bryan@gmail.com", "firebase/2019")
