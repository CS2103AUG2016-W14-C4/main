# User Guide

* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Command Summary](#command-summary)

<!-- @@author A0130853L -->
<br>
## Introduction

Welcome! This user guide will provide you with all the essential information required for you to make full use of our task manager, TasKitty.

TasKitty is a task manager that can help you manage events, deadlines that you have to meet, or simply tasks that you want to get done whenever you have free time.

If you are a keyboard lover and dislike clicking, then TasKitty is the right task manager for you! It boasts an intuitive command line interface with minimal clicking required, and the commands you have to type in are short and sweet. 

To get started, proceed to the Quick Start section below.

<br>
<!-- @@author -->
## Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>
>   Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.  
   
<!-- @@author A0130853L -->

1. Download the latest `TasKitty.jar` from the [releases](../../../releases) tab.
2. Copy the file to the folder you want to use as the home folder for your TasKitty.
3. Double-click the file to start the app. The GUI should appear in a few seconds. <br>
   <img src="images/Ui.png" width="900">

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window. 
> While typing commands, tooltips will appear to aid you. <br>
5. Some example commands you can try:
   * **`add`**` read book` : adds a new task named `read book`. 
   * **`add`**` math homework 1 Jan 2015 2pm` : adds a new task that has the deadline `02:00pm,1 Jan 2015`.
   * **`view`** : lists all upcoming and uncompleted tasks.
   * **`view`**` 1 Jan 2015` : lists all tasks for the specific date.
   * **`delete`**` d1` : deletes the 1st task shown in the current list of deadline tasks.
   * **`exit`** : exits the program.
6. Refer to the [Features](#features) section below for details of each command.<br>

<br>
## Features

> Items in `<angle brackets>` are the parameters.<br>
> Items in `[square brackets]` are optional.<br>
> Items with `...` after them can have multiple instances.<br><br>
> `<datetime>` parameters can accept different formats. `2 Jan 2015 3pm`, `15:00 2/1/2015`, `2-1-2015, 3:00pm` are acceptable formats to represent 2 Jan 2015, 3pm.<br>
> `<datetime>` parameters can also accept relative date formats such as `tomorrow 4pm` and `next friday 2359`.<br><br>
> The order of parameters is fixed.<br>

<br>
#### View help : `help`
Format: `help`<br>
Shortcut key: `F1`


A pop-up window displaying the command summary will be shown. Help is also shown if you enter an incorrect command e.g. `abcd`.<br>
<img src="images/UIhelp.png" width="900">


<br>
#### View upcoming tasks: `view`
Lists all upcoming and uncompleted tasks.<br>
Format: `view`<br>
Shortcut Key: `Ctrl + Shift + T`

All uncompleted todo tasks, upcoming events and deadlines will be displayed.
> Note that overdue tasks will be displayed in red.<br>
> Also note that after each command, the current type of tasks being displayed will be shown on the bottom left corner of the task manager on the status bar.

Example:

* `view`<br>
  <img src="images/UIview.png" width="900">
 
 
<br>
#### View all tasks for a specified date: `view <date>`
Lists all events for the specified date, deadlines up to the specified date, and all todo tasks.<br>
Format: `view <date>`

All todo tasks, events for the specified date and deadlines up to the specified date will be displayed.<br>
If `view today` is entered, today's tasks, events and deadlines up to today will be displayed.

Example:

* `view 5 Nov`<br>
  <img src="images/UIviewDate.png" width="900">


<br>
#### View all completed tasks: `view done`
Lists all completed tasks, deadlines and events.<br>
Format: `view done`<br>
Shortcut key: `Ctrl + Shift + D`

All completed todo tasks, deadlines and events will be shown shown in grey.

Example:

* `view done`<br>
  <img src="images/UIviewDone.png" width="900">
  
<br>

#### View all tasks: `view all`
Lists all todo tasks, deadlines and events ever added.<br>
Format: `view all`<br>
Shortcut key: `Ctrl + Shift + L`

All todo tasks, deadlines and events ever added will be displayed, including events that have passed and done tasks.
> Note that events that are over will be automatically marked and displayed as completed.

Example:

* `view all`<br>
  <img src="images/UIviewAll.png" width="900">
  
<br>

<!-- @@author A0139930B -->
#### Create a new task: `add`
Adds a new task to the todo or deadlines list, or a new event to the event calendar.<br>

> Tasks are split into 3 categories: `todo`, `deadline`, `event`.<br>
> `todo`: Tasks that have no specific date/time to be completed by.<br>
> `deadline`: Tasks that have a specific date/time they must be completed by.<br>
> `event`: Tasks that have specific start and end date/time.<br>

* Todo format: `add <name> [#tag]...`<br>
* Deadline format: `add <name> [end datetime] [#tag]...`<br>
* Event format: `add <name> [start datetime] to [end datetime] [#tag]...`

<!-- @@author A0130853L -->
Examples:

* `add study for test`<br>
  Adds a `todo` task with `<name>` as `study for test`.<br>
  <img src="images/UItodo.png" width="900">

* `add math assignment 17 Nov 2pm`<br>
  Adds a `deadline` task with `<name>` as `math assignment`, `<datetime>` as `02:00pm, 17 Nov 2016`.<br>
  <img src="images/UIdeadline.png" width="900">

* `add walk dog 5 Nov 2016 17:00 to 18:00`<br>
  Adds an `event` task with `<name>` as `walk dog`, `<start datetime>` as `05:00pm, 5 Nov 2016`, `<end datetime>` as `06:00pm, 5 Nov 2016`.<br>
  <img src="images/UIevent.png" width="900">
  

<!-- @@author A0139930B -->
<br>
#### Find tasks: `find`
Finds tasks based on keywords.<br>
Format: `find <keyword> [more keywords]...`

Tasks that partly or completely match the keywords entered will be displayed. You can find tags using `#`<br>

Example: 
* `find assign`<br>
  <img src="images/UIfind.png" width="900">
  
* `find #work`<br>


<br>
<!-- @@author A0135793W -->
#### Edit task details: `edit`
Edits a todo, deadline or event already inside the task manager using the index of the task.<br>

* Format: `edit <index> [name] [datetime] `

`<index>` is the index of the task shown in the most recent listing, including the prefix, `t` todo, `d` deadline or `e` event. eg. `t2` `d1` `e4`<br>
If no or an invalid category was listed, the app will default to todo format `t`. eg. `1` and `+1` becomes `t1` <br>

Format depends on the type of task being edited. When only 1 `<time>` is provided, it is treated as `<end time>` for both deadline and event.<br>

> Note that you can enter the [`view`](#view-upcoming-tasks-view) command before the `edit` command, to view the list of tasks and events and edit the specified task accordingly. Alternatively, you can use the [`find`](#find-tasks-find) command to narrow down the displayed list of tasks and events.

<!-- @@author A0130853L -->
Example:

* `view`<br>
  `edit d2 math assignment2 15 Nov 2016`<br>
  Edits the 2nd task under the deadline tasks section. Changes the `<name>` to `math assignment2` and `<date>` to `15 Nov 2016`.<br>
  <img src="images/UIedit.png" width="900">

<br>

#### Delete task: `delete`
Deletes one or more todo, deadline or event task already inside the task manager using the index of the task.<br>

* Format: `delete <index> [more indexes]...`<br>

> You can enter tasks in a range by typing the category and first number index of the task followed by a `-` and then the final number index. eg. `t1-3` counts as todo tasks t1, t2 and t3 together.<br>

Examples:

* `view`<br>
  `delete d1`<br>
  Deletes the 1st task under the deadlines section as shown by the `view` command.<br>
  
  Before:<br>
  <img src="images/UIdeleteBefore.png" width="900"><br>
  
  After:<br>
  <img src="images/UIdeleteAfter.png" width="900"><br>
  
* `view 5 Nov 2016`<br>
  `delete e1`<br>
  Deletes the 1st task under the events section for `5 Nov 2016` as shown by the `view DATE` command.<br>
  
  Before:<br>
  <img src="images/UIdeleteBeforeDate.png" width="900"><br>
  
  After:<br>
  <img src="images/UIdeleteAfterDate.png" width="900"><br>
  
<!-- @@author A0139052L -->

* `view`<br>
  `delete t1 d1 e1`<br>
  Deletes the 1st task under the each section as shown by the `view` command.<br>
  
  Before:<br>
  <img src="images/UIdeleteMultipleBefore.png" width="900"><br>
  
  After:<br>
  <img src="images/UIdeleteMultipleAfter.png" width="900"><br>  
  
<!-- @@author A0130853L -->  

<br>

#### Mark task as done: `done`
Marks one or more task in the task list as done.<br>

* Format: `done <index> [more indexes]...`

> Tasks that are marked as done are moved to the bottom of the list in their respective sections.<br>
> Note that tasks that are marked as done will be sorted in reversed order based on their `<start datetime>`.<br>

Example:

* `view all`<br>
  `done d1`<br>
  Marks the 1st task today under the deadlines section shown by the `view all` command as completed.<br>
  
  Before:<br>
  <img src="images/UIdoneBefore.png" width="900"><br>
  
  After:<br>
  <img src="images/UIdoneAfter.png" width="900"><br>
<!-- @@author A0139052L --> 
 * `view all`<br>
  `done t1 t2 t3`<br>
  Marks the 1st 3 task under the todo section shown by the `view all` command as completed.<br>
  
  Before:<br>
  <img src="images/UIdoneMultipleBefore.png" width="900"><br>
  
  After:<br>
  <img src="images/UIdoneMultipleAfter.png" width="900"><br>
  
<!-- @@author A0130853L -->  

<br>

#### Undo previous action: `undo`
Undoes the last completed action.<br>
Format: `undo`<br>
Shortcut key: `Ctrl + Shift + U`

The previous version will be restored.<br>
User can keep undoing multiple actions until we reach the original version at the start of the current session.<br>

Example:

* `undo`<br>
  Undoes the last deleted item.<br>
  
  Before:<br>
  <img src="images/UIundoBefore.png" width="900"><br>
  
  After:<br>
  <img src="images/UIundoAfter.png" width="900"><br>

<br>

<!-- @@author A0139052L -->
#### Redo previous undone action: `redo`
Redoes the last undoned action.<br>
Format: `redo`<br>
Shortcut key: `Ctrl + Shift + Y`

The previous undone version will be restored.<br>
User can keep redoing multiple undone actions until we reach the latest version.<br>

Example:

* `redo`<br>
  Redoes the last deleted item.<br>
  
  Before:<br>
  <img src="images/UIundoAfter.png" width="900"><br>
  
  After:<br>
  <img src="images/UIredoAfter.png" width="900"><br>

<br>

> Note that for undo and redo, you are only able to undo/redo commands that changes the task manager. eg. add, delete
Commands that do not affect the task manager cannot be undone/redone. eg. view, help <br>

>If you have undone actions and you enter a new valid command that is undoable, previous undone actions that were not redone will be lost.<br>

>Note that undoing/redoing does not change the view status back to the one before undoing/redoing, it will stay at the current status until you enter a different command that changes the view status.

<br>
<!-- @@author A0135793W -->

#### Save/Load data: `path`
Saves data to a specified folder.<br>
Format: `path <filepath>.xml`

* Windows OS FILEPATH format example: `C:\\Users\\<username>\\Desktop\\CS2103 Tutorial\\TasKitty.xml`
* Mac OS FILEPATH format example: `/Users/<username>/Desktop/CS2103 Tutorial/TasKitty.xml`

> TasKitty must end with a .xml extension.<br>
> TasKitty will save any other FILEPATH format in the same directory as TasKitty.<br>
> TasKitty will automatically create the folder if the folder is not present.<br>
> TasKitty can load data from an existing .xml file if TasKitty is empty.<br>

Example:

* `path /Users/<username>/Desktop/CS2103 Tutorial/TasKitty.xml`<br>
  Saves TasKitty data into the folder CS2103 Tutorial with TasKitty.xml as the filename.<br>
  If folder CS2103 Tutorial is not present, TasKitty will create the folder.

<br>

<!-- @@author A0130853L -->
#### Clear all entries : `clear`
Clears all tasks from the task manager.<br>
Format: `clear`<br>
Shortcut key: `Ctrl + Shift + C`
<img src="images/UIclear.png" width="900">

<br>

#### Exit the program : `exit`
Exits the program.<br>
Format: `exit`  

<br>
<!-- @@author -->
## FAQ

**Q**: The app is not recognizing my tasks correctly!<br>
**A**: Try to use "quotes" around your task names to help TasKitty differentiate between your task name and other parameters.

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous task manager's folder.      

<br>

<!-- @@author A0130853L -->
## Command Summary

Command | Format  
-------- | :-------- 
Add todo | `add <name>`
Add deadline | `add <name> <end datetime>`
Add event | `add <name> <start datetime> to <end datetime>`
View upcoming | `view`
View all | `view all`
View date | `view <date>`
View done | `view done`
Find | `find <keyword> [more keywords]...`
Edit | `edit <index> <name> <datetime>`
Delete | `delete <index> [more indexes]...`
Done | `done <index> [more indexes]...`
Path | `path <filepath>.xml`
Undo | `undo`
Redo | `redo`
Help | `help`
Clear | `clear`
Exit | `exit`
