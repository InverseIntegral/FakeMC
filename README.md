# FakeMC

[![Build Status](https://travis-ci.org/InverseIntegral/FakeMC.svg?branch=master)](https://travis-ci.org/InverseIntegral/FakeMC)
[![Gitter](https://badges.gitter.im/InverseIntegral/FakeMC.svg)](https://gitter.im/InverseIntegral/FakeMC?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
## Description
FakeMC is a fake minecraft server that shows false information in the server list. You can configure specific information using a `.json` configuration file.

This is what a fake ping response looks like:
![Example Entry](docs/example.png)

## How does this work?
This server implementation handles specific minecraft packets (handshake, ping etc.) to provide fake information to a client. It does handle the following minecraft packets:
* Handshake
* Status Request
* Ping
* Login

## What has to be done?
* Configuration for motds, ports, player amounts etc.
* Favicon using a png image
* CI and repository
* Implementation using Project Reactor and Spring

## Credits
* [@michidk](https://github.com/michidk) implemented a similar version of this project but without Netty. [FakeMCServer](https://github.com/michidk/FakeMCServer)
* I took the protocol information from [this](http://wiki.vg/Protocol) wiki.
