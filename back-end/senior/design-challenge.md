# Requirements
As the company continues to grow, the demands on our platform are also on the increase. As part of the backend engineering team, you notice that the platform is really struggling to handle a high volume of traffic when we offer a deal for investors. This area is critical for Yieldstreet, the platform should be able to handle a good amount of traffic and not degrade with usage. You are given total freedom to come up with a solution that satisfies the criteria below.

### Deal Restrictions
1. Each deal cannot allow more than 200 users;
1. The total amount of investment requests cannot be more than the amount of the deal itself.

### Process
The following steps are executed in order for a user to get into a deal:

1. Record the investment request into the DB;
1. Update the user record indicating it has a pending investment;
1. Call a third party service which pulls the money;
1. Call another third-party service to generate the required documents;
1. Return the outcome to the user;
1. Send a confirmation email to the user.

### Process Parameters:
The following are some requirements/constraints that should be included in the solution:

1. Handling of a fairly big amount of requests (~500/s) and reply in a timely manner;
1. Have a fair process, where the users are allowed into the deal as the requests came in  (first come first serve);
1. Respect the deal constraints;
1. Have the whole process as atomic, either the whole process finishes successfully or the system needs to be able to revert any changes done in order to return to a stable state;
1. The solution should be scalable horizontally as needed (through a manual process, does not need to auto-scale);
1. You’re allowed to suggest changes to requirements if you think that would help the solution.

# The Challenge
Given these sets of requirements and constraints, how would you architect the system to be efficient, fair and resilient. You can use any set of technologies that you are familiar with to solve the problem stated above.