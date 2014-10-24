Virtaul Therapist
-----------------
Virtual Therapist is a platform for analysing and managing usege of the 
Virtual Therapist App.

Setup
-----
Copy `conf/application.conf.example` and to `conf/application.conf` and edit 
the database credentials to your needs. If you are a Unix user edit only 
`db.default.url`:

    db.default.url="mysql://user:password@localhost/datatabse"

If you are Windows user set the following settings:
    
    db.default.url="jdbc:mysql://3306/database"
    db.default.user="user"
    db.default.password="password"

Make sure the database exists, during start of the application the tables will
be created automatically.

When in production edit `application.secret` and set a unique key.

Start development server
------------------------
Start the webapp for development using [`activator`][1]:

    $ activator run

By default the application listens on port 9000 of your machine.


Start production server
-----------------------
First, make sure you've a set a application secret. Of cource, do not use the secret
in that
It's not recommmanded to start the server in production using `$activator run`.
["Play checks with SBT to see if any files have changed, and this may have significant performance impacts on your application."][2]

First, make sure that the user that is running the application is allowed to use port 80.

Clean the application by removing caches and compiled stuff. Now prepare the application
using the `stage` task.

    $ activator clean stage

An executable has been placed in `target/universal/stage/bin/` directory. Use this executable
to start the server.

    $ target/universal/stage/bin/virtualcoach-web -Dpidfile.path=/tmp/play.pid > /dev/null 2>&1 &

Stopping the server is easy:

    $ kill $(cat /tmp/play.pid)

[1]:https://typesafe.com/activator
[2]:https://www.playframework.com/documentation/2.4.x/Production
