# Control Route Optimizer
A small web application that tries to optimize (to an extent since it's
basically the traveling salesman problem) the addresses being visited
in a day by the civil protection team that are checking whether the shelters
are okay. Simplified constraints:

- The time is pre agreed upon
- Each address at a time can be dispatched to one of the teams available
- Minimize driving time (I intend using the Bing Maps API to get that)
- I implemented it in a greedy way - the first team gets dibs on the closest
  next destination, then comes the second, etc.

Based heavily on https://auth0.com/blog/developing-jsf-applications-with-spring-boot/

You'll need a Bing Maps key, to be stored in the environment variable CONTROL_ROUTE_OPTIMIZER_BINGMAPS_KEY

## Next steps
If we actually want to use it, we have to import the shelter locations from Abri 2000
and the meeting requests from our self-schedule platform. We *could* print the assignment
directly on the control form, but those are normally printed in advance (until such time as
the control form is fully digital as well).
