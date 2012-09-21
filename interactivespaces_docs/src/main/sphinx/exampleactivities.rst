Interactive Spaces Example Activities
***********************

The Interactive Spaces Workbench contains a variety of example
activities in its *examples* folder. These are to provide you with some
very basic examples of the sorts of things you can do with Interactive
Spaces.

The number of examples will continue to grow as more functionality is placed
into Interactive Spaces.

The Simple Events
============================

These activities merely log some Interactive Spaces events which take
place when a Live Activity runs. These examples can help you understand 
what events happen in what order.

They are written in a variety of programming languages.

* interactivespaces-example-activity-java-simple
* interactivespaces-example-activity-script-javascript
* interactivespaces-example-activity-script-python

You will need to hit the *Configure* button to see the configuration update
event. *Edit Config* should be used first if you have never configured
the activity.

If you want less output from these Activities, find *onActivityCheckState*
and change *info* to *debug*. This means that checking activity state will
only be logged if the logging level for the activity is set to DEBUG.

Native Activities
===================

You can start and stop native activities with Interactive Spaces. This
makes it easy for you to write activities in openFrameworks and other languages
that don't run inside the Interactive Spaces container.

* interactivespaces-example-activity-native

This example uses the Linux *mpg321* to play an audio file found in the
activity.

If you want to use Interactive Spaces communication protocols, which you should,
you will need to have your Activity speak with something directly running in
Interactive Spaces. Sockets and Web Sockets are supported out of the box.

Web Activities
==============

You can easily use browser-based applications to provide a UI for your
Activities.

Simple Web Activity
------

If your Live Activity need only start up a browser which contains a simple
Javascript-based implementation, you can place your HTML, CSS, and Javascript
inside our activity and configure Interactive Spaces to run it.

* interactivespaces-example-activity-web

This is a standalone browser activity which shows a single page and not much
else.

Web Socket Activities
---------------------

The following Activities are browser based, but use Web Sockets so that
the Activity code running in the browser can communicate with other 
Activities in the space.

* interactivespaces-example-activity-web-java
* interactivespaces-example-activity-web-script-javascript
* interactivespaces-example-activity-web-script-python

Routable Activities
===================

Interactive Spaces really only become interesting when your space has
event producers and event consumers running as their own Activities which
speak to each other.

In Interactive Spaces, this communication can be done with *routes*. You can
have *input routes* which listen for messages and deliver them to their 
Activities, and *output routes* which write messages out for an input route
somewhere to receive.

* interactivespaces-example-activity-routable-input-script-python
* interactivespaces-example-activity-routable-output-script-python

You can run both of these activities on the same controller or on separate
controllers. You can also run multiple versions of each activity (such as
multiple versions of the input route sample), but
only do that on separate controllers.

Interactive Spaces uses ROS for its underlying communication. Usage of ROS
can be somewhat intimidating, so the above examples use JSON-based 
communication. Messages are dictionaries of data which can be turned
into JSON messages and read from JSON messages.

Topic Bridges
=============

Interactive Spaces makes it possible for Live Activities to communicate
with each other. At some point you may find yourself having an event producer
and an event consumer which need to talk to each other, but they were not
written with each other in mind so their messaging protocols are different.

Topic Bridges make it possible for you to translate from one message protocol
to another by writing a tiny script which merely says which field or fields 
from the source message are combined to create the fields of the destination
message.

* interactivespaces-example-activity-bridge-topic
