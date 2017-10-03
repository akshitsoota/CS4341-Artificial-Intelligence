from collections import Counter
from itertools import tee

from keras.models import Sequential
from keras.layers import Dense, Activation
import numpy as np
from werkzeug._compat import izip


def printimage(arr, char='X'):
    """Convert image data from an array to a string.

    Nonzero characters will be rendered as the given character, and zeros will
    be rendered as spaces.

    Adapted from: https://canvas.wpi.edu/courses/4977/discussion_topics/23672
    """
    print('\n'.join(''.join(char if i else ' ' for i in row) for row in arr))


def generate_hot_one_vector(label, start=0, end=9):
    original = [0] * (end - start + 1)
    original[label] = 1

    return original


# Start loading up the files

images = np.load("images.npy")
labels = list(np.array(np.load("labels.npy")))

# Covert each of the images from a matrix to a vector
flattened_images = [list(np.array(img).flatten()) for img in images]

# Convert each label to hot-one vector
labels = list(map(generate_hot_one_vector, labels))

# Create mapping for one-hot vector to all matching
assert len(images) == len(labels)

mapping = {}
for idx, image in enumerate(images):
    the_tuple = tuple(labels[idx])
    if the_tuple in mapping:
        mapping[the_tuple].append(image)
    else:
        mapping[the_tuple] = [image]

# Shuffle the values in the mapping

for key in range(len(mapping.keys())):
    np.random.shuffle(mapping[tuple(generate_hot_one_vector(key))])
    # import hashlib
    # print(hashlib.sha1(str(mapping[tuple(generate_hot_one_vector(key))]).encode('utf-8')).hexdigest())

# Model Template

model = Sequential()  # declare model
model.add(Dense(10, input_shape=(28 * 28,), kernel_initializer='he_normal'))  # first layer
model.add(Activation('relu'))
#
#
#
# Fill in Model Here
#
#
model.add(Dense(10, kernel_initializer='he_normal'))  # last layer
model.add(Activation('softmax'))

# Compile Model
model.compile(optimizer='sgd',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

# Train Model
history = model.fit(x_train, y_train,
                    validation_data=(x_val, y_val),
                    epochs=10,
                    batch_size=512)

# Report Results

print(history.history)
model.predict()
