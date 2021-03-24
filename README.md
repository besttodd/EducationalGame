# Educational Game aimed at High School Students
Includes a Maths mini-game and a Memory mini-game.
The aim of the maths game is to get the student to quickly calculate the sums in their head then compare the answers. 
The memory game shows a specific sequence that the student has to remember then repeat back in the same order. 
The harder levels show more options and the sequence plays faster.

The app makes use a range of different aspects of mobile computing. The memory game uses a looper to play the sequence and a stream buffer and adapter to read in and 
display the tile images. The high scores activity uses both the SQLite OpenHelper API, and Android Material Design to read and display contents of a database. 
The results activity also uses SQLite OpenHelper to write new high scores to this same database as well as Twitter integration through Twitter4J library to connect 
the app to social media. Most activities within the app also make use of SoundPool to play audio music and sound effects and the accelerometer to detect shake events. 
The app itself makes use of a toolbar, fragments and the lifecycle methods to make the app adaptable and easy to use on many different android devices.
