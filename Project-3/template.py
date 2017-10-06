import numpy as np
from keras.layers import Dense, Activation
from keras.models import Sequential


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
images = [list(np.array(img).flatten()) for img in images]

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

x_train = []
y_train = []
x_val = []
y_val = []
x_test = []
y_test = []

for key, value in mapping.items():
    size = len(value)

    train = value[0:int(size * 0.6)]
    x_train.extend(train)
    y_train.extend([key] * len(train))

    val = value[int(size * 0.6):int(size * 0.75)]
    x_val.extend(val)
    y_val.extend([key] * len(val))

    test = value[int(size * 0.75):]
    x_test.extend(test)
    y_test.extend([key] * len(test))

    assert len(x_train) == len(y_train)
    assert len(x_val) == len(y_val)
    assert len(x_test) == len(y_test)

x_train = np.array(x_train)
y_train = np.array(y_train)
x_val = np.array(x_val)
y_val = np.array(y_val)
x_test = np.array(x_test)
y_test = np.array(y_test)

# Model Template

model = Sequential()  # declare model
model.add(Dense(100, input_shape=(28 * 28,), kernel_initializer='he_normal'))  # first layer
model.add(Activation('selu'))

model.add(Dense(70, kernel_initializer='he_normal'))  # second layer
model.add(Activation('relu'))

model.add(Dense(40, kernel_initializer='he_normal'))  # third layer
model.add(Activation('tanh'))

model.add(Dense(10, kernel_initializer='he_normal'))  # last layer
model.add(Activation('softmax'))

# Compile Model
model.compile(optimizer='sgd',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

# Train Model
history = model.fit(x_train, y_train,
                    validation_data=(x_val, y_val),
                    epochs=100,
                    batch_size=256)

# Report Results

# score = model.evaluate(x_test, y_test, verbose=0)

print(history.history)


# Evaluate the model
score = model.evaluate(x_test, y_test, batch_size=256, verbose=0)
print('Test loss:', score[0])
print('Test accuracy:', score[1])

# y_evaluated = model.predict(x=x_test, batch_size=512)
# y_test = list(map(get_value_from_hot_one_vector, list(map(list, list(y_test)))))
#
# print('Test loss:', score[0])
# print('Test accuracy:', score[1])


# good_count, total_count = 0, 0
#
# for idx, evaluated in enumerate(y_evaluated):
#     evaluated = get_value_from_hot_one_vector(list(evaluated))
#
#     if y_test[idx] == evaluated:
#         good_count += 1
#     total_count += 1
#
# print(good_count, total_count)
# print(good_count / total_count)
