## Multimedia-Note

## Background
###Android
Android is a Linux-based operating system designed primarily for touchscreen mobile devices such as smartphones and tablet computers.

### Development Environment and Tool
This project is developed in J2SE and android SDK 2.1 on the Eclipse IDE integrated with Android Development Tools, run on the Win7 operation system. And the application is tested on the android mobile phone HTC G11.

## Application Design, Analysis and Implementation
### Application design
As shown in Fig .1 the use cases of our application, our project has such functions like: adding new notes, reviewing the list of recorded notes, checking the recorded note and deleting the recorded notes. We also add the function of inputting images from the photo gallery of the mobile phone, voice recording and video recording.

![](http://c1.staticflickr.com/9/8620/16531122978_ef69a4d181_b.jpg)

                              Fig 1. Use cases of multimedia notes


![](http://c1.staticflickr.com/9/8632/16692796596_b17a9a1849_b.jpg)

                             Fig 2. Activity Diagram of multimedia notes

Figure 2 is the activity diagram of multimedia notes. It shows the work flow of the application. When the application is started, we assume that it will first go into the main page which shows a list of the titles and recorded time of the recorded notes. Then user can click the title of the note to go into detailed page of the note. Or they can just add a new note through a button. When it is in editing mode, user can input text, image, audio record and video record. The content will be stored in the mobile phone if user finally decided to save them.

### Application Analysis
While for application analysis, we firstly analyze the part of logic and functions, then the part of UI.
Based on android SDK, we use the basic life circle of android activity, as Figure 3, to develop this application. The android Activity is a component of the application, which supplies a visual interface.

![](http://c1.staticflickr.com/9/8646/16098795003_ca15ebeccc_b.jpg)
 
                                Fig 3. Life cycle of an Android activity


We have created 4 activities and named them as “MainActivity”, “NoteCheckAcitivity”, “NoteEditActivity”, “MyAudioRecord”, which implement the main functions of this application.
MainAcitivity implements the Main Page referred to Fig 1. When starting this application, it first start MainActivity, the activity will first load the layout xml file to initial the layout, then retrieve the database and show recorded notes on the main page. 

On the List of recorded notes, when we click a note, then it will turn to another activity named NoteCheckActivity, which shows the detailed content of that note.
There are two button on main page, which separately implements the functions of adding a new note and exiting the application. We declare an android object named Intent to execute the switching between the current activity to NoteEditActivity, which implements the function of editing a new note.

In NoteEditActivity, the main problems are adding text, images, audios and videos. In order to save content and check content easily, we use an android integrated database-sqlite, to store data.
Then, we first choose image from the photo gallery of the mobile phone, and obtain the uri of file, show the image in the text view and record the uri in database.

For recording audio, since every mobile phone may has different software to record and play audio, we decide to implement a simple audio recorder integrated in this application in order to avoid some error. Using android SDK class, MediaRecorder, to implement this audio recorder. What is worth saying is how we save the audio. After recording, we save it with a file name with prefix and a random number at the root path of the SD card in the phone. And obtaining the absolute path of the audio file, then save it in database.

For recording video, it is easier to implement. Since the steps of recording and playing videos are simple. So we can just call the system camera, and use it to do the job of video recording. Then we save those videos with the same method as saving audios. Fig 4 shows the class diagram of this whole application.

![](http://c1.staticflickr.com/9/8606/16511421067_854520a988_b.jpg)

                                 Fig 4. Class diagram of multimedia notes


For the Layout of this application, we mainly using the xml file to implement each layout of the activities. Fig 5 is an example, the layout of MainActivity. 

A layout defines the visual structure for a user interface, such as the UI for an android activity. And Android provides a straightforward XML vocabulary that corresponds to the View classes and subclasses, such as those for widgets and layouts. Using Android's XML vocabulary, you can quickly design UI layouts and the screen elements they contain, in the same way you create web pages in HTML — with a series of nested elements. 
We mainly use LinearLyout as the basic layout. LinearLayout arranges its children in a single column or a single row. Only after we have defined the root element can we add other layout objects or widgets as child elements. While the layouts for our project are mainly static and form-based, hence, using android xml layout file with LinearLayout is effectively.

In order to implement the UI effect as Fig 5 shown, we defined the LinearLayout as root element, and add the android UI components as TextView, ListView and a child LinearLayout including two buttons.

![](https://c1.staticflickr.com/9/8665/16511420987_e67ed7be09.jpg)

                 Fig 5. Layout of MainActivity.  

![](http://c1.staticflickr.com/9/8658/16717429941_bc42bc80cf.jpg)

                 Fig 6. Layout of NoteCheckActivity

![](http://c1.staticflickr.com/9/8598/16717631322_5a67b10151.jpg)

                Fig 7. Layout of NoteEditActivity

![](http://c1.staticflickr.com/9/8674/16717429801_997415e941.jpg)

                Fig 8. Layout of MyAudioRecord


## Conclusion
Our current application can save text, image, audio and video in one note, and show the title and creating time of the note on the main page for user to review. While it can also allow user to update and delete the recorded notes.Our whole idea of designing and implementing this application is firstly design and analyze the functions of this application well, then implements the UI and finally implements the functions.However, it is simple and limited, but it is a good try for us to learn programming android application and understand the implication of multimedia. 

