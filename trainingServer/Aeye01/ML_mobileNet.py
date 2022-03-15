# Import dependencies
import tensorflow as tf
from tensorflow import keras

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import random

print("TensorFlow version:", tf.__version__)

# Import MNIST dataset
mnist = keras.datasets.mnist
(train_images, train_labels), (test_images, test_labels) = mnist.load_data()

# Normalize the input image so that each pixel value is between 0 to 1.
train_images = train_images / 255.0
test_images = test_images / 255.0

# Add a color dimension to the images in "train" and "validate" dataset to
# leverage Keras's data augmentation utilities later.
train_images = np.expand_dims(train_images, axis=3)
test_images = np.expand_dims(test_images, axis=3)

# Define data augmentation configs
datagen = keras.preprocessing.image.ImageDataGenerator(
  rotation_range=30,
  width_shift_range=0.25,
  height_shift_range=0.25,
  shear_range=0.25,
  zoom_range=0.2
)

# Generate augmented data from MNIST dataset
train_generator = datagen.flow(train_images, train_labels)
test_generator = datagen.flow(test_images, test_labels)

# Define and train the Keras model.
model = keras.Sequential([
  keras.layers.InputLayer(input_shape=(28, 28, 1)),
  keras.layers.Conv2D(filters=32, kernel_size=(3, 3), activation=tf.nn.relu),
  keras.layers.Conv2D(filters=64, kernel_size=(3, 3), activation=tf.nn.relu),
  keras.layers.MaxPooling2D(pool_size=(2, 2)),
  keras.layers.Dropout(0.25),
  keras.layers.Flatten(),
  keras.layers.Dense(10, activation=tf.nn.softmax)
])
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])
model.fit(train_generator, epochs=5, validation_data=test_generator)

# Convert Keras model to TF Lite format and quantize.
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()
with open('mnist_v2.tflite', "wb") as f:
  f.write(tflite_model)

  import os
from google.colab import files
import firebase_admin
from firebase_admin import ml

uploaded = files.upload()

for fn in uploaded.keys():
  print('User uploaded file "{name}" with length {length} bytes'.format(
      name=fn, length=len(uploaded[fn])))
  os.environ["GOOGLE_APPLICATION_CREDENTIALS"]='/content/' + fn
  projectID = fn.rsplit("-firebase")[0]
  firebase_admin.initialize_app(
      options={'projectId': projectID, 
               'storageBucket': projectID + '.appspot.com' })

# This uploads it to your bucket as mmnist_v2.tflite
source = ml.TFLiteGCSModelSource.from_keras_model(model, 'mnist_v3.tflite')
print (source.gcs_tflite_uri)

# Create a Model Format
model_format = ml.TFLiteFormat(model_source=source)

# Create a Model object
sdk_model_1 = ml.Model(display_name="mnist_v3", model_format=model_format)

# Make the Create API call to create the model in Firebase
firebase_model_1 = ml.create_model(sdk_model_1)
print(firebase_model_1.as_dict())

# Publish the model
model_id = firebase_model_1.model_id
firebase_model_1 = ml.publish_model(model_id)