{\rtf1\ansi\ansicpg1252\cocoartf1187\cocoasubrtf370
{\fonttbl\f0\fswiss\fcharset0 ArialMT;}
{\colortbl;\red255\green255\blue255;\red26\green26\blue26;\red255\green255\blue255;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\deftab720
\pard\pardeftab720

\f0\fs26 \cf2 \cb3 @echo off\
for /f "usebackq" %%Z in (`tasklist /nh /fi "imagename eq DylosLogger.exe"`) do if %%Z==INFO: goto processnotrunning\
echo DylosLogger is running\
exit\
:processnotrunning\
cd C:\\Program Files\
start DylosLogger.exe\
}