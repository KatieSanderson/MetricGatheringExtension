README

MetricGatheringExtension is a metric-gathering extension for a web application.

It gathers and displays maximum, minimum, average of Request Time and Response Size, metrics about requests and responses served by the application. Metrics can be viewed via (/metrics/*) URLs

It allows users to look up a specific request's request time and response size by a unique identifier. Requests can be viewed via (/requestMetrics/*) URL and inputting a valid request ID.

Build status (Travis CI)

[![Build Status](https://travis-ci.com/KatieSanderson/MetricGatheringExtension.svg?branch=master)](https://travis-ci.com/KatieSanderson/MetricGatheringExtension)

METRICS GATHERED
1. Request time (milliseconds) - time spent between when the application starts to process the request and the time when the application sends the response to the client
    * Maximum, minimum, and average request time of all requests
    * Historical request time by unique identifier
2. Response size (bytes) - size of the HTTP response body in bytes
    * Maximum, minimum, and average request size of all requests
    * Historical request size by unique identifier
3. Request IDs - randomly assigned unique by metric gathering extension
    * A user can obtain the request ID through HTTP header "ID"

DEPLOY INSTRUCTIONS
Directions using this zip after extraction (all in terminal, project folder):
1. Command: 'mvn jetty:run'
    * This will build the project and start a Jetty server
2. In a browser, options:
    * Request web pages with metric gathering extension (via servlet URL mapping)
    * View historical metrics (via /metrics/*)
    * View a specific request's request time and response size by unique identifier (via /requestMetrics/*)

REQUIREMENTS
1. Any servlet extended with metric gathering extension must set the response content length
2. HTTP URLs are compatible with the metric gathering extension

ADDING METRIC GATHERING EXTENSION TO SERVLET
1. Ensure desired servlet is configured properly in the web application
2. Add servlet to MetricFilter's filter mapping in the web.xml
   * Syntax for adding filter mapping, where "ServletName" is the associated web.xml servlet name
       ~~~~
       <filter-mapping>
           <filter-name>MetricsFilter</filter-name>
           <servlet-name>"ServletName"</servlet-name>
       </filter-mapping>
       ~~~~
   * No additional compilation is required if only modifying the web.xml for filter mapping
3. If desired, reset metrics data by deleting "metrics.txt" (MetricGatheringExtension\metrics.txt)
4. Run normally

ADDING METRIC GATHERING EXTENSION TO NEW WEB APPLICATION
1. The following classes or files are required to run servlets with metric gathering extension:
    * Metrics.java (\MetricGatheringExtension\src\main\java\Metrics.java)
    * MetricsFile.java (\MetricGatheringExtension\src\main\java\MetricsFile.java)
    * MetricsFilter.java (\MetricGatheringExtension\src\main\java\MetricsFilter.java)
    * RequestData.java (\MetricGatheringExtension\src\main\java\RequestData.java)
    * RequestMetricsServlet.java (\MetricGatheringExtension\src\main\java\RequestMetricsServlet.java)
    * DisplayMetricsServlet.java (\MetricGatheringExtension\src\main\java\DisplayMetricsServlet.java)
    * web.xml (\MetricGatheringExtension\src\main\webapp\WEB-INF\web.xml)
        * Servlet, Servlet Mappings, and Filter Mappings will need modified for new web application's servlets
    * pom.xml (\MetricGatheringExtension\pom.xml)
2. Ensure desired servlet is configured properly in the web application
3. Add servlet to MetricFilter's filter mapping in the web.xml
   * Syntax for adding filter mapping, where "ServletName" is the associated web.xml servlet name
       <filter-mapping>
           <filter-name>MetricsFilter</filter-name>
           <servlet-name>"ServletName"</servlet-name>
       </filter-mapping>
4. Run normally