![EcoCar Launch Control](https://puu.sh/uxNIi/6fb0780bb8.png)

## Description

Launch Control is the [University of Alberta EcoCar Team's](http://ualberta-ecocar.ca/) new car and driver management system. In the past with all our embedded system, we have found it difficult to properly collect and log data in meaningful ways. Log files are great, however they don't tell you at the moment when the car is on the track how things are going or what is wrong in a crunch. This Android app aims to solve this issue while providing the driver with real time information.

This app is one of three software pieces for the Launch Control system. The other two being a Node.js server that hosts all the data and is in charge of passing out the information to all the different clients and the local web app that displays the info to users outside of the car.

## What does the app look like?

### Welcome Screen:

![Welcome Screen Image](https://puu.sh/vWeUR/e8ecf3b561.png)

### Drivers Display:

![Drivers Display Image](https://puu.sh/vWeXQ/b062b74d40.png)

### Time Keepers Display:

![Time Keepers Display Image](https://puu.sh/vWeVY/d427981e9f.png)

## How to get the app

For EcoCar team members, the app is currently located in the EcoCar Electrical Google Drive in the following path.

![Path in Drive](https://puu.sh/uxuy1/bbd236a8ab.png)

To install the app you must have an Android phone and [allow apps from Unknown Sources](https://support.google.com/nexus/answer/2812853?hl=en). When attempting to install the app, you will most likely be prompted with a message on how to allow apps from Unknown Sources.

To install the app, on your phone use google drive and navigate to the path above, download the application and select it in your downloads folder to install.

If you are not a member of EcoCar and still want to poke around, you will need to clone the project and setup your own firebase account for analytics, a Node.js project with SocketIO and populate the endpoint.xml file with your Node.js project websocket URLs. The out going JSON object format is in the WebDataPacket class.

## How to send data from the car to the app

For data communication JSON objects will be sent over serial via Bluetooth. The data types of the expected JSON values are below.
```
  speed: float
  fc_voltage: float
  motor_current: float
  fc_alarm_code: int
  fc_state: int
  fc_temp: float
  efficiency: float
```
An example of the full data format for the JSON object is below.
```JSON
{
  "speed": 23.2,
  "fc_voltage":12.1,
  "motor_current":13.8,
  "fc_alarm_code":14,
  "fc_state":3,
  "fc_temp":20.3,
  "efficiency":200.3
}
```

If the String does not match the format, the app will simply reject it. The code above has been expanded, please compress the JSON string to a single line and use the println function to avoid sending weird strings. An example of sending a JSON object using an Arduino Uno and a Sparkfun Bluetooth Mate is below.

```C++
#include "Arduino.h"
#include <SoftwareSerial.h>

int bluetoothTx = 2;
int bluetoothRx = 3;

SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

void setup()
{
  Serial.begin(9600);
  bluetooth.begin(115200);
  bluetooth.print("$");
  bluetooth.print("$");
  bluetooth.print("$");
  delay(100);
  bluetooth.println("U,9600,N");
  bluetooth.begin(9600);
}

void loop()
{
  int randomSpeed = random(40);
  bluetooth.print("{\"speed\":");
  bluetooth.print(randomSpeed);
  bluetooth.println(",\"fc_voltage\":12,\"motor_current\": 13,\"fc_alarm_code\":14,\"fc_state\":3,\"fc_temp\":20.2,\"efficiency\":200.3}");
  delay(500);
}
```
