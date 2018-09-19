import numpy as np
from keras.layers import Dense, Activation
from keras.models import Sequential
# import matplotlib.pyplot as plot
# from sklearn.metrics import classification_report,confusion_matrix

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

# Loop over this
iteration_count = 0
accuracy_sum = 0
loss_sum = 0

while True:
    # Shuffle the values in the mapping

    for key in range(len(mapping.keys())):
        np.random.shuffle(mapping[tuple(generate_hot_one_vector(key))])

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
                        batch_size=256,
                        verbose=0)

    # Evaluate the model
    score = model.evaluate(x_test, y_test, batch_size=256, verbose=0)
    loss_sum += score[0]
    accuracy_sum += score[1]
    iteration_count += 1

    # Print out the stats
    print("Iteration Number: {0}, Accuracy: {1:.5f}, Loss: {2:.5f}".format(iteration_count, score[1], score[0]))
    print("Iteration Number: {0}, Accuracy Average: {1:.5f}, Loss Average: {2:.5f}".format(iteration_count, accuracy_sum / iteration_count, loss_sum / iteration_count))
#history = model.load_weights("trained_model.h5")

# # Uncomment the following comment block to run accuracy plotting code and confusion matrix code
# # Note- requires matplotlib and sklearn packages. Import statements are included at the top but commented out
# # Adapted from http://learnandshare645.blogspot.in/2016/06/feeding-your-own-data-set-into-cnn.html
# training_loss = history.history['loss']
# validation_loss = history.history['val_loss']
# training_accuracy = history.history['acc']
# validation_accuracy = history.history['val_acc']
# plot.figure(1, figsize=(7,5))
# plot.plot(range(100), training_accuracy)
# plot.plot(range(100), validation_accuracy)
# plot.xlabel("Number of Epochs")
# plot.ylabel("Accuracy of Training & Validation Sets")
# plot.title("Training and Validation Set Accuracy vs Number of Epochs")
# plot.grid(True)
# plot.legend(["Training Set", "Validation Set"])
# plot.style.use(['classic'])
# plot.savefig('accuracy.pdf')
# Y_pred = model.predict(x_test)
# # print(Y_pred)
# y_pred = np.argmax(Y_pred, axis=1)
# print(y_pred)
# p = model.predict_proba(x_test) # to predict probability
# print("\n")
# print(classification_report(np.argmax(y_test,axis=1), y_pred))
# print(confusion_matrix(np.argmax(y_test,axis=1), y_pred))

# Report Results
print(history.history)

# Evaluate the model
score = model.evaluate(x_test, y_test, batch_size=256, verbose=0)
print('Test loss:', score[0])
print('Test accuracy:', score[1])

## Save the weights to the file (uncomment to run- needs h5py package)
# output_file_name = "trained_model.h5"
# model.save_weights(output_file_name, overwrite=True)