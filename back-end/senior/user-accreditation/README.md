# User Accreditation Challenge
This is a simulation of the user accreditation service, part of YieldStreet's
recruiting challenges. To complete this challenge, you need to implement the
"Submit Accreditation" endpoint in a satisfactory manner.

This is a fairy open and complex challenge and you have a limited time to work
on it. Don't worry about getting every little detail right; do your best, and
if your solution has known shortcomings, document them.

### Running the application
All you need to run this application locally is Docker and Docker Compose. If
you're on macOS or Windows, you can get both by installing [Docker Desktop][1]
for your platform. If you're on Linux you should know what to do.

You can start the application in development mode by running `docker-compose up app`
in the application folder. This will start docker containers for Zookeeper,
Kafka and the application development server. After a while, you should see
the following lines in the console:

```
app_1        | --- (Running the application, auto-reloading is enabled) ---
app_1        | [info] p.c.s.AkkaHttpServer - Listening for HTTP on /0.0.0.0:9000
app_1        | (Server started, use Enter to stop and go back to the console...)
```

You can now reach the application at http://localhost:9000. Use `curl` or other
similar tools to submit test requests:

```
curl -X POST \
  -H 'Content-Type: application/json' \
  -d '{"userId": 1234, "accreditation":{}}' \
  http://localhost:9000/user/accreditation
```

The server detects changes to the source code and reload whenever required. If
you're seeing any strange behavior, try restarting the docker container.

[1]: https://www.docker.com/products/docker-desktop

#### Running without docker
You can run the application without Docker if you know what you're doing. In
this case you'll need a Java SDK version 11 or higher, and sbt installed on 
your local machine. If you want to go down this road, you're on your own.

## Understanding the project
This application is built on top of the [Play Framework][4], version 2.7.4 and
its transitive dependencies, like [Akka][5] 2.5.20. Additional Akka modules for
streaming data to and from Kafka (aka [Alpakka Kafka][6]) is included, along
with utility libraries like [Lombok][7].

If you're not familiar with Play, we recommend skimming through its
documentation. Quickly learning enough about Play to implement your solution
is part of the challenge. You're not required to use all included frameworks,
and you're free to add additional dependencies. If you need external resources
not already provided in `docker-compose.yml` (databases, etc.), it would be
great if you could set that up, but it's not required as long as you include
sufficient instructions. 

Your entry point is the `AccreditationController` class. Its `#accreditation`
method will be called on every HTTP request to the `POST /user/accreditation`
endpoint. Parsing of the request body is already implemented; your work should
pick it up from there.

[4]: https://www.playframework.com
[5]: https://akka.io
[6]: https://doc.akka.io/docs/alpakka-kafka/current/home.html
[7]: https://projectlombok.org 
 
## What do you need to do
You need to provide an implementation for the `POST /user/accreditation`
endpoint. This endpoint is called when users submit a new accreditation
data, which then must pushed to a third party verification service. The
endpoint takes a payload in the following shape:

```json 
{
  "userId": "123bs34",
  "accreditation": {}
}
```

That is, the request body must:
* Be a valid JSON object;
* Contain a `userId` property with a string key;
* Contain an `accreditation` property with an arbitrary JSON object value.

The endpoint must return with a `200 OK` response once the request is accepted.
You're free to choose when exactly that happens, but keep in mind that the point
of this exercise is to provide a service with consistent response times that are
not affected by the underlying verification service. Note that the response
payload does _not_ include any verification results, so you don't have to wait
for the verification service.

If the client request is malformed and can't be accepted, the endpoint should
return a `400 Bad Request` response. You're free to use other HTTP status codes
for conditions not described here. Clients of this endpoint are expected to
retry requests that complete with any status code other than 200 or 400.

There's an example implementation in
[PR #25](https://github.com/yieldstreet/challenges/pull/25) for this repository.
Take a look at it if you have no idea where to start, but be warned that this
implementation is naïve and flawed.g 

### Queuing requests
As hinted above, to provide consistent latencies, the accreditation endpoint
should acknowledge requests as soon as possible, queuing them for later
processing by the verification service. You have Akka and Kafka at your
disposal, but feel free to pick any other solution. Bonus points if the service
is resilient to crashes, and can pick up acknowledged but unprocessed requests
after a restart.

## The verification service
All code required to interact with the verification service is already provided.
Please use the `VerificationServiceProvider` interface to make requests. The
provided implementation simulates an unreliable third party service with a
variable latency between 50ms and 5s; additionally, there's a 2% chance that
every request will fail outright.
