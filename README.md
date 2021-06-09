# Control Route Optimizer
A small web application that tries to optimize (to an extent since it's
basically the traveling salesman problem) the addresses being visited
in a day by the civil protection team that are checking whether the shelters
are okay. Simplified constraints:

- The time is pre agreed upon
- Each address at a time can be dispatched to one of the teams available
- Minimize driving time (I intend using some maps API to get that)

Based heavily on https://auth0.com/blog/developing-jsf-applications-with-spring-boot/

You'll need a Bing Maps key, to be stored in the environment variable CONTROL_ROUTE_OPTIMIZER_BINGMAPS_KEY
