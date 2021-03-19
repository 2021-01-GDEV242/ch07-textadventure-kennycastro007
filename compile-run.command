#!/bin/bash
javac --release=8 -d temp/ *.java
cd temp
java MainClass
cd ../