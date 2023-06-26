# Mobile Scheduling Application
## Team Members:
- Simon Gordon ([gordons34](https://github.com/gordons34))
- Eric Cheung ([thebowser20](https://github.com/TheBowser20))
- Ben Walsh ([benwalsh23](https://github.com/benwalsh23))
- Mark D'Cruz ([dcruzm0](https://github.com/dcruzm0))

## Basic Introduction to Project

This was the final project for **CMPT 395: Software Engineering** where we were tasked with creating a mobile application for a client which served the following features:
<ul><li>Add / Edit / Remove employees</li>
<li>Schedule employees to shifts</li>
<li>Update availability, Add vacation, and other QOL features</li></ul>

The project was developed using [Android Studio Electric Eel 2022.1.1](https://developer.android.com/studio/releases/past-releases/as-electric-eel-release-notes), and followed Agile development, more specifically, Scrum. The project was broken into 4 sprints, each of which were 2 weeks in length.

## Main Menu

<img height="600px" align="left" src="https://github.com/Gordons34Repo/Mobile-Scheduling-Application/assets/135652713/595d1ea5-a60b-4592-acf4-2cbb56c0179e"/>

### Main Screen layout

The main screen went over several revisions before we reached our final layout of only 3 buttons. These options are **"View Schedule"**, **"Staff"**, and **"View Today"**. There is also an on-screen dispaly of the current time and date. It was discussed to add the employees working on the current day, but it was cut due to time constraints.

#### View Schedule

&nbsp;- This button redirects to view the [current month's schedule](#calendar).

#### Staff

&nbsp;- This button redirects to view the employee management section of the application.

#### View Today

&nbsp;- This button displays the [current day's scheduled employees](#calendar-continued--).<br clear="left"/>

## Calendar

<img height ="600px" align="right" src="https://github.com/Gordons34Repo/Mobile-Scheduling-Application/assets/135652713/07f05f50-8b8c-4e88-94ca-85fb53cf7b6f"/>

### Layout

the layout of the schedule was designed to concisely display the current month, and allow the user to easily cycle between other months. We were tasked to add some sort of display to represent how complete a day is, and we accomplished this by using a colored circle below the date. An orange circle indicates a day partially complete, a green circle indicates a complete day, and no circle indicates a day that has not been set.

### Challenges

<ul><li>Getting the days to display in the correct order.</li>
<li>Aligning the first day on the first week day of a month.</li>
<li>Displaying the correct number of days in a month.</li>
<li>Display which days are in progress or completed.</li></ul>

### Retrospective

Something I would like to have finished before the end of our last sprint was making the navigation UI a bit more fitting to the overal aesthetic of the application. On paper and in our sprint planning, this didn't seem like a very difficult task, but android studio was a surprisingly challenging tool for user interfaces.<br clear="right"/>

## Calendar Continuted. . .

<img height ="600px" align="left" src="https://github.com/Gordons34Repo/Mobile-Scheduling-Application/assets/135652713/44db06f8-2b44-457c-9c86-2f45d7641d13"/>

### Layout

This section of the app can be reached by either clicking on a day in the calendar, or clicking on the main menu button to "View Today". This page was designed to easily display monring and afternoon shifts, and allow the scheduling manager to slot colleagues accordingly. When selecting colleagues for a day, it calls upon the application's database to grab only colleagues that are available at that time. I go over this more in the [Staff] section.

### Challenges

&nbsp;- Pulling and loading data from the database.<br/>
&nbsp;- Grabbing only available colleagues from the database.<br/>
&nbsp;- Displaying colleagues in order using sharding.<br/>

### Retrospective

A major challenge in this section was the UI. To display colleagues reactively, we had to use a feature called "Sharding", wherein we create shards of a piece of UI and add it on the page. This allowed us to functionalize our code instead of hardcoding where the colleague slots are. This was important as the client would have some days where less people are scheduled than usual. Sharding had the unintended side effect of being extremely hard to stylize, which left us with an inconsistent UI design. IF we had more time this would definitely be something I would like to fix.
