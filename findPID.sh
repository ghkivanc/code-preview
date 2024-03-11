#!/bin/bash

eval "ps -A | grep $1 | egrep '[0-9]{0,4}'"

shift