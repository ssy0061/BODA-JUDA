from pydoc import plain
import requests
import json
from firebase_quickstart_python_remote_config import _get_access_token
from firebase_quickstart_python_remote_config import _publish
from firebase_quickstart_python_remote_config import _get
from image_maker import image_maker as im
from snack_classifier import snack_model_maker as smm
from slack_post import get_channel_id
from slack_post import image_upload
from slack_sdk import WebClient
import datetime as dt

ORIGIN_IMAGE_PATH = '/home/team1/AEye/data/image/snack_30'
INPUT_IMAGE_PATH = ORIGIN_IMAGE_PATH + '_rota'
MODEL_PATH = '/home/team1/AEye/model_float16'
KEY_PATH = '/home/team1/AEye/keys'
KEY_NAME = 'aeye-9c1ee-firebase-adminsdk-9glw7-8f233a41fb.json'
PROJECT_ID = 'aeye-9c1ee'
BASE_URL = 'https://firebaseremoteconfig.googleapis.com'
REMOTE_CONFIG_ENDPOINT = 'v1/projects/' + PROJECT_ID + '/remoteConfig'
REMOTE_CONFIG_URL = BASE_URL + '/' + REMOTE_CONFIG_ENDPOINT
JSON_PATH = '/home/team1/AEye/remote_config_info/config.json'
SLACK_TOKEN = "xoxb-3304749042240-3295636403457-PND6zGxAgmSk7NhoQYSnINeF"

# config.json 파일 읽기 ---------------------------------------------------------------------------
_get()

with open(JSON_PATH) as f:
   json_data = json.load(f)
# print(json_data)
# print(json.dumps(json_data, indent="\t") )

# config.json 파일 파라미터 수정 ------------------------------------------------------
model_name = json_data['parameters']['next_model']['defaultValue']['value']
next_name = 'snack_v' + str(int(model_name.split('_v')[1])+1)

smm(model_name,INPUT_IMAGE_PATH,MODEL_PATH,KEY_PATH, KEY_NAME)