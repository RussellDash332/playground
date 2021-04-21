import tensorflow as tf
from tensorflow import keras

import matplotlib.pyplot as plt

import cv2 as cv
import numpy as np
import scipy as sp
from scipy import misc

from keras.preprocessing.image import ImageDataGenerator
from keras.optimizers import RMSprop
from keras.preprocessing import image

train_datagen = ImageDataGenerator(rescale=1/255)
validation_datagen = ImageDataGenerator(rescale=1/255)

####train_generator = train_datagen.flow_from_directory(
####        '/tmp/horse-or-human/',  # This is the source directory for training images
####        target_size=(300, 300),  # All images will be resized to 150x150
####        batch_size=128,
####        # Since we use binary_crossentropy loss, we need binary labels
####        class_mode='binary')

# Check the version
print(tf.__version__)

# History of the models
models = [
    keras.Sequential([
        keras.layers.Dense(units=1, input_shape=[1])
    ]),
    keras.models.Sequential([
        keras.layers.Flatten(), 
        keras.layers.Dense(128, activation=tf.nn.relu), 
        keras.layers.Dense(10, activation=tf.nn.softmax)
    ]),
    keras.models.Sequential([
        keras.layers.Flatten(input_shape=(28, 28)),
        keras.layers.Dense(512, activation=tf.nn.relu),
        keras.layers.Dense(10, activation=tf.nn.softmax)
    ]),
    keras.models.Sequential([
        keras.layers.Conv2D(64, (3,3), activation='relu', input_shape=(28, 28, 1)),
        keras.layers.MaxPooling2D(2, 2),
        keras.layers.Conv2D(64, (3,3), activation='relu'),
        keras.layers.MaxPooling2D(2,2),
        keras.layers.Flatten(),
        keras.layers.Dense(128, activation='relu'),
        keras.layers.Dense(10, activation='softmax')
    ]),
    keras.models.Sequential([
        keras.layers.Conv2D(16, (3,3), activation='relu', input_shape=(300, 300, 3)),
        keras.layers.MaxPooling2D(2,2),
        keras.layers.Conv2D(32, (3,3), activation='relu'),
        keras.layers.MaxPooling2D(2,2),
        keras.layers.Conv2D(64, (3,3), activation='relu'),
        keras.layers.MaxPooling2D(2,2),
        keras.layers.Conv2D(64, (3,3), activation='relu'),
        keras.layers.MaxPooling2D(2,2),
        keras.layers.Conv2D(64, (3,3), activation='relu'),
        keras.layers.MaxPooling2D(2,2),
        keras.layers.Flatten(),
        keras.layers.Dense(512, activation='relu'),
        keras.layers.Dense(1, activation='sigmoid')
    ])
]

CURRENT_VERSION = len(models)-1

# Main function to train the MNIST data
def train_mnist():
    class myCallback(keras.callbacks.Callback):
        def on_epoch_end(self, epoch, logs={}):
            if(logs.get('acc',0)>0.99):
                print("\nReached 99% accuracy so cancelling training!")
                self.model.stop_training = True

    # Callback if accuracy is reached
    callback = myCallback()

    # Preprocess
    mnist = keras.datasets.fashion_mnist
    (training_images, training_labels), (test_images, test_labels) = mnist.load_data()
    training_images=training_images.reshape(60000, 28, 28, 1)
    training_images=training_images / 255.0
    test_images = test_images.reshape(10000, 28, 28, 1)
    test_images=test_images/255.0

    # Main model
    model = models[CURRENT_VERSION]

    # Compile the model
    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    # model.compile(loss='binary_crossentropy', optimizer=RMSprop(lr=0.001), metrics=['accuracy'])

    # Shows the model summary
    model.summary()

    # Fit model with training data
    history = model.fit(training_images, training_labels, epochs=5, callbacks=[callback])
    # history = model.fit(train_generator, steps_per_epoch=8, epochs=15, verbose=1, validation_data = validation_generator, validation_steps=8)

    # Evaluate model based on test data
    test_loss = model.evaluate(test_images, test_labels)

    return history.epoch, history.history['acc'][-1]

# Visualize the convolution process
def visualize(FIRST_IMAGE, SECOND_IMAGE, THIRD_IMAGE):
    f, axarr = plt.subplots(3,4)
    CONVOLUTION_NUMBER = 1
    from tensorflow.keras import models
    layer_outputs = [layer.output for layer in model.layers]
    activation_model = keras.models.Model(inputs = model.input, outputs = layer_outputs)
    for x in range(0,4):
        f1 = activation_model.predict(test_images[FIRST_IMAGE].reshape(1, 28, 28, 1))[x]
        axarr[0,x].imshow(f1[0, : , :, CONVOLUTION_NUMBER], cmap='inferno')
        axarr[0,x].grid(False)
        f2 = activation_model.predict(test_images[SECOND_IMAGE].reshape(1, 28, 28, 1))[x]
        axarr[1,x].imshow(f2[0, : , :, CONVOLUTION_NUMBER], cmap='inferno')
        axarr[1,x].grid(False)
        f3 = activation_model.predict(test_images[THIRD_IMAGE].reshape(1, 28, 28, 1))[x]
        axarr[2,x].imshow(f3[0, : , :, CONVOLUTION_NUMBER], cmap='inferno')
        axarr[2,x].grid(False)

# Barebone convolution algorithm
def explore_convo():
    i = misc.ascent() # Sample 'ascent' image

    ## Input image
    plt.grid(False)
    plt.gray()
    plt.axis('off')
    plt.imshow(i)
    plt.show()

    i_transformed = np.copy(i)
    size_x = i_transformed.shape[0]
    size_y = i_transformed.shape[1]

    ## Main filter
    filter = [
        [-1, -2, -1],
        [0, 0, 0],
        [1, 2, 1]
    ]
    weight = 1

    ## Convo process
    for x in range(1,size_x-1):
        for y in range(1,size_y-1):
            convolution = 0.0
            convolution = convolution + (i[x - 1, y-1] * filter[0][0])
            convolution = convolution + (i[x, y-1] * filter[0][1])
            convolution = convolution + (i[x + 1, y-1] * filter[0][2])
            convolution = convolution + (i[x-1, y] * filter[1][0])
            convolution = convolution + (i[x, y] * filter[1][1])
            convolution = convolution + (i[x+1, y] * filter[1][2])
            convolution = convolution + (i[x-1, y+1] * filter[2][0])
            convolution = convolution + (i[x, y+1] * filter[2][1])
            convolution = convolution + (i[x+1, y+1] * filter[2][2])
            convolution = convolution * weight
            if(convolution<0):
                convolution=0
            if(convolution>255):
                convolution=255
            i_transformed[x, y] = convolution

    ## Output the convo result
    plt.gray()
    plt.grid(False)
    plt.imshow(i_transformed)
    # plt.axis('off')
    plt.show()

    ## 2D Pooling
    new_x = int(size_x/2)
    new_y = int(size_y/2)
    newImage = np.zeros((new_x, new_y))
    for x in range(0, size_x, 2):
      for y in range(0, size_y, 2):
        pixels = []
        pixels.append(i_transformed[x, y])
        pixels.append(i_transformed[x+1, y])
        pixels.append(i_transformed[x, y+1])
        pixels.append(i_transformed[x+1, y+1])
        newImage[int(x/2),int(y/2)] = max(pixels)

    # Plot the image. Note the size of the axes -- now 256 pixels instead of 512
    plt.gray()
    plt.grid(False)
    plt.imshow(newImage)
    #plt.axis('off')
    plt.show()

def colab_complex():
    from google.colab import files
    
    uploaded = files.upload()

    for fn in uploaded.keys():
     
      # predicting images
      path = '/content/' + fn
      img = image.load_img(path, target_size=(300, 300))
      x = image.img_to_array(img)
      x = np.expand_dims(x, axis=0)

      images = np.vstack([x])
      classes = model.predict(images, batch_size=10)
      print(classes[0])
      if classes[0]>0.5:
        print(fn + " is a human")
      else:
        print(fn + " is a horse")
