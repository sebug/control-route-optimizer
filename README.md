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
to ensure correct addresses with the already imported meeting requests from our self-schedule platform. We'll also have to better optimize for the fact that we normally have less cars
than teams - Idea there would be to pick the n closest addresses for a car containing n
teams and then give it to them either randomly or by optimizing walking distance.

## Internal Deployment
A very unoptimized Docker image is built from the sources (there were some issues with the
WEB-INF folder and the files contained there being part of the run that I haven't figured
out how to package yet).

In the compose folder you'll find a docker-compose.yml file. put your CONTROL_ROUTE_OPTIMIZER_BINGMAPS_KEY in the variables.env file and then docker-compose up and
you can access it internally on your-openmediavault-ip-address:9095 .


