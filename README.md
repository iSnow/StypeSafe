StypeSafe
=========

Add the missing features of stypi.com

###Purpose
Stypesafe tries to add some of the more needed features to Stypi. Amongst those are:

* Backups. While I love Stypi, I would like to have the possibility to download my work at any time -- all files in a nice ZIP-archive. As of this writing, this is missing.

* Revision control beyond the film camera view.

* Visual diffs between revisions.

Since Stypi does not feature an API, StypeSafe emulates a web-browser to spider documents shared to you and pulls them to a local repository.

After each run, it builds a JSON-file containing the latest revision number and number of revisions of all the files. 

This JSON can be used by a HTML-App to show you the list of your monitored files and provide links into Stypi as well as to the next screen, where it lists all available revisions to a selected file.

In this screen, you can select two revisions and hit "Compare Versions" to get a side-by-side comparison with changes highlighted in red (deleted in newer revision), green (added in newer revision) and yellow (changed in newer revision).

###System requirements
For StypeSafe to be usable, you need:

* A computer that is online 24/7. This can preferably be a server somewhere (including your home if you are on a DSL/Cable modem connection). You should be able to schedule cron-jobs.

* Java SE 6 running on this computer.

* Any static HTTP-Server if you want to use the HTML app.

* A mySQL 5.1 or newer server for features that are not yet implemented. You need it anyways or you have to cull all database-related code. 

###Required libraries
####For the HTML webapp, you need the following libraries:

* difflib.js/diffview.js (https://github.com/cemerick/jsdifflib)

* jQuery

* jquery.metadata.js

* jquery.tablesorter.min.js (the _forked_ version from https://github.com/Mottie/tablesorter)

* jquery.timeago.js (https://github.com/rmm5t/jquery-timeago)

* jstz.min.js (https://bitbucket.org/pellepim/jstimezonedetect/downloads)

####To build the stypesafe.jar, you need the following libraries:

* apache-log4j-1.2.17

* gson-1.7.1

* httpcomponents-client-4.2.1

* ini4j-0.5.2

* jOOQ-2.4.2-full

* jsoup-1.6.3.jar

* junit-4.10.jar

* mysql-connector-java-5.1.15

###File system layout
StypeSafe consists of two parts:

* The spider which writes the monitored files and the sitemap.json into an archive directory

* The HTML app that reads sitemap.json and archived versions from that directory.

**Warning** Putting the spider into a directory accessible by the HTTP server opens a security hole because the config.txt file used to configure the spider contains login and passowrd information. So don't do that.

1) So start by creating an web directory and a worker directory

    web/
     + archive/
     + assets/
     + index.html
     + zip/

    worker/
     + config.txt
     + stypesafe.jar
     + stypesafe.sh

Note: you must edit stypesafe.sh and replace <path-to-worker-directory> with your real paths. Also you need to edit the path for JavaHome.

2) If your file layout is done, edit config.txt and enter your stypi credentials as well as the information needed to access the database.

3) Create a database and use the SQL-Skript to create the tables. Again, this is currently not yet needed for anything, but the code requires it.

4) Next, edit crontab to schedule a recurring job:

    00,30 * * * * <path-to-worker-directory>/stypesafe.sh

This will run stypesafe.jar every half hour. 

5) Point your browser to the index.html file and wait till the cronjob retrieves new versions. Errors are logged to `stypi.error.log` in the worker directory where a `log.txt` captures all the conversation between stypesafe.jar and the Stypi webserver if you need to debug your setup.


