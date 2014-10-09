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

Start
-----
Start the webapp using [`activator`][1]:

    $ activator run

By default the application listens on port 9000 of your machine.

[1]:https://typesafe.com/activator


