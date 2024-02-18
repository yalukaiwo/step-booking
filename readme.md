
# Booking App

Step project by Maxym Zymyn & Luka Ponomarenko



## Features

- Random flight generation
- Transfer flights
- User authentication
- Flight booking




## Task distribution

- Luka: Flights & Bookings DAO / Service / Controller
- Maxym: Users DAO / Service / Controller & Console


## Usage

To generate random flights run

```bash
  GenerateRandom.java
```
To use the app run
```bash
  Main.java
```
App comes with a pre-made database of 5000 flights which are distributed across 3 days from creation. If flights are outdated, consider deleting
```bash
flights.bin
```
and running the randomizer again on desired amount of flights.