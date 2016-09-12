# IoT Board Service

The IoT board service is an integration service between Game On! and the IBM Watson IoT platform which allows Game On! developers to control physical devices. It can also provide virtual versions of these devices to allow for easy testing and to scale beyond the number of available devices, whilst still maintaining the same interfaces and protocols.

## Game On! Room Developers

If you are a Game On! room developer looking to take advantage of the devices provided either by yourself, or someone else, then this is the section for you. It will walk you through any setup required and then explain how to control the device. As more device providers plug in to Game On! they will be added to this section. If you are a device provider e.g. allowing a room to respond to a button push, then you'll need to refer to the Device Providers section.

# Controlling a light from your room

For Game On! developers that want to control a light to indicate activity within your room, such as a player leaving or entering the room, this is a two step process.

## Using your phone

It may be that there isn't any physical lights for you to communicate with, in which case you can use your phone to simulate
a light being turned on and off. Don't worry, if you add a physical light later on it will be kept in sync.

* Go to http://iotboard.mybluemix.net/iotboard/
* Enter your ```Game On! ID``` in the box provided and click register. If you need to lookup your ID, then it can be found here https://game-on.org/#/play/me.

You can verify that the registration process has completed successfully by viewing the **IoT Foundation Information** panels on the screen. They will show the connection details that you are using and the current status of your connection to the backend IoT Foundation service.

### Testing

At the bottom of the page there are some test buttons which allow you to simulate receiving messages without having your room send them. This is useful if you just want to see what the page should look like when a message is received (sometimes it's hard to tell if something is working, if you don't know what 'working' looks like !), or you are working offline.

* **Test receiving messages** : Simulates receiving IoT messages for this device.

## Turning the lights on !

Now that you have a device configured (virtual or physical), you can now control the light (or lights, depends on the device
  provider) via the following simple REST call.

```
POST : http://iotboard.mybluemix.net/iotboard/v1/control
{
  "siteId":<the site ID for your room>,
  "roomName": <the name of your room>,
  "playerId": <your Game On! ID>,
  "data": {
    "lightId": "player",
    "lightState": <true/false to turn the light on or off>
  }
}
```

## A room can also be a device

More to come on how a room can act as a device.
