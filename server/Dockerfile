FROM python:alpine

RUN apk add --no-cache build-base
RUN pip install --upgrade pip
RUN pip install flask pyrebase werkzeug flask-cors apscheduler flask_oauthlib urllib3 flask_mail bs4

COPY ./ /server/

ENTRYPOINT ["python", "/server/main.py"]