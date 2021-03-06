# Yoga #

Yoga enables client-defined relational queries and out-of-the-box HATEOAS on **your existing** Java-based REST server.

* 3-4x faster speeds in high-latency (e.g. mobile) apps
* Simplified SOA development (esp. across multiple teams)
* Browsable APIs

This demo shows how aggregation can accelerate a service with multiple nested calls: http://yoga-demo-springmvc.cloudfoundry.com/#demo

## Supported Frameworks ##
 + RESTEasy
 + Any RESTful Spring MVC application

## Wiki ##
Most of the documentation is here: https://github.com/skyscreamer/yoga/wiki

## Inspiration ##
LinkedIn’s JavaOne presentation on building flexible REST interfaces (http://blog.linkedin.com/2009/07/08/brandon-duncan-java-one-building-consistent-restful-apis-in-a-high-performance-environment/)

## How does this work? ##
Take for example a music-based social network site.  If I'm a user of that site, one of the first things I'd like to do is see my friends' favorite bands.  A standard RESTful navigation approach involves two steps:

1. Get my friends
2. Get their favorite artists

If I only have two dozen friends on this site, I'm already making 25 queries to get this data.  On mobile applications, where the latency for individual calls run 100-200ms, this could take several seconds.  But imagine I've got a more typical average of 120 friends.  This is now taking nearly 20 seconds.  (And this is one of the simplest use cases.)

With a REST aggregator, you can do it in one call:

    GET /user/1.json?selector=:(friends:(favoriteArtists))

As you go deeper into the data tree, the effect multiplies geometrically.  In a more complicated example, let's say I want to compile a play list of songs from my friend's favorite artists.  This would require thousands of queries in strict RESTful navigation, but it can still be aggregated as a single query:

    GET /user/1.json?selector=:(friends:(favoriteArtists:(albums:(songs))))

Of course, you can write a custom query each time you need something bigger than a single entity or a 1-deep list, but if you can have a framework do it for you it reduces development time, testing time, and code complexity.

Find a more detailed explanation of our selectors here: https://github.com/skyscreamer/yoga/wiki/REST-Selectors

In short:

    What                    Why
    ----------              ---
    Fewer requests          Faster response
    Fewer sockets           Fewer servers
    Simpler client code     Happier client developers, less head banging

## How Do I Use It? ##
Yoga works with existing framworks.  To hook it up, you need to add some annotations and a few lines of configuration.  Check out the documentation on our wiki for a more detailed explanation.

For advanced users, we provide hooks to further extend Yoga's capabilities.

Once Yoga has been set up, clients can build and modify their own relational queries without any server-side programming.  
