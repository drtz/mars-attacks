## Overview

This is a Spring Boot Web MVC application that serves an API which, given a valid Earth date,
fetches a single image for that date from the [NASA Mars Rover Photos API](https://api.nasa.gov)
taken by the Curiosity rover.

After being fetched, images are cached in an `image-cache` directory for quicker response on
subsequent requests.

Images for a [small set of dates](https://github.com/drtz/mars-attacks/blob/master/src/main/resources/preload-dates.txt) are automatically loaded and cached when the application starts.

## Demo

A live demo of this application is running on a shared Kubernetes cluster in [Kubesail](https://kubesail.com/).

- http://marsattacks.drtz.net/photos/?date=02/27/17
- http://marsattacks.drtz.net/photos/?date=June%202,%202018
- http://marsattacks.drtz.net/photos/?date=Jul-13-2016
- http://marsattacks.drtz.net/photos/?date=April%202031,%202018

## Future Enhancements

### Testing

I did the bare minimum for testing just to wrap my head around the very basics of JUnit. Extensive
unit testing and load testing would be ideal for a real-world deployment.

### Cache NASA API Responses

The NASA API seems to be pretty slow to respond. Since we're dealing with historical data from a
Mars rover that's no longer active, caching the data retrieved from NASA should be safe.

### Frontend App

There's no frontend to this application, so it's not really user-friendly at the moment. A nice UI would make it much more palettable.

### Non-Blocking IO

IO operations in this app will block the application process from being able to handle concurrent
requests. Each instance of the application would be able to handle a lot more traffic with a
non-blocking server architecture.

## Building

### Gradle

```
./gradlew build
```

### Docker

```
APP_VERSION=$(./gradlew properties | grep version | sed 's/version: //') && docker build --build-arg APP_VERSION=${APP_VERSION} -t drtz/marsattacks:${APP_VERSION} .
```

## Running

Before running, [generate a NASA API key](https://api.nasa.gov).

### Gradle

```
NASA_API_KEY=<api key> ./gradlew bootRun
```

### Docker

```
docker run -p 8080:8080 --env NASA_API_KEY=<api key> --rm drtz/marsattacks:latest
```

## Deploying

### Kubernetes

```
kubectl apply -f deploy/secret.yaml
kubectl edit secret marsattacks # manually apply NASA API Key

kubectl apply -f deploy/volume.yaml
kubectl apply -f deploy/deployment.yaml
kubectl apply -f deploy/service.yaml
kubectl apply -f deploy/ingress.yaml
```

## Disclaimer

This is my first attempt at building a Java web server. I make no claim that this is exemplary in
any way, shape, or form.
