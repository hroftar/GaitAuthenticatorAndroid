# Prototype of an experimental mobile gait authentication system.
Experimental application for human gait identification implemented for Android devices

This project is a part of third year university project. 
Idea of the work was to investigate the possibility of apllication of an authentication system that relies on the user's gait, or on the way how the user walks.
In contrast to the typical approach used in this area, when the movements of the silhoette are recorded by capturing devices, and later analysed for some unique patterns, 
in this work the readings of in-built sensors of modern mobile devices, gyroscope, accelermeter, were used as a measure a gait. 

An Android application was created for gathering such data, that was later on analysed with Python and used for KNN model training. As every person has a unique way of 
walking, the pattern of corresponding sensor readings can be used for their distinguishing, that was used in the prototype authentication system implemented in Java 
for Android devices. 
