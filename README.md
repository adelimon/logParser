# LogParser

Just a few words about this implementation:
* The SQL files can be found in the SQL subdirectory.  The requested queries are in their own (aptly named) files.  The
creation script is in its own file.

* I use a basic JDBC implementation here.  I was unclear if external libraries were allowed, so I did not use one
such as Hibernate or JPA.  Normally I would use one of those but for a simple problem I chose a simple implementation.

* The DB connection properties are inlined in the main class.  Normally this would be in the app server config or in 
a YAML file. Because of this if you use a different DB you'll need to change that over.   I did this this way as I chose
to focus on solving the problem rather than plumbing and also wasn't sure if I could use external libraries.

* My DB properties use a local instance that I have installed.  I have this machine blocking any incoming connections on 3306 so hence no password on the DB.

* I parsed commandline arguments in my own code (see comment about external libraries above).

* The log file is truncated and reloaded to the database each time the program is run.  The sample log file
takes about 30-60 seconds to load on my machine, which is running a local MySQL database.

* Overall, I found this to be a fun problem to solve.  What a great way to screen candidates!   A little challenging but
not overly complicated.