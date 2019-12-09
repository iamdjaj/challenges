# Back End Challenges
You will find two assignments in the sub-folders here, each with specific
instructions on the assignment.

1. The first is a review challenge, intended to gauge your ability to spot code
   issues. (30 minutes)
2. The second is a set of implementation challenges, intended to gauge your
   understanding and familiarity with writing performant and scalable code. It
   assumes familiarity with Java 8, which is our primary back-end language. (4
   hours, 30 minutes)

Please work on **all** of them. We ask that you spend no more than 5 hours in
total working on these.

Also, know that we care more about quality rather than how far you get. If
you’re not able to finish in 5 hours, please don’t sweat it.

## Working on the compilable project
This repository includes compilable code and tests that should verify your
solution to the challenges. This is optional; we're more interested in your
reasoning than in executable code.

That said, if you want to run the tests you'll need to install [sbt][1]. Follow
the instructions on the website, then run `sbt test` in this folder to run the
test suite. You can also import the project into an IDE like [IntelliJ IDEA][2]
or [Eclipse][3].

There are only four files that need to be modified in your solution:

* [StuffService.java](src/main/java/yieldstreet/challenges/singleton/StuffService.java) 
* [StuffRepository.java](src/main/java/yieldstreet/challenges/concurrent/StuffRepository.java) 
* [BlockingToAsyncStuffRepository.java](src/main/java/yieldstreet/challenges/concurrent/BlockingToAsyncStuffRepository.java) 
* [CallbackToAsyncStuffRepository.java](src/main/java/yieldstreet/challenges/concurrent/CallbackToAsyncStuffRepository.java)

Each of these files has a `TODO` comment in the exact place you need to make
changes. Of course, you're free to examine the test cases to understand what
they're expecting from your solution.  

## Submitting your solution
You have two options to submit your solutions:

1. Email the solution to challenges@yieldstreet.com in a zip archive. 
2. If you prefer to use GitHub, fork the `challenges` repo to a **private**
   repo on your personal GitHub account, and add the following GitHub users as
   collaborators: `uqbar`, `mbrito-yieldstreet`, `paulcutajar` and
   `rfialho-yieldstreet`. Email challenges@yieldstreet.com to let us know the
   solution has been pushed.

**IMPORTANT: PLEASE MAKE SURE THE FORKED REPO IS PRIVATE, AND PLEASE DO NOT
SUBMIT A PULL REQUEST ON THIS REPO**

[1]: https://www.scala-sbt.org
[2]: https://www.jetbrains.com/idea
[3]: https://www.eclipse.org
